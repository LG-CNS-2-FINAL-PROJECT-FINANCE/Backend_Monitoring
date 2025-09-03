package com.ddiring.Backend_Monitoring.service;

import com.ddiring.Backend_Monitoring.api.product.ProductClient;
import com.ddiring.Backend_Monitoring.api.product.ProductDetailDto;
import com.ddiring.Backend_Monitoring.dto.request.ReportDto;
import com.ddiring.Backend_Monitoring.dto.response.ReportDetail;
import com.ddiring.Backend_Monitoring.dto.response.ReportListDto;
import com.ddiring.Backend_Monitoring.dto.response.UserReportDto;
import com.ddiring.Backend_Monitoring.entity.Monitoring;
import com.ddiring.Backend_Monitoring.entity.Monitoring.Status;
import com.ddiring.Backend_Monitoring.repository.MonitoringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringService {

        private final MonitoringRepository repository;
        private final ProductClient productClient;

        // 전체 신고 조회
        public List<ReportListDto> getList() {
                List<Monitoring> list = repository.findAll();
                return list.stream()
                                .map(m -> new ReportListDto(
                                                m.getReportNo(),
                                                m.getWriterId(),
                                                m.getReportId(),
                                                m.getProjectId(),
                                                m.getReportType(),
                                                m.getStatus()))
                                .collect(Collectors.toList());
        }

        // 사용자 신고 내역 조회
        public List<UserReportDto> getUserReports(String writerId) {
                List<Monitoring> list = repository.findByWriterId(writerId);
                return list.stream()
                                .map(m -> new UserReportDto(
                                                m.getReportId(),
                                                m.getProjectId(),
                                                m.getStatus()))
                                .collect(Collectors.toList());
        }

        // 신고 상세 조회
        public ReportDetail getDetail(Integer reportNo) {
                Monitoring m = repository.findById(reportNo).orElseThrow();
                return new ReportDetail(
                                m.getReportId(),
                                m.getWriterId(),
                                m.getProjectId(),
                                m.getReportType(),
                                m.getContent());
        }

        // 신고
        public ReportDetail createReport(String writerId, ReportDto dto) {
                ProductDetailDto product = productClient.getProduct(dto.getProjectId()).getBody();
                if (product == null) {
                        throw new IllegalStateException("상품 정보가 없습니다.");
                }

                Monitoring m = new Monitoring();
                m.setWriterId(writerId);
                m.setReportId(product.getUserSeq());
                m.setProjectId(product.getProjectId());
                m.setReportType(dto.getReportType());
                m.setContent(dto.getContent());
                m.setStatus(Status.RECEIPT);

                Monitoring saved = repository.save(m);
                return new ReportDetail(
                                saved.getReportId(),
                                saved.getWriterId(),
                                saved.getProjectId(),
                                saved.getReportType(),
                                saved.getContent());
        }
}
