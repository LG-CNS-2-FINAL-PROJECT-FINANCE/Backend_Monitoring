package com.ddiring.Backend_Monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Slf4j
@Entity
@Table(name = "monitoring")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Monitoring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_no")
    private Integer reportNo;

    // 프로젝트 아이디 + 제목
    @Column(name = "project_id")
    private String projectId;
    @Column(name = "title")
    private String title;

    // 피신고자 아이디 + 닉네임
    @Column(name = "report_id")
    private String reportId;
    @Column(name = "report_nickname")
    private String reportNickname;

    // 신고자 아이디 + 닉네임
    @Column(name = "writer_id")
    private String writerId;
    @Column(name = "writer_nickname")
    private String writerNickname;

    // 신고 유형 + 신고 내용
    @Column(name = "report_type")
    private ReportType reportType;
    @Column(name = "content")
    private String content;

    // 신고 현황
    @Column(name = "status")
    private Status status;
    // 처리 내용 (관리자가 작성)
    @Column(name = "process_content")
    private String processContent;

    public enum ReportType {
        USER,    // 사용자 신고
        CONTENT, // 유익하지 않은 콘텐츠
        FAKE     // 허위 정보 제공
    }

    public enum Status {
        SUBMITTED,  // 신고됨
        PROCESSING, // 처리중
        COMPLETE    // 처리완료
    }
}
