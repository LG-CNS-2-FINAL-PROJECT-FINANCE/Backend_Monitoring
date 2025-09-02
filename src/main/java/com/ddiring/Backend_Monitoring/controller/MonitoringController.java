package com.ddiring.Backend_Monitoring.controller;

import com.ddiring.Backend_Monitoring.common.util.GatewayRequestHeaderUtils;
import com.ddiring.Backend_Monitoring.dto.request.ReportDto;
import com.ddiring.Backend_Monitoring.dto.response.ReportDetail;
import com.ddiring.Backend_Monitoring.dto.response.ReportListDto;
import com.ddiring.Backend_Monitoring.dto.response.UserReportDto;
import com.ddiring.Backend_Monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService service;

    // 전체 신고 조회
    @GetMapping("/report/list")
    public ResponseEntity<List<ReportListDto>> getList() {
        List<ReportListDto> list = service.getList();
        return ResponseEntity.ok(list);
    }

    // 신고 내역 조회
    @GetMapping("/report/userlist")
    public ResponseEntity<List<UserReportDto>> getUserReports() {
        String writerId = GatewayRequestHeaderUtils.getUserSeq();
        List<UserReportDto> list = service.getUserReports(writerId);
        return ResponseEntity.ok(list);
    }

    // 신고 상세 조회
    @GetMapping("/report/{reportNo}")
    public ResponseEntity<ReportDetail> getDetail(@PathVariable Integer reportNo) {
        ReportDetail detail = service.getDetail(reportNo);
        return ResponseEntity.ok(detail);
    }

    // 신고
    @PostMapping("/report")
    public ResponseEntity<ReportDetail> createReport(@RequestBody ReportDto dto) {
        String writerId = GatewayRequestHeaderUtils.getUserSeq();
        ReportDetail created = service.createReport(writerId, dto);
        return ResponseEntity.ok(created);
    }
}
