package com.ddiring.Backend_Monitoring.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {
    private final StreamsBuilderFactoryBean streamsBuilder;

    public void initFraudDetection() {
    }
}
