package com.ddiring.Backend_Monitoring.service;

import com.ddiring.Backend_Monitoring.dto.AllReportDto;
import com.ddiring.Backend_Monitoring.repository.MonitoringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final MonitoringRepository repository;

    public List<AllReportDto> getList() {
        List<AllReportDto> list =
        return AllReportDto.
    }
}
