package com.ddiring.Backend_Monitoring.service;

import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestAcceptedEvent;
import com.ddiring.Backend_Monitoring.event.dto.consumer.trade.TradeRequestRejectedEvent;
import com.ddiring.Backend_Monitoring.service.FraudDetection.TradeFailureDetection;
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

            // 모든 이벤트를 TradeEvent 상위 클래스로 역직렬화
            KStream<String, TradeEvent> tradeEvents = builder.stream("TRADE", Consumed.with(Serdes.String(), new JsonSerde<>(TradeEvent.class)));
            TradeFailureDetection.detectTradeFailure(tradeEvents);

        } catch (Exception e) {
            throw new RuntimeException("[FraudDetection] 이상 거래 탐지 설정 중 오류 발생");
        }
    }
}
