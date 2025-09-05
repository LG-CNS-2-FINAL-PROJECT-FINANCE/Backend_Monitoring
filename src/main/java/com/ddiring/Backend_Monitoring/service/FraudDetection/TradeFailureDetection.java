package com.ddiring.Backend_Monitoring.service.FraudDetection;

import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestAcceptedEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestRejectedEvent;
import com.ddiring.Backend_Monitoring.event.dto.producer.fraud.TradeUserHighFailureRateEvent;
import com.ddiring.Backend_Monitoring.event.dto.producer.fraud.TradeProjectHighFailureRateEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.Objects;

public class TradeFailureDetection {

    public static void detectTradeFailure(KStream<String, TradeEvent> events) {
        // 이벤트 유형에 따라 스트림 분기 및 명확한 캐스팅
        KStream<String, TradeRequestAcceptedEvent> acceptedEvents = events
                .filter((key, value) -> value instanceof TradeRequestAcceptedEvent)
                .mapValues((key, value) -> (TradeRequestAcceptedEvent) value);

        KStream<String, TradeRequestRejectedEvent> rejectedEvents = events
                .filter((key, value) -> value instanceof TradeRequestRejectedEvent)
                .mapValues((key, value) -> (TradeRequestRejectedEvent) value);

        detectProjectFailureRate(acceptedEvents, rejectedEvents);
        detectBuyerFailureRate(acceptedEvents, rejectedEvents);
        detectSellerFailureRate(acceptedEvents, rejectedEvents);
    }

    private static void detectProjectFailureRate(KStream<String, TradeRequestAcceptedEvent> acceptedStream, KStream<String, TradeRequestRejectedEvent> rejectedStream) {
        // projectId를 기준으로 키 재설정
        KStream<String, TradeRequestAcceptedEvent> keyedAccepted = acceptedStream.selectKey((key, value) -> value.getPayload().getProjectId());
        KStream<String, TradeRequestRejectedEvent> keyedRejected = rejectedStream.selectKey((key, value) -> value.getPayload().getProjectId());

        // 1시간 윈도우를 사용하여 성공/실패 건수 집계
        KTable<Windowed<String>, Long> successCounts = keyedAccepted.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();
        KTable<Windowed<String>, Long> failureCounts = keyedRejected.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();

        // 조인을 통해 실패율 계산 및 이상 탐지
        failureCounts.join(
                        successCounts,
                        // join 결과로 필요한 값만 반환하는 임시 객체 사용
                        (failValue, successValue) -> {
                            long total = (failValue != null ? failValue : 0) + (successValue != null ? successValue : 0);
                            double calculatedFailureRate = (double) (failValue != null ? failValue : 0) / total;

                            if (total > 1 && calculatedFailureRate > 0.1) {
                                // DTO에 필요한 데이터를 담을 임시 객체를 생성하여 반환
                                return new Object() {
                                    public final double failureRate = calculatedFailureRate;
                                    public final long failedCount = (failValue != null ? failValue : 0);
                                    public final long totalCount = total;
                                    public final long timeWindowMinutes = 60L;
                                };
                            }
                            return null;
                        }
                )
                .filter((windowedKey, value) -> value != null)
                .toStream()
                .mapValues((windowedKey, value) -> {
                    String projectId = windowedKey.key(); // Windowed<String>에서 키를 가져옴
                    String description = "High failure rate detected for Trade: " + Objects.requireNonNull(value).failureRate;

                    return TradeProjectHighFailureRateEvent.of(
                            description,
                            projectId,
                            value.failureRate,
                            value.failedCount,
                            value.totalCount,
                            value.timeWindowMinutes
                    );
                })
                .selectKey((windowedKey, value) -> windowedKey.key() + "@" + windowedKey.window().start() + "-" + windowedKey.window().end())
                .to("TRADE_HIGH_FAILURE_RATE", Produced.with(Serdes.String(), new JsonSerde<>(TradeProjectHighFailureRateEvent.class)));
    }

    private static void detectBuyerFailureRate(KStream<String, TradeRequestAcceptedEvent> acceptedStream, KStream<String, TradeRequestRejectedEvent> rejectedStream) {
        // projectId를 기준으로 키 재설정
        KStream<String, TradeRequestAcceptedEvent> keyedAccepted = acceptedStream.selectKey((key, value) -> value.getPayload().getBuyerAddress());
        KStream<String, TradeRequestRejectedEvent> keyedRejected = rejectedStream.selectKey((key, value) -> value.getPayload().getBuyerAddress());

        // 1시간 윈도우를 사용하여 성공/실패 건수 집계
        KTable<Windowed<String>, Long> successCounts = keyedAccepted.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();
        KTable<Windowed<String>, Long> failureCounts = keyedRejected.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();

        // 조인을 통해 실패율 계산 및 이상 탐지
        failureCounts.join(
                        successCounts,
                        // join 결과로 필요한 값만 반환하는 임시 객체 사용
                        (failValue, successValue) -> {
                            long total = (failValue != null ? failValue : 0) + (successValue != null ? successValue : 0);
                            double calculatedFailureRate = (double) (failValue != null ? failValue : 0) / total;

                            if (total > 1 && calculatedFailureRate > 0.1) {
                                // DTO에 필요한 데이터를 담을 임시 객체를 생성하여 반환
                                return new Object() {
                                    public final double failureRate = calculatedFailureRate;
                                    public final long failedCount = (failValue != null ? failValue : 0);
                                    public final long totalCount = total;
                                    public final long timeWindowMinutes = 60L;
                                };
                            }
                            return null;
                        }
                )
                .filter((windowedKey, value) -> value != null)
                .toStream()
                .mapValues((windowedKey, value) -> {
                    String buyerAddress = windowedKey.key(); // Windowed<String>에서 키를 가져옴
                    String description = "High failure rate detected for Trade: " + Objects.requireNonNull(value).failureRate;

                    return TradeUserHighFailureRateEvent.ofBuyer(
                            description,
                            buyerAddress,
                            value.failureRate,
                            value.failedCount,
                            value.totalCount,
                            value.timeWindowMinutes
                    );
                })
                .selectKey((windowedKey, value) -> windowedKey.key() + "@" + windowedKey.window().start() + "-" + windowedKey.window().end())
                .to("TRADE_HIGH_FAILURE_RATE", Produced.with(Serdes.String(), new JsonSerde<>(TradeUserHighFailureRateEvent.class)));
    }

    private static void detectSellerFailureRate(KStream<String, TradeRequestAcceptedEvent> acceptedStream, KStream<String, TradeRequestRejectedEvent> rejectedStream) {
        // projectId를 기준으로 키 재설정
        KStream<String, TradeRequestAcceptedEvent> keyedAccepted = acceptedStream.selectKey((key, value) -> value.getPayload().getSellerAddress());
        KStream<String, TradeRequestRejectedEvent> keyedRejected = rejectedStream.selectKey((key, value) -> value.getPayload().getSellerAddress());

        // 1시간 윈도우를 사용하여 성공/실패 건수 집계
        KTable<Windowed<String>, Long> successCounts = keyedAccepted.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();
        KTable<Windowed<String>, Long> failureCounts = keyedRejected.groupByKey().windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1))).count();

        // 조인을 통해 실패율 계산 및 이상 탐지
        failureCounts.join(
                        successCounts,
                        // join 결과로 필요한 값만 반환하는 임시 객체 사용
                        (failValue, successValue) -> {
                            long total = (failValue != null ? failValue : 0) + (successValue != null ? successValue : 0);
                            double calculatedFailureRate = (double) (failValue != null ? failValue : 0) / total;

                            if (total > 1 && calculatedFailureRate > 0.1) {
                                // DTO에 필요한 데이터를 담을 임시 객체를 생성하여 반환
                                return new Object() {
                                    public final double failureRate = calculatedFailureRate;
                                    public final long failedCount = (failValue != null ? failValue : 0);
                                    public final long totalCount = total;
                                    public final long timeWindowMinutes = 60L;
                                };
                            }
                            return null;
                        }
                )
                .filter((windowedKey, value) -> value != null)
                .toStream()
                .mapValues((windowedKey, value) -> {
                    String sellerAddress = windowedKey.key(); // Windowed<String>에서 키를 가져옴
                    String description = "High failure rate detected for Trade: " + Objects.requireNonNull(value).failureRate;

                    return TradeUserHighFailureRateEvent.ofSeller(
                            description,
                            sellerAddress,
                            value.failureRate,
                            value.failedCount,
                            value.totalCount,
                            value.timeWindowMinutes
                    );
                })
                .selectKey((windowedKey, value) -> windowedKey.key() + "@" + windowedKey.window().start() + "-" + windowedKey.window().end())
                .to("TRADE_HIGH_FAILURE_RATE", Produced.with(Serdes.String(), new JsonSerde<>(TradeUserHighFailureRateEvent.class)));
    }
}
