package com.ddiring.Backend_Monitoring.api.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {

    private String projectId;
    private String title;

    private String userSeq;
    private String nickname;
}
