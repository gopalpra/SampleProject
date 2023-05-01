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
@Table(name="invoice_payment")
@JsonIgnoreProperties(value={"creation_ts","modified_ts"})
public class InvoicePayment
{
    @Id
    private UUID id;
    @Column(name="customer_id")
    @JsonProperty(value = "customer_id")
    private UUID customerId;
    @Column(name="payment_amount")
    @JsonProperty(value = "payment_amount")
    private Double paymentAmount;
    @Column(name="payment_mode")
    @JsonProperty(value = "payment_mode")
    private String paymentMode;
    @Column(name="payment_date")
    @JsonProperty(value = "payment_date")
    private String paymentDate;
    private String remark;
    private String status;
    @JsonProperty(value = "creation_ts")
    @Column(name="creation_ts")
    private long creationTs;
    @JsonProperty(value = "modified_ts")
    @Column(name = "modified_ts")
    private long modifiedTs;
}
