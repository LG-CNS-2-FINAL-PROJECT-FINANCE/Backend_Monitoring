package com.ddiring.Backend_Monitoring.event.dto.trade;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "eventType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TradeRequestAcceptedEvent.class, name = "TRADE.REQUEST.ACCEPTED"),
        @JsonSubTypes.Type(value = TradeRequestRejectedEvent.class, name = "TRADE.REQUEST.REJECTED"),
        @JsonSubTypes.Type(value = TradeSucceededEvent.class, name = "TRADE.SUCCEEDED"),
        @JsonSubTypes.Type(value = TradeFailedEvent.class, name = "TRADE.FAILED")
})
@Getter
public abstract class TradeEvent {
    public static final String TOPIC = "TRADE";

    private String eventId;
    private String eventType;
    private Instant timestamp;
}