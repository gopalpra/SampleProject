package com.invoice.repository;

import com.invoice.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, UUID>
{
    @Query("select c from InvoiceItem c where c.invoiceId=:invoiceId")
    public List<InvoiceItem> findByInvoiceId(@Param("invoiceId") UUID invoiceId);

    @Modifying
    @Transactional
    @Query("Delete from InvoiceItem i where i.invoiceId = ?1")
    int deleteByInvoiceId(UUID invoiceId);

}
