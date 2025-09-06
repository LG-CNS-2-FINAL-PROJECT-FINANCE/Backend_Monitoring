package com.ddiring.Backend_Monitoring.service.FraudDetection;

import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestAcceptedEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestRejectedEvent;
import com.ddiring.Backend_Monitoring.event.dto.producer.fraud.TradeProjectHighFailureRateEvent;
import com.ddiring.Backend_Monitoring.event.dto.producer.fraud.TradeUserFrequencyRateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class TradeFrequencyDetection {

    public static void detectTradeFrequency(KStream<String, TradeEvent> events) {
        // 이벤트 유형에 따라 스트림 분기 및 명확한 캐스팅
        KStream<String, TradeRequestAcceptedEvent> acceptedEvents = events
                .filter((key, value) -> value instanceof TradeRequestAcceptedEvent)
                .mapValues((key, value) -> (TradeRequestAcceptedEvent) value);

        KStream<String, TradeRequestRejectedEvent> rejectedEvents = events
                .filter((key, value) -> value instanceof TradeRequestRejectedEvent)
                .mapValues((key, value) -> (TradeRequestRejectedEvent) value);

        KTable<Windowed<String>, Long> accpetedCounts = acceptedEvents.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();
        KTable<Windowed<String>, Long> rejectedCounts = rejectedEvents.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();

        AtomicReference<Long> atomicTotalEventCount = new AtomicReference<>(0L);
        accpetedCounts.join(
                rejectedCounts,
                (acceptedValue, rejectedValue) -> {
                    atomicTotalEventCount.set((acceptedValue != null ? acceptedValue : 0) + (rejectedValue != null ? rejectedValue : 0));
                    return null;
                }
            );

        // 구매자 및 판매자 빈도 비율 탐지
        detectBuyerFrequencyRate(atomicTotalEventCount.get(), acceptedEvents, rejectedEvents);
        detectSellerFrequencyRate(atomicTotalEventCount.get(), acceptedEvents, rejectedEvents);
    }
    
    private static void detectBuyerFrequencyRate(Long totalEventCount, KStream<String, TradeRequestAcceptedEvent> acceptedStream, KStream<String, TradeRequestRejectedEvent> rejectedStream) {
        KStream<String, TradeRequestAcceptedEvent> keyedAccepted = acceptedStream.selectKey((key, value) -> value.getPayload().getBuyerAddress());
        KStream<String, TradeRequestRejectedEvent> keyedRejected = rejectedStream.selectKey((key, value) -> value.getPayload().getBuyerAddress());

        KTable<Windowed<String>, Long> buyerSuccessCounts = keyedAccepted.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();
        KTable<Windowed<String>, Long> buyerFailureCounts = keyedRejected.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();

        buyerSuccessCounts.join(
                buyerFailureCounts,
                (successValue, failureValue) -> {
                    long buyerSuccessCount = successValue != null ? successValue : 0;
                    long buyerFailureCount = failureValue != null ? failureValue : 0;
                    long buyerTotalCount = buyerSuccessCount + buyerFailureCount;

                    if (totalEventCount > 0 && buyerTotalCount > 0) {
                        double buyerFrequencyRate = (double) buyerTotalCount / totalEventCount;
                        if (buyerFrequencyRate > 0.1) { // 예시 임계값
                            log.info("[FraudDetection] 이상 패턴 탐지 - 구매자 주소: {}, 성공 횟수: {}, 실패 횟수: {}, 빈도 비율: {}",
                                    (successValue != null ? successValue : "N/A"),
                                    buyerSuccessCount,
                                    buyerFailureCount,
                                    buyerFrequencyRate);

                            return new Object() {
                                public final double frequencyRate = buyerFrequencyRate;
                                public final long userTotalCount = buyerTotalCount;
                                public final long eventCount = totalEventCount;
                                public final long timeWindowMinutes = 60;
                            };
                        }
                    }
                    return null;
                }
        )
                .filter((windowedKey, value) -> value != null)
                .toStream()
                .mapValues((windowedKey, value) -> {
                    String buyerAddress = windowedKey.key();

                    return TradeUserFrequencyRateEvent.ofBuyer(
                            buyerAddress,
                            value.userTotalCount,
                            value.eventCount,
                            value.frequencyRate
                    );
                })
                .selectKey((windowedKey, value) -> windowedKey.key() + "@" + windowedKey.window().start() + "-" + windowedKey.window().end())
                .to("TRADE_FREQUENCY_RATE", Produced.with(Serdes.String(), new JsonSerde<>(TradeUserFrequencyRateEvent.class)));
    }

    private static void detectSellerFrequencyRate(Long totalEventCount, KStream<String, TradeRequestAcceptedEvent> acceptedStream, KStream<String, TradeRequestRejectedEvent> rejectedStream) {
        KStream<String, TradeRequestAcceptedEvent> keyedAccepted = acceptedStream.selectKey((key, value) -> value.getPayload().getSellerAddress());
        KStream<String, TradeRequestRejectedEvent> keyedRejected = rejectedStream.selectKey((key, value) -> value.getPayload().getSellerAddress());

        KTable<Windowed<String>, Long> sellerSuccessCounts = keyedAccepted.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();
        KTable<Windowed<String>, Long> sellerFailureCounts = keyedRejected.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();

        sellerSuccessCounts.join(
                        sellerFailureCounts,
                        (successValue, failureValue) -> {
                            long sellerSuccessCount = successValue != null ? successValue : 0;
                            long sellerFailureCount = failureValue != null ? failureValue : 0;
                            long sellerTotalCount = sellerSuccessCount + sellerFailureCount;

                            if (totalEventCount > 0 && sellerTotalCount > 0) {
                                double sellerFrequencyRate = (double) sellerTotalCount / totalEventCount;
                                if (sellerFrequencyRate > 0.1) { // 예시 임계값
                                    log.info("[FraudDetection] 이상 패턴 탐지 - 판매자 주소: {}, 성공 횟수: {}, 실패 횟수: {}, 빈도 비율: {}",
                                            (successValue != null ? successValue : "N/A"),
                                            sellerSuccessCount,
                                            sellerFailureCount,
                                            sellerFrequencyRate);

                                    return new Object() {
                                        public final double frequencyRate = sellerFrequencyRate;
                                        public final long userTotalCount = sellerTotalCount;
                                        public final long eventCount = totalEventCount;
                                        public final long timeWindowMinutes = 60;
                                    };
                                }
                            }
                            return null;
                        }
                )
                .filter((windowedKey, value) -> value != null)
                .toStream()
                .mapValues((windowedKey, value) -> {
                    String sellerAddress = windowedKey.key();

                    return TradeUserFrequencyRateEvent.ofBuyer(
                            sellerAddress,
                            value.userTotalCount,
                            value.eventCount,
                            value.frequencyRate
                    );
                })
                .selectKey((windowedKey, value) -> windowedKey.key() + "@" + windowedKey.window().start() + "-" + windowedKey.window().end())
                .to("TRADE_FREQUENCY_RATE", Produced.with(Serdes.String(), new JsonSerde<>(TradeUserFrequencyRateEvent.class)));
    }
}