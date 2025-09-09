package com.ddiring.Backend_Monitoring.dto.response;

import com.ddiring.Backend_Monitoring.entity.Monitoring;
import com.ddiring.Backend_Monitoring.entity.Monitoring.Status;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportListDto {

    private Integer reportNo;

    private String projectId;
    private String title;

    private String reportId;
    private String reportNickname;

    private String writerId;
    private String writerNickname;

    private Monitoring.ReportType reportType;

    private Status status;

    public static ReportListDto from(Monitoring m) {
        return ReportListDto.builder()
                .reportNo(m.getReportNo())
                .projectId(m.getProjectId())
                .title(m.getTitle())
                .reportId(m.getReportId())
                .reportNickname(m.getReportNickname())
                .writerId(m.getWriterId())
                .writerNickname(m.getWriterNickname())
                .reportType(m.getReportType())
                .status(m.getStatus())
                .build();
    }
}
