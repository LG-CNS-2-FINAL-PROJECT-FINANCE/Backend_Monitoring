package com.ddiring.Backend_Monitoring.dto.response;

import com.ddiring.Backend_Monitoring.entity.Monitoring.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportListDto {

    private Integer reportNo;
    private String writerId;
    private String reportId;
    private String projectId;
    private Integer reportType;
    private Status status;
}
