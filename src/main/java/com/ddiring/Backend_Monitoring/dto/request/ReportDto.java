package com.ddiring.Backend_Monitoring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private String reportId;
    private String projectId;
    private Integer reportType;
    private String content;
}
