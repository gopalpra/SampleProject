package com.invoice.service;

import com.invoice.entity.InvoicePayment;
import com.invoice.repository.CustomInvoicePaymentRepository;
import com.invoice.repository.InvoicePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoicePaymentService {
    @Autowired
    private InvoicePaymentRepository invoicePaymentRepository;

    @Autowired
    private CustomInvoicePaymentRepository customInvoicePaymentRepository;

    public List<InvoicePayment> getAllInvoicePaymentByCustomerId(UUID customerId)

    {
        return invoicePaymentRepository.findByCustomerId(customerId);
    }

    public Optional<InvoicePayment> getInvoicePaymentById(UUID id)
    {
        return invoicePaymentRepository.findById(id);
    }

    public InvoicePayment save(InvoicePayment invoicePayment)
    {
        return invoicePaymentRepository.save(invoicePayment);
    }

    public InvoicePayment updateInvoicePayment(Map<String,Object> invoicePayment, UUID id)
    {
        return customInvoicePaymentRepository.updateInvoicePayment(invoicePayment,id);
    }



    public void deleteInvoicePayment(UUID id)
    {
        invoicePaymentRepository.deleteById(id);
    }
}