package com.invoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invoice.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.util.Map;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveInvoiceReq {

    public String id;   // InvoiceId
    public String title;
    public String description;
    @JsonProperty(value = "org_id")
    public String orgId;
    @JsonProperty(value = "branch_id")
    public String branchId;
    @JsonProperty(value = "customer_id")
    public String customerId;
    @JsonProperty(value = "currency_id")
    public String currencyId;
    @JsonProperty(value = "logo_url")
    public String logoUrl;
    public String date;
    @JsonProperty(value = "due_date")
    public String dueDate;
    @JsonProperty(value = "purchase_code")
    public String purchaseCode;
    @JsonProperty(value = "payment_mode")
    public String paymentMode;
    public String remark;
    public String note;

    public String status;

    @JsonProperty(value = "discount_id")
    public String discountId;

    @JsonProperty(value = "tax_id")
    public String taxId;
    @JsonProperty(value = "invoice_items")
    public List<SaveItemReq> invoiceItems;

    @JsonProperty(value = "customer_payload")
    public Map<String, Object> customerPayload;

}
