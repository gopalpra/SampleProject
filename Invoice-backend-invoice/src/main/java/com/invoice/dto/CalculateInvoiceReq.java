package com.invoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalculateInvoiceReq {

    @JsonProperty(value = "discount_id")
    public String discountId;

    @JsonProperty(value = "tax_id")
    public String taxId;

    @JsonProperty(value = "invoice_items")
    public List<SaveItemReq> invoiceItems;


}
