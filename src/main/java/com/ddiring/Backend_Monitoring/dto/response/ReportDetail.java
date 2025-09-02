package com.ddiring.Backend_Monitoring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetail {

    private String reportId;
    private String writerId;
    private String projectId;
    private Integer reportType;
    private String content;
}
