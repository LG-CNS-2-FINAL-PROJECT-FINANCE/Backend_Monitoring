package com.ddiring.Backend_Monitoring.event.dto.consumer.trade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class TradeFailedEvent extends TradeEvent {

    private TradeFailedPayload payload;

    @Getter
    @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TradeFailedPayload {
        private String projectId;
        private Long tradeId;
        private String buyerAddress;
        private String sellerAddress;
        private Long tradeAmount;
        private String status;
        private String errorType;
        private String errorMessage;
    }
}
