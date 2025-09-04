package com.ddiring.Backend_Monitoring.event.dto.producer.fraud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class TradeHighFailureRateEvent extends FraudDetectionEvent {
    public static final String TOPIC = "TRADE_HIGH_FAILURE_RATE";

    @Builder.Default
    private String eventType = TOPIC;

    @Getter
    @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TradeHighFailureRatePayload {
        private Long tradeId;
        private String buyerAddress;
        private String sellerAddress;
        private Long tradeAmount;
        private Double failureRate;
        private Long failedTradeCount;
        private Long totalTradeCount;
        private Long timeWindowMinutes;
    }
}
