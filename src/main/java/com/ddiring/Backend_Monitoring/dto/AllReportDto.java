package com.ddiring.Backend_Monitoring.dto;

import com.ddiring.Backend_Monitoring.entity.Monitoring;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllReportDto {

    private Integer reportNo;
    private String writerId;
    private String reportId;
    private String projectId;
    private Integer reportType;
    private Monitoring.Status status;
}
