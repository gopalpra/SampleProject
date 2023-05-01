package com.invoice.service;

import com.invoice.entity.InvoiceItem;
import com.invoice.repository.CustomInvoiceItemRepository;
import com.invoice.repository.InvoiceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceItemService
{
    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private CustomInvoiceItemRepository customInvoiceItemRepository;

    public List<InvoiceItem> getAllInvoiceItem()
    {
        return invoiceItemRepository.findAll();
    }

    public Optional<InvoiceItem> getInvoiceItemById(UUID id)
    {
        return invoiceItemRepository.findById(id);
    }

    public List<InvoiceItem> getInvoiceItemByInvoiceId(UUID id)
    {
        return invoiceItemRepository.findByInvoiceId(id);
    }

    public InvoiceItem addInvoiceItem(InvoiceItem invoiceItem)
    {
        return invoiceItemRepository.save(invoiceItem);
    }


    public List<InvoiceItem> addInvoiceItems(List<InvoiceItem> invoiceItems)
    {
        return invoiceItemRepository.saveAll(invoiceItems);
    }


    public InvoiceItem updateInvoiceItem(Map<String,Object> invoiceItem, UUID id)
    {
        return customInvoiceItemRepository.updateInvoiceItem(invoiceItem,id);
    }


    public int deleteByInvoiceId(UUID invoiceId){
        return invoiceItemRepository.deleteByInvoiceId(invoiceId);
    }
//
//    public void deleteInvoiceItemByInvoiceId(UUID id)
//    {
//        invoiceItemRepository.deleteByInvoiceId(id);
//    }

}
