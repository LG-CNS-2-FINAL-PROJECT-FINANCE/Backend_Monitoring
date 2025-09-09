package com.ddiring.Backend_Monitoring.dto.response;

import com.ddiring.Backend_Monitoring.entity.Monitoring;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateReportDto {
    private Monitoring.Status status;
    private String processContent; // 처리 내용
}

