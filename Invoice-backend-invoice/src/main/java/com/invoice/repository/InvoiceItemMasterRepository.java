package com.invoice.repository;

import com.invoice.entity.InvoiceItemMaster;
import graphql.com.google.common.base.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceItemMasterRepository extends JpaRepository<InvoiceItemMaster, UUID> {
    @Query(value = "SELECT * FROM invoice_item_master WHERE Lower(name) LIKE %:KEYWORD%" ,nativeQuery = true)
    public List<InvoiceItemMaster> getInvoiceItemMasterByName(@Param("KEYWORD") String keyword);

    @Query(value = "SELECT * FROM invoice_item_master WHERE Lower(name) = :name",nativeQuery = true)
    public InvoiceItemMaster getInvoiceItemMasterByNamed(@Param("name") String name);
//    @Query(value = "DELETE FROM invoice_item_master WHERE Lower(name) = :name",nativeQuery = true)
//    public void deleteInvoiceItemMasterByNamed(@Param("name") String name);
}
