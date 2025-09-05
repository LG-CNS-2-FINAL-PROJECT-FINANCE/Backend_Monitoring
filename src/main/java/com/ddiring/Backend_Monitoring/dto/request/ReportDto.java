package com.ddiring.Backend_Monitoring.dto.request;

import com.ddiring.Backend_Monitoring.entity.Monitoring;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private String projectId;
    private String title;

    private String reportId;
    private String reportNickname;

    private String writerId;
    private String writerNickname;

    private Monitoring.ReportType reportType;
    private String content;
}
