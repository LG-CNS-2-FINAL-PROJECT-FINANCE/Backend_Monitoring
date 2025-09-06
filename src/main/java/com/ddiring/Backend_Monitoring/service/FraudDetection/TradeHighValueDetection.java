package com.ddiring.Backend_Monitoring.service.FraudDetection;

import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestAcceptedEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestRejectedEvent;
import com.ddiring.Backend_Monitoring.event.dto.producer.fraud.TradeHighValueEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.kafka.support.serializer.JsonSerde;

@Slf4j
public class TradeHighValueDetection {

    public static void detectHighValueTrade(KStream<String, TradeRequestAcceptedEvent> acceptedEvents, KStream<String, TradeRequestRejectedEvent> rejectedEvents) {
        handleAcceptedTrade(acceptedEvents);
        handleRejectedTrade(rejectedEvents);
    }

    private static void handleAcceptedTrade(KStream<String, TradeRequestAcceptedEvent> acceptedEvents) {
        // 고액 거래 탐지 로직 구현 (예: 1회 토큰 거래량이 10,000 이상인 경우)
        acceptedEvents.filter((key, value) -> value.getPayload().getTradeAmount() >= 10000)
                .mapValues((key, value) -> {
                    // 고액 거래 탐지 시 처리 로직 (예: 로그 기록, 알림 발송 등)
                    log.info("[TradeHighValueDetection] High value trade detected: tradeId = {}, buyerAddress = {}, sellerAddress = {}, amount = {}",
                            value.getPayload().getTradeId(), value.getPayload().getBuyerAddress(), value.getPayload().getSellerAddress(), value.getPayload().getTradeAmount());

                    return TradeHighValueEvent.of(
                            value.getPayload().getTradeId(),
                            value.getPayload().getBuyerAddress(),
                            value.getPayload().getSellerAddress(),
                            value.getPayload().getTradeAmount()
                    );
                }).to(TradeHighValueEvent.TOPIC, Produced.with(Serdes.String(), new JsonSerde<>(TradeHighValueEvent.class)));
    }

    private static void handleRejectedTrade(KStream<String, TradeRequestRejectedEvent> rejectedEvents) {
        // 고액 거래 탐지 로직 구현 (예: 1회 토큰 거래량이 10,000 이상인 경우)
        rejectedEvents.filter((key, value) -> value.getPayload().getTradeAmount() >= 10000)
                .mapValues((key, value) -> {
                    // 고액 거래 탐지 시 처리 로직 (예: 로그 기록, 알림 발송 등)
                    log.info("[TradeHighValueDetection] High value trade detected: tradeId = {}, buyerAddress = {}, sellerAddress = {}, amount = {}",
                            value.getPayload().getTradeId(), value.getPayload().getBuyerAddress(), value.getPayload().getSellerAddress(), value.getPayload().getTradeAmount());

                    return TradeHighValueEvent.of(
                            value.getPayload().getTradeId(),
                            value.getPayload().getBuyerAddress(),
                            value.getPayload().getSellerAddress(),
                            value.getPayload().getTradeAmount()
                    );
                }).to(TradeHighValueEvent.TOPIC, Produced.with(Serdes.String(), new JsonSerde<>(TradeHighValueEvent.class)));
    }
}
