package com.ddiring.Backend_Monitoring.event.dto.producer.fraud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Objects;
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

    public static TradeProjectHighFailureRateEvent of (String projectId, Double failureRate, Long failedTradeCount, Long totalTradeCount, Long timeWindowMinutes) {
        String eventType = TOPIC + ".BASE_BY_PROJECT";
        String description = String.format("Project ID %s has a high trade failure rate of %.2f%% over the last %d minutes.", projectId, failureRate * 100, timeWindowMinutes);

        return TradeProjectHighFailureRateEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .timestamp(Instant.now())
                .description(description)
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
