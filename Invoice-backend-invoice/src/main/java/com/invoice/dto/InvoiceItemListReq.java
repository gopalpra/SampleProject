package com.invoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceItemListReq {

    @JsonProperty(value = "invoice_items")
    public List<SaveItemReq> invoiceItems;
}
