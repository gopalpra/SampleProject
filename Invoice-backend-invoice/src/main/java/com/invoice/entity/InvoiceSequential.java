package com.invoice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="invoice_sequential")
@JsonIgnoreProperties(value={"creation_ts","modified_ts"})
public class InvoiceSequential
{
    @Id
    private UUID id;
    @JsonProperty(value="org_id")
    @Column(name = "org_id")
    private UUID orgId;
    @JsonProperty(value="branch_id")
    @Column(name = "branch_id")
    private UUID branchId;
    @JsonProperty(value="seq_start_no")
    @Column(name = "seq_start_no")
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long seqStartNo;
    @JsonProperty(value="seq_current_no")
    @Column(name = "seq_current_no")
    private Long seqCurrentNo;
    @JsonProperty(value="seq_text")
    @Column(name = "seq_text")
    private String seqText;
    @JsonProperty(value="seq_year")
    @Column(name = "seq_year")
    private String seqYear;
    @JsonProperty(value="invoice_no_format")
    @Column(name = "invoice_no_format")
    private String invoiceNoFormat;
    @Column(name="is_deleted")
    @JsonProperty(value = "is_deleted")
    private boolean isDeleted;
    @Column(name="creation_ts")
    @JsonProperty(value = "creation_ts")
    private long creationTs;
    @Column(name="modified_ts")
    @JsonProperty(value = "modified_ts")
    private long modifiedTs;
}