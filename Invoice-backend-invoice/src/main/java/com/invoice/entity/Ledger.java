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

/**
 * @author chhavi priya tanwar
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="ledger")
@JsonIgnoreProperties(value={"creation_ts","modified_ts"})
public class Ledger {
    @Id
    private UUID id;

    @JsonProperty(value="org_id")
    @Column(name = "org_id")
    private UUID orgId;

    @JsonProperty(value="invoice_id")
    @Column(name = "invoice_id")
    private UUID invoiceId;

    @JsonProperty(value="customer_id")
    @Column(name = "customer_id")
    private UUID customerId;

    @JsonProperty(value="branch_id")
    @Column(name = "branch_id")
    private UUID branchId;

    private Double amount;

    @JsonProperty(value="balance_type")
    @Column(name = "balance_type")
    private String balanceType;

    private String type;

    private Double balance;

    @JsonProperty(value="creation_ts")
    @Column(name = "creation_ts")
    private long creationTs;

    @JsonProperty(value="modified_ts")
    @Column(name = "modified_ts")
    private long modifiedTs;

}
