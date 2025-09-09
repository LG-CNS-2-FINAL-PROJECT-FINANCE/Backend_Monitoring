package com.ddiring.Backend_Monitoring.service;

import com.ddiring.Backend_Monitoring.api.product.ProductClient;
import com.ddiring.Backend_Monitoring.api.product.ProductDetailDto;
import com.ddiring.Backend_Monitoring.dto.request.ReportDto;
import com.ddiring.Backend_Monitoring.dto.response.AdminUpdateReportDto;
import com.ddiring.Backend_Monitoring.dto.response.ReportDetail;
import com.ddiring.Backend_Monitoring.dto.response.ReportListDto;
import com.ddiring.Backend_Monitoring.entity.Monitoring;
import com.ddiring.Backend_Monitoring.entity.Monitoring.Status;
import com.ddiring.Backend_Monitoring.repository.MonitoringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringService {

        private final MonitoringRepository repository;
        private final ProductClient productClient;

        // 신고
        public void createReport(String writerId, ReportDto dto) {
                ProductDetailDto product = productClient.getProduct(dto.getProjectId()).getBody();
                if (product == null) {
                        throw new IllegalStateException("상품 정보가 없습니다.");
                }

                Monitoring m = new Monitoring();
                m.setProjectId(product.getProjectId());
                m.setTitle(product.getTitle());
                m.setReportId(product.getUserSeq());
                m.setReportNickname(product.getNickname());
                m.setWriterId(writerId);
                m.setWriterNickname(dto.getWriterNickname());
                m.setReportType(dto.getReportType());
                m.setContent(dto.getContent());
                m.setStatus(Status.SUBMITTED);
                repository.save(m);
        }

        // 상태변경
        @Transactional
        public ReportDetail updateStatus(Integer reportNo, AdminUpdateReportDto dto) {
                Monitoring monitoring = repository.findById(reportNo)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 신고입니다."));

                monitoring.setStatus(dto.getStatus());
                monitoring.setProcessContent(dto.getProcessContent());

                Monitoring saved = repository.save(monitoring);
                return ReportDetail.from(saved);
        }

        // 관리자 전체 신고 조회
        public List<ReportListDto> getAdminList() {
                return repository.findAll().
                        stream()
                        .map(ReportListDto::from)
                        .toList();
        }
        // 관리자 신고 상세 조회
        public ReportDetail getAdminDetail(Integer reportNo) {
                Monitoring monitoring = repository.findById(reportNo)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 신고입니다."));
                return ReportDetail.from(monitoring);
        }

        // 신고자 전체 신고 조회
        public List<ReportListDto> getWriterList(String userSeq) {
                return repository.findByWriterId(userSeq)
                        .stream()
                        .map(ReportListDto::from)
                        .toList();
        }
        // 신고자 신고 상세 조회
        public ReportDetail getWriterDetail(String userSeq, Integer reportNo) {
                Monitoring monitoring = repository.findById(reportNo)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 신고입니다."));
                if (!monitoring.getWriterId().equals(userSeq)) {
                        throw new RuntimeException("해당 유저의 신고 내역이 아닙니다.");
                }
                return ReportDetail.from(monitoring);
        }

        // 피신고자 전체 신고 조회
        public List<ReportListDto> getReportList(String userSeq) {
                return repository.findByReportId(userSeq)
                        .stream()
                        .map(ReportListDto::from)
                        .toList();
        }
}
