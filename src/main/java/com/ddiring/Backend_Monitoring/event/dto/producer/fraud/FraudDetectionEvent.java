package com.ddiring.Backend_Monitoring.event.dto.producer.fraud;

import java.time.Instant;

public abstract class FraudDetectionEvent {
    private String eventId;
    private String eventType;
    private Instant timestamp;
    private String description;
}
