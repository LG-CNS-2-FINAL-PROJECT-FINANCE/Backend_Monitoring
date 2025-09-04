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
public class TradeHighFailureRateEvent extends FraudDetectionEvent {
    public static final String TOPIC = "TRADE_HIGH_FAILURE_RATE";

    private TradeHighFailureRatePayload payload;

    @Getter
    @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TradeHighFailureRatePayload {
        private String projectId;
        private Double failureRate;
        private Long failedTradeCount;
        private Long totalTradeCount;
        private Long timeWindowMinutes;
    }

    public static TradeHighFailureRateEvent of (String descript, String projectId, Double failureRate, Long failedTradeCount, Long totalTradeCount, Long timeWindowMinutes) {
        return TradeHighFailureRateEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(TOPIC)
                .timestamp(Instant.now())
                .description(descript)
                .payload(
                        TradeHighFailureRatePayload.builder()
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
