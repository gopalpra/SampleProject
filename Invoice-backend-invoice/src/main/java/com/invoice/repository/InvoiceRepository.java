package com.invoice.repository;

import com.invoice.entity.Invoice;
import com.invoice.entity.InvoicePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID>, PagingAndSortingRepository<Invoice, UUID>
{

     @Query(value = "SELECT i FROM com.invoice.entity.Invoice i where customer_id = :id")
     Page<Invoice> findByCustomerId(@Param("id") UUID id, Pageable pageable);

     @Query(value = "SELECT i FROM com.invoice.entity.Invoice i where customer_id = :id and status = :status")
     Page<Invoice> findByCustomerIdAndStatus(@Param("id") UUID id,@Param("status") String status, Pageable pageable);
     @Query(value = "SELECT i FROM com.invoice.entity.Invoice i where org_id = :id")
     Page<Invoice> findByOrganizationId(@Param("id") UUID id,Pageable pageable);

     @Query(value = "SELECT i FROM com.invoice.entity.Invoice i where org_id = :id and status = :status")
     Page<Invoice> findByOrganizationIdAndStatus(@Param("id") UUID id,@Param("status") String status,Pageable pageable);


     /*@Query(value = "SELECT i FROM com.invoice.entity.Invoice i")
    Page<Invoice> findAll(Pageable pageable);*/


     @Modifying
     @Query(value = "UPDATE invoice SET status = :status  WHERE id = :id",nativeQuery = true)
     public void updateStatus(@Param("status") String status,@Param("id")UUID id);
}
