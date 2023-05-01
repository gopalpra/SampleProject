package com.invoice.util;

import com.invoice.dto.ListAPIRes;
import com.invoice.dto.ObjAPIRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@FeignClient(value = "ORGANIZATION-SERVICE", url = "https://inv-do-organization.tecorelabs.com")
//@FeignClient(value = "ORGANIZATION-SERVICE", url = "http://localhost:9092/")
public interface OrganizationServiceAPIClient {


     /*"data": [
    {
        "id": "a0a8188b-205d-4dac-88a6-4fb7334a583f",
            "name": "additional",
            "value": 100.0,
            "org_id": "4e9d6b2c-9694-4709-92d0-c6dc13ae8859",
            "value_type": "Percent",
            "discount_type": "invoice"
    },
    {
        "id": "84cf0f5c-bdb8-43df-84f3-b0d5217cd667",
            "name": "extra",
            "value": 100.0,
            "org_id": "28b819e1-8629-47b9-83e4-88132e2faf35",
            "value_type": "Amount",
            "discount_type": "invoice"
    }
    ]*/

    @GetMapping("/discount/master/list")
    public ListAPIRes getDiscountMaster(@RequestHeader("Authorization") String token,
                                        @RequestHeader("account_id") String accountId,
                                        @RequestHeader("token_type") String tokenType);

    @GetMapping("/discount/master")
    public ObjAPIRes getDiscountMasterBYID(@RequestParam UUID id,
                                           @RequestHeader("Authorization") String token,
                                           @RequestHeader("account_id") String accountId,
                                           @RequestHeader("token_type") String tokenType);


    // https://inv-do-organization.tecorelabs.com/get/settings?org_id=1d4e5afd-20bd-4522-93df-b4154f5d6a44
    // Get Seq Number
    @GetMapping("/get/settings")
    public ObjAPIRes getInvoiceSequentialInfo(@RequestParam UUID org_id,
                                              @RequestHeader("Authorization") String token,
                                              @RequestHeader("account_id") String accountId,
                                              @RequestHeader("token_type") String tokenType);



}
