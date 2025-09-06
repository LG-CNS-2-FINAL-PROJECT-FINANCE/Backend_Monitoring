package com.ddiring.Backend_Monitoring.event.dto.producer.fraud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TradeHighValueEvent extends FraudDetectionEvent{
    public static final String TOPIC = "DETECT_TRADE_FRAUD";

    private TradeHighValuePayload payload;

    @Getter
    @SuperBuilder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TradeHighValuePayload {
        private Long tradeId;
        private String buyerAddress;
        private String sellerAddress;
        private Long tradeAmount;
    }

    public static TradeHighValueEvent of(Long tradeId, String buyerAddress, String sellerAddress, Long tradeAmount) {
        String eventType = TOPIC + ".HIGH_VALUE";

        String description = String.format("High value trade detected: tradeId=%s, buyerAddress=%s, sellerAddress=%s, amount=%d",
                tradeId, buyerAddress, sellerAddress, tradeAmount);

        return TradeHighValueEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .timestamp(Instant.now())
                .description(description)
                .payload(TradeHighValuePayload.builder()
                        .tradeId(tradeId)
                        .buyerAddress(buyerAddress)
                        .sellerAddress(sellerAddress)
                        .tradeAmount(tradeAmount)
                        .build())
                .build();
    }
}
