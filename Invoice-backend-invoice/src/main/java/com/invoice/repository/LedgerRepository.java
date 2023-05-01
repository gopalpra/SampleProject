package com.invoice.repository;

import com.invoice.entity.Ledger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chhavi priya tanwar
 */
@Repository
@EnableJpaRepositories
public interface LedgerRepository extends JpaRepository<Ledger, UUID> {

    public Optional<Ledger> getLedgerById(UUID id);

    List<Ledger> findByCustomerId(UUID customerId);
//    List<Ledger> existsByCustomerId(UUID customerId);

    // this is without customer_id
    Page<Ledger> findAll(Pageable pageable);

    //this is use with customer_id
    public Page<Ledger> findByCustomerId(UUID customerId, Pageable pageable);
    long countByCustomerId(UUID customerId);
    long count();
    public boolean existsByCustomerId(UUID customerId);


}

