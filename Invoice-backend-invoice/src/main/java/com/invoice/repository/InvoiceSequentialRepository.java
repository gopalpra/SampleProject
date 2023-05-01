package com.invoice.repository;

import com.invoice.entity.InvoiceSequential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface InvoiceSequentialRepository extends JpaRepository<InvoiceSequential, UUID> {

    public Optional<InvoiceSequential> getSequentialByOrgId(UUID orgId);

    @Modifying
    @Query(value = "UPDATE invoice_sequential set seq_current_no = :seqCurrentNo WHERE id = :id", nativeQuery = true)
    void updateInvoiceSequentialCurrentNumber(@Param("seqCurrentNo") long seqCurrentNo,@Param("id") UUID id );
}
