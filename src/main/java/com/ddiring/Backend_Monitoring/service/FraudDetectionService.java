package com.ddiring.Backend_Monitoring.service;

import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestAcceptedEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestRejectedEvent;
import com.ddiring.Backend_Monitoring.service.FraudDetection.TradeFraudDetection;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {
    private final StreamsBuilderFactoryBean streamsBuilder;

    @PostConstruct
    public void initFraudDetection() {
        try {
            StreamsBuilder builder = streamsBuilder.getObject();

            // 1. 모든 이벤트를 TradeEvent 상위 클래스로 역직렬화
            KStream<String, TradeEvent> events = builder.stream("TRADE", Consumed.with(Serdes.String(), new JsonSerde<>(TradeEvent.class)));

            // 2. 이벤트 유형에 따라 스트림 분기 및 명확한 캐스팅
            KStream<String, TradeRequestAcceptedEvent> acceptedEvents = events
                    .filter((key, value) -> value instanceof TradeRequestAcceptedEvent)
                    .mapValues((key, value) -> (TradeRequestAcceptedEvent) value);

            KStream<String, TradeRequestRejectedEvent> rejectedEvents = events
                    .filter((key, value) -> value instanceof TradeRequestRejectedEvent)
                    .mapValues((key, value) -> (TradeRequestRejectedEvent) value);

            TradeFraudDetection.detectFailureRate(acceptedEvents, rejectedEvents);

        } catch (Exception e) {
            throw new RuntimeException("[FraudDetection] 이상 거래 탐지 설정 중 오류 발생");
        }
    }
}
