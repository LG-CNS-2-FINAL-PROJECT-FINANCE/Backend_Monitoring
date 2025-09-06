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
public class TradeUserFrequencyRateEvent extends FraudDetectionEvent {
    public static final String TOPIC = "DETECT_TRADE_FRAUD";

    private TradeUserFrequencyRatePayload payload;

    @Getter
    @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TradeUserFrequencyRatePayload {
        private String userAddress;
        private long totalCount;
        private long totalEventCount;
        private double failureRate;
        private Long timeWindowMinutes;
    }

    public static TradeUserFrequencyRateEvent ofBuyer(String userAddress, long totalCount, long totalEventCount, double failureRate) {
        String eventType = TOPIC + ".HIGH_FREQUENCY_RATE.BASE_BY_BUYER";
        String description = String.format("Buyer %s has a trade frequency rate of %.2f%% with %d trades out of %d total events.",
                userAddress, (double) totalCount / totalEventCount * 100, totalCount, totalEventCount);

        return TradeUserFrequencyRateEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .timestamp(Instant.now())
                .description(description)
                .payload(TradeUserFrequencyRatePayload.builder()
                        .userAddress(userAddress)
                        .totalCount(totalCount)
                        .totalEventCount(totalEventCount)
                        .failureRate(failureRate)
                        .build())
                .build();
    }

    public static TradeUserFrequencyRateEvent ofSeller(String userAddress, Long totalCount, Long totalEventCount, Double failureRate, Long timeWindowMinutes) {
        String eventType = TOPIC + ".HIGH_FREQUENCY_RATE.BASE_BY_SELLER";
        String description = String.format("Seller %s has a trade frequency rate of %.2f%% with %d trades out of %d total events.",
                userAddress, (double) totalCount / totalEventCount * 100, totalCount, totalEventCount);

        return TradeUserFrequencyRateEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .timestamp(Instant.now())
                .description(description)
                .payload(TradeUserFrequencyRatePayload.builder()
                        .userAddress(userAddress)
                        .totalCount(totalCount)
                        .totalEventCount(totalEventCount)
                        .failureRate(failureRate)
                        .timeWindowMinutes(timeWindowMinutes)
                        .build())
                .build();
    }
}
