package com.invoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveItemReq {

    private String name;

    private Integer quantity;

    @JsonProperty(value = "unit_id")
    private UUID unitId;
    @JsonProperty(value = "hsn_code")
    private String hsnCode;

    @JsonProperty(value = "is_remember")
    private Boolean isRemember;

    private Double rate;

    @JsonProperty(value = "discount_id")
    private UUID discountId;

    @JsonProperty(value = "tax_id")
    private UUID taxId;
}
