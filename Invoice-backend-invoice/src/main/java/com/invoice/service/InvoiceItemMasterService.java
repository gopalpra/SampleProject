package com.invoice.service;

import com.invoice.entity.InvoiceItemMaster;
import com.invoice.repository.CustomInvoiceItemMasterRepository;
import com.invoice.repository.InvoiceItemMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceItemMasterService
{
    @Autowired
    private InvoiceItemMasterRepository invoiceItemMasterRepository;

    @Autowired
    private CustomInvoiceItemMasterRepository customInvoiceItemMasterRepository;

    public List<InvoiceItemMaster> getAllInvoiceItemMaster()
    {
        return invoiceItemMasterRepository.findAll();
    }
    public List<InvoiceItemMaster> getAllInvoiceItemMasterByName(String name)
    {
        return invoiceItemMasterRepository.getInvoiceItemMasterByName(name);
    }

    public Optional<InvoiceItemMaster> getInvoiceItemMasterById(UUID id)
    {
        return invoiceItemMasterRepository.findById(id);
    }
    public InvoiceItemMaster getInvoiceItemMasterByNamed(String name)
    {
        return invoiceItemMasterRepository.getInvoiceItemMasterByNamed(name);
    }

    public InvoiceItemMaster addInvoiceItemMaster(InvoiceItemMaster InvoiceItemMaster)
    {
        return invoiceItemMasterRepository.save(InvoiceItemMaster);
    }

    public InvoiceItemMaster updateInvoiceItemMaster(Map<String,Object> invoiceItemMaster, UUID id)
    {
        return customInvoiceItemMasterRepository.updateInvoiceItemMaster(invoiceItemMaster, id);
    }

    public void deleteInvoiceItemMaster(UUID id)
    {
        invoiceItemMasterRepository.deleteById(id);
    }

//    public void deleteInvoiceItemMasterByName(String name)
//    {
//        invoiceItemMasterRepository.deleteInvoiceItemMasterByNamed(name);
//    }
}
