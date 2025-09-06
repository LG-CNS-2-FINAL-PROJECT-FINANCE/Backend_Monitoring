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
public class TradeUserHighFailureRateEvent extends FraudDetectionEvent {
    public static final String TOPIC = "TRADE_HIGH_FAILURE_RATE";

    private TradeUserHighFailureRatePayload payload;

    @Getter
    @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TradeUserHighFailureRatePayload {
        private String userAddress;
        private Double failureRate;
        private Long failedTradeCount;
        private Long totalTradeCount;
        private Long timeWindowMinutes;
    }

    public static TradeUserHighFailureRateEvent ofSeller (String userAddress, Double failureRate, Long failedTradeCount, Long totalTradeCount, Long timeWindowMinutes) {
        String eventType = TOPIC + ".BASE_BY_SELLER";
        String description = String.format("Seller %s has a high trade failure rate of %.2f%% with %d failed trades out of %d total trades in the last %d minutes.",
                userAddress, failureRate * 100, failedTradeCount, totalTradeCount, timeWindowMinutes);

        return TradeUserHighFailureRateEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .timestamp(Instant.now())
                .description(description)
                .payload(
                        TradeUserHighFailureRatePayload.builder()
                                .userAddress(userAddress)
                                .failureRate(failureRate)
                                .failedTradeCount(failedTradeCount)
                                .totalTradeCount(totalTradeCount)
                                .timeWindowMinutes(timeWindowMinutes)
                                .build()
                )
                .build();
    }

    public static TradeUserHighFailureRateEvent ofBuyer (String userAddress, Double failureRate, Long failedTradeCount, Long totalTradeCount, Long timeWindowMinutes) {
        String eventType = TOPIC + ".BASE_BY_BUYER";
        String description = String.format("Buyer %s has a high trade failure rate of %.2f%% with %d failed trades out of %d total trades in the last %d minutes.",
                userAddress, failureRate * 100, failedTradeCount, totalTradeCount, timeWindowMinutes);

        return TradeUserHighFailureRateEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .timestamp(Instant.now())
                .description(description)
                .payload(
                        TradeUserHighFailureRatePayload.builder()
                                .userAddress(userAddress)
                                .failureRate(failureRate)
                                .failedTradeCount(failedTradeCount)
                                .totalTradeCount(totalTradeCount)
                                .timeWindowMinutes(timeWindowMinutes)
                                .build()
                )
                .build();
    }
}
