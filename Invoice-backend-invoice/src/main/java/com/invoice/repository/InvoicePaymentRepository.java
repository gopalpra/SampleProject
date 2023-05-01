package com.invoice.repository;

import com.invoice.entity.InvoicePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoicePaymentRepository extends JpaRepository<InvoicePayment, UUID>
{
    @Query("select c from InvoicePayment c where c.customerId=:customerId")
    public List<InvoicePayment> findByCustomerId(@Param("customerId") UUID customerId);

}
