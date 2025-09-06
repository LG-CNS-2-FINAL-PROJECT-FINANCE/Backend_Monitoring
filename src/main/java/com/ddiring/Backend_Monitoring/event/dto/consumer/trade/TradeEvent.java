package com.ddiring.Backend_Monitoring.event.dto.consumer.trade;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
@NoArgsConstructor(access =  lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class TradeEvent {
    public static final String TOPIC = "TRADE";

    private String eventId;
    private String eventType;
    private Instant timestamp;
}