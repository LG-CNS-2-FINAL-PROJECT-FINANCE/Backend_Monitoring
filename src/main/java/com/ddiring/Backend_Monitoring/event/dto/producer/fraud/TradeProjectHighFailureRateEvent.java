package com.ddiring.Backend_Monitoring.event.dto.producer.fraud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class TradeProjectHighFailureRateEvent extends FraudDetectionEvent {
    public static final String TOPIC = "TRADE_HIGH_FAILURE_RATE";

    private TradeProjectHighFailureRatePayload payload;

    @Getter
    @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TradeProjectHighFailureRatePayload {
        private String projectId;
        private Double failureRate;
        private Long failedTradeCount;
        private Long totalTradeCount;
        private Long timeWindowMinutes;
    }

    public static TradeProjectHighFailureRateEvent of (String descript, String projectId, Double failureRate, Long failedTradeCount, Long totalTradeCount, Long timeWindowMinutes) {
        String eventType = TOPIC + ".BASE_BY_PROJECT";

        return TradeProjectHighFailureRateEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .timestamp(Instant.now())
                .description(descript)
                .payload(
                        TradeProjectHighFailureRatePayload.builder()
                                .projectId(projectId)
                                .failureRate(failureRate)
                                .failedTradeCount(failedTradeCount)
                                .totalTradeCount(totalTradeCount)
                                .timeWindowMinutes(timeWindowMinutes)
                                .build()
                )
                .build();
    }
}
