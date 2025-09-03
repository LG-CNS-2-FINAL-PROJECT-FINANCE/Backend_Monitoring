package com.ddiring.Backend_Monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
@EnableFeignClients
public class BackendMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendMonitoringApplication.class, args);
	}

}
