package com.ddiring.Backend_Monitoring.event.dto.consumer.trade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class TradeRequestAcceptedEvent extends TradeEvent {
    private TradeRequestAcceptedPayload payload;

    @Getter
    @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TradeRequestAcceptedPayload {
        private String projectId;
        private Long tradeId;
        private String buyerAddress;
        private String sellerAddress;
        private Long tradeAmount;
        private String status;
    }}