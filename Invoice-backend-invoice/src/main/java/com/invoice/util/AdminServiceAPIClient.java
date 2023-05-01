package com.invoice.util;

import com.invoice.dto.ListAPIRes;
import com.invoice.dto.ObjAPIRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@FeignClient(value = "ADMIN-SERVICE", url = "https://inv-do-admin.tecorelabs.com")
//@FeignClient(value = "ADMIN-SERVICE", url = "http://localhost:9091/")
public interface AdminServiceAPIClient {

/*
    {
        "id": "3fb6c7c2-b66c-42f4-80c0-c72f653e7f00",
            "name": "AUS",
            "value": 100.0,
            "rate": {
        "isgt": "20%"
    },
        "country_code": "61"
    },
    {
        "id": "dcdbe892-c399-49ec-baf2-08bdcae033da",
            "name": "InterS",
            "value": 100.0,
            "rate": {
        "igst": "100%"
    },
        "country_code": "91"
    },
    {
        "id": "47ef6ef7-90af-42c9-a93f-382ca8b02bde",
            "name": "Brazil",
            "value": 10.0,
            "rate": {
        "isgt": "110%"
    },
        "country_code": "55"
    },*/

    @GetMapping("/invoice/tax/list")
    ListAPIRes getTaxDetails(@RequestHeader("Authorization") String token,
                             @RequestHeader("account_id") String accountId,
                             @RequestHeader("token_type") String tokenType);


    @GetMapping("/invoice/tax")
    public ObjAPIRes getTaxById(@RequestParam UUID id,
                                @RequestHeader("Authorization") String token,
                                @RequestHeader("account_id") String accountId,
                                @RequestHeader("token_type") String tokenType);
}
