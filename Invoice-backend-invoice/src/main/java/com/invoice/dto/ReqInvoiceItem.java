package com.invoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqInvoiceItem
{
    private String name;
    private Integer quantity;
    @Column(name="unit_id")
    @JsonProperty(value = "unit_id")
    private UUID unitId;
    private Double rate;
    @Column(name="discount_id")
    @JsonProperty(value = "discount_id")
    private UUID discountId;
    @Column(name="hsn_code")
    @JsonProperty(value = "hsn_code")
    private String hsnCode;
    @Column(name="tax_id")
    @JsonProperty(value = "tax_id")
    private UUID taxId;
    @NotNull
    @Column(name="is_remember")
    @JsonProperty(value = "is_remember")
    private Boolean isRemember;
    @Column(name="discount_amount")
    @JsonProperty(value = "discount_amount")
    private Double discountAmount;
    @Column(name="tax_amount")
    @JsonProperty(value = "tax_amount")
    private Double taxAmount;
   }
