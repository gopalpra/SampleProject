package com.invoice.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString
@Table(name="invoice_item_master")
@Cacheable(value = false)
@JsonIgnoreProperties(value={"creation_ts","modified_ts"})
public class InvoiceItemMaster
{
    @Id
    private UUID id;
    private String name;
    @Column(name="unit_id")
    @JsonProperty(value = "unit_id")
    private UUID unitId;
    @Column(name="hsn_code")
    @JsonProperty(value = "hsn_code")
    private String hsnCode;
    private Double rate;
    @Column(name="discount_id")
    @JsonProperty(value = "discount_id")
    private UUID discountId;
    @Column(name="tax_id")
    @JsonProperty(value = "tax_id")
    private UUID taxId;
    @JsonProperty(value="creation_ts")
    @Column(name = "creation_ts")
    private long creationTs;
    @JsonProperty(value="modified_ts")
    @Column(name = "modified_ts")
    private long modifiedTs;
}
