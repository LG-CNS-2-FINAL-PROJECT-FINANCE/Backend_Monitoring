package com.ddiring.Backend_Monitoring.dto.response;

import com.ddiring.Backend_Monitoring.entity.Monitoring;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDetail {

    private Integer reportNo;

    private String projectId;
    private String title;

    private String reportId;
    private String reportNickname;

    private String writerId;
    private String writerNickname;

    private Monitoring.ReportType reportType;
    private String content;

    private Monitoring.Status status;
    private String processContent;

    public static ReportDetail from(Monitoring m) {
        return ReportDetail.builder()
                .reportNo(m.getReportNo())
                .projectId(m.getProjectId())
                .title(m.getTitle())
                .reportId(m.getReportId())
                .reportNickname(m.getReportNickname())
                .writerId(m.getWriterId())
                .writerNickname(m.getWriterNickname())
                .reportType(m.getReportType())
                .content(m.getContent())
                .status(m.getStatus())
                .processContent(m.getProcessContent())
                .build();
    }
}
