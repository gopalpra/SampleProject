package com.invoice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="invoice_items")
@JsonIgnoreProperties(value={"creation_ts","modified_ts"})
public class InvoiceItem
{
    @Id
    private UUID id;
    @Column(name="invoice_id")
    @JsonProperty(value = "invoice_id")
    private UUID invoiceId;
    private String name;
    private int quantity;
    @Column(name="unit_id")
    @JsonProperty(value = "unit_id")
    private UUID unitId;
    private Double rate;
    @Column(name="discount_id")
    @JsonProperty(value = "discount_id")
    private UUID discountId;
    @Column(name="tax_id")
    @JsonProperty(value = "tax_id")
    private UUID taxId;
    @Column(name="discount_amount")
    @JsonProperty(value = "discount_amount")
    private Double discountAmount;
    @Column(name="tax_amount")
    @JsonProperty(value = "tax_amount")
    private Double taxAmount;
    @Column(name="hsn_code")
    @JsonProperty(value = "hsn_code")
    private String hsnCode;
    @Column(name="is_deleted")
    @JsonProperty(value = "is_deleted")
    private boolean isDeleted;
    @Column(name="is_remember")
    @JsonProperty(value = "is_remember")
    private Boolean isRemember;
    @Column(name="creation_ts")
    @JsonProperty(value = "creation_ts")
    private long creationTs;
    @Column(name="modified_ts")
    @JsonProperty(value = "modified_ts")
    private long modifiedTs;
}
