package com.ddiring.Backend_Monitoring.controller;

import com.ddiring.Backend_Monitoring.dto.AllReportDto;
import com.ddiring.Backend_Monitoring.service.MonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private final MonitoringService service;

    @GetMapping("/report")
    public ResponseEntity<?> getList() {
        service.getList();
        return ResponseEntity.ok()
    }
}
