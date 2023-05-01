package com.invoice.service;

import com.invoice.entity.Ledger;
import com.invoice.repository.CustomLedgerRepository;
import com.invoice.repository.LedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chhavi priya tanwar
 */
@Service
public class LedgerService {

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private CustomLedgerRepository customLedgerRepository;

    //getAll
    public List<Ledger> getAllLedger(){
        return ledgerRepository.findAll();
    }

    //getById
    public Optional<Ledger> getLedgerById(UUID id){
        return ledgerRepository.findById(id);
    }

    //find by customer_id
    public List<Ledger> getLedgersByCustomerId(UUID customerId) {
        return ledgerRepository.findByCustomerId(customerId);
    }

    //---------------pagination by without customer_id---------------------
    public Page<Ledger> getLedgers(Pageable pageable) {
        return ledgerRepository.findAll(pageable);
    }
    public long getTotalLedgerCount() {
        return ledgerRepository.count();
    }

    //-------------Pagination by customer id ----------------------
    public Page<Ledger> getLedgersByCustomerId(UUID customerId, Pageable pageable) {
        return ledgerRepository.findByCustomerId(customerId, pageable);
    }

    public long getTotalLedgerCountByCustomerId(UUID customerId) {
        return ledgerRepository.countByCustomerId(customerId);
    }
    public boolean isValidCustomerId(UUID customerIdString) {
        return ledgerRepository.existsByCustomerId(customerIdString);
    }
    //----------------------------------------------------------------------------------


    //createLedgerRecord
    public Ledger createLedger(Ledger ledger){
        return ledgerRepository.save(ledger);
    }

    //updateLedgerRecord
    public Ledger updateLedger(Map<String, Object> data, UUID id) {
        System.out.println("inside service : ");
        return customLedgerRepository.updateLedger(data,id);
    }

    //find latest ledger
    public Ledger getLatestLedger() {
        // Get the latest ledger record based on the creation timestamp
        List<Ledger> ledgerList = ledgerRepository.findAll(Sort.by(Sort.Direction.DESC, "creationTs"));
        if (ledgerList.isEmpty()) {
            return null;
        } else {
            return ledgerList.get(0);
        }
    }

    //deleteLedgerRecord
    public void deleteLedger(UUID id){
        ledgerRepository.deleteById(id);
    }
}
