package com.masa.sale.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "AUTH-SERVICE",
        path = "/api/v1/profile"
)
public interface ProfileClient {
    @GetMapping()
    String getProfile(@RequestParam Long id);
}
