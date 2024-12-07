package com.groupeisi.ms2.feign;

import com.groupeisi.ms2.domain.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms1")
public interface CustomerRestClient {
    @GetMapping("/api/v1/customers/{id}")
    Customer getCustomerById(@PathVariable Long id);
}
