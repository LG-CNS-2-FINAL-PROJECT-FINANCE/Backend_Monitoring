package com.ddiring.Backend_Monitoring.api.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "productClient",
        url = "${product.base-url}"
)
public interface ProductClient {

    @GetMapping("/api/product/{projectId}")
    ResponseEntity<ProductDetailDto> getProduct(@PathVariable("projectId") String projectId);
}
