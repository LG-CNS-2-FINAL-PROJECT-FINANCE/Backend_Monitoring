package com.ddiring.Backend_Monitoring.controller;

import com.ddiring.Backend_Monitoring.common.util.AuthUtils;
import com.ddiring.Backend_Monitoring.common.util.GatewayRequestHeaderUtils;
import com.ddiring.Backend_Monitoring.dto.request.ReportDto;
import com.ddiring.Backend_Monitoring.dto.response.AdminUpdateReportDto;
import com.ddiring.Backend_Monitoring.dto.response.ReportDetail;
import com.ddiring.Backend_Monitoring.dto.response.ReportListDto;
import com.ddiring.Backend_Monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService service;

    // 신고
    @PostMapping("/report")
    public ResponseEntity<ReportDto> createReport(@RequestBody ReportDto dto) {
        String writerId = GatewayRequestHeaderUtils.getUserSeq();
        service.createReport(writerId, dto);
        return ResponseEntity.ok().build();
    }

    //신고 상태 변경
    @PatchMapping("/report//admin{reportNo}/status")
    public ResponseEntity<ReportDetail> updateStatus(@PathVariable Integer reportNo,
                                                     @RequestBody AdminUpdateReportDto dto) {
        String role = GatewayRequestHeaderUtils.getRole();
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ReportDetail updated = service.updateStatus(reportNo, dto);
        return ResponseEntity.ok(updated);
    }

    // 관리자 신고 조회
    @GetMapping("/report/admin/list")
    public ResponseEntity<List<ReportListDto>> getAdminList() {
        AuthUtils.requireAdmin();
        List<ReportListDto> list = service.getAdminList();
        return ResponseEntity.ok(list);
    }

    // 관리자 상세 조회
    @GetMapping("/report/admin/{reportNo}")
    public ResponseEntity<ReportDetail> getAdminDetail(@PathVariable Integer reportNo) {
        AuthUtils.requireAdmin();
        ReportDetail detail = service.getAdminDetail(reportNo);
        return ResponseEntity.ok(detail);
    }

    // 신고자 목록 조회
    @GetMapping("/report/writer/list")
    public ResponseEntity<List<ReportListDto>> getWriterList() {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        AuthUtils.requireUser();
        List<ReportListDto> list = service.getWriterList(userSeq);
        return ResponseEntity.ok(list);
    }

    // 신고자 상세 조회
    @GetMapping("/report/writer/{reportNo}")
    public ResponseEntity<ReportDetail> getWriterDetail(@PathVariable Integer reportNo) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        AuthUtils.requireUser();
        ReportDetail detail = service.getWriterDetail(userSeq, reportNo);
        return ResponseEntity.ok(detail);
    }

    // 피신고자 목록 조회
    @GetMapping("/report/creator/list")
    public ResponseEntity<List<ReportListDto>> getReportList() {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        AuthUtils.requireCreator();
        List<ReportListDto> list = service.getReportList(userSeq);
        return ResponseEntity.ok(list);
    }

    // 피신고자 상세 조회
    @GetMapping("/report/creator/{reportNo}")
    public ResponseEntity<ReportDetail> getReportDetail(@PathVariable Integer reportNo) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        AuthUtils.requireCreator();
        ReportDetail detail = service.getWriterDetail(userSeq, reportNo);
        return ResponseEntity.ok(detail);
    }
}
