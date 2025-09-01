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

    // 작성자
    @Column(name = "writer_id")
    private String writerId;

    // 신고자
    @Column(name = "report_id")
    private String reportId;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "content")
    private String content;

    // 사용자 신고 0, 유익하지 않은 컨텐츠 1, 허위 정보 제공 2
    @Column(name = "report_type")
    private Integer reportType;

    @Column(name = "status")
    private Status status;

    public enum Status {
        RECEIPT, PROCESSING, COMPLETE
    }
}
