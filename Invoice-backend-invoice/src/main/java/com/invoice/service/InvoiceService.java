package com.invoice.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invoice.InvoiceApplication;
import com.invoice.constant.Constants;
import com.invoice.dto.ObjAPIRes;
import com.invoice.dto.SaveItemReq;
import com.invoice.entity.Invoice;
import com.invoice.entity.InvoiceItem;
import com.invoice.entity.InvoiceItemMaster;
import com.invoice.entity.InvoicePayment;
import com.invoice.repository.CustomInvoiceRepository;
import com.invoice.repository.InvoiceRepository;
import com.invoice.dto.SaveInvoiceReq;
import com.invoice.util.AdminServiceAPIClient;
import com.invoice.util.OrganizationServiceAPIClient;
import com.invoice.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import javax.persistence.Column;
import javax.persistence.Id;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceItemService invoiceItemService;

    @Autowired
    private InvoicePaymentService invoicePaymentService;
    @Autowired
    private InvoiceItemMasterService invoiceItemMasterService;
    @Autowired
    private CustomInvoiceRepository customInvoiceRepository;

    @Autowired
    AdminServiceAPIClient adminServiceAPIClient;

    @Autowired
    OrganizationServiceAPIClient organizationServiceAPIClient;

    @Autowired
    InvoiceSequentialService invoiceSequentialService;

    public Page<Invoice> getAllInvoices(int page, int pageSize){
        Pageable pageable =
                PageRequest.of(page, pageSize);
        return invoiceRepository.findAll(pageable);
    }

    public Page<Invoice> getAllInvoicesByCustomerId(UUID customerId,int page, int pageSize){
        Pageable pageable =
                PageRequest.of(page, pageSize);
        return invoiceRepository.findByCustomerId(customerId,pageable);
    }
    public Page<Invoice> getAllInvoicesByCustomerIdAndStatus(UUID customerId,String status,int page, int pageSize){
        Pageable pageable =
                PageRequest.of(page, pageSize);
        return invoiceRepository.findByCustomerIdAndStatus(customerId,status,pageable);
    }
    public Page<Invoice> getAllInvoicesByOrganizationId(UUID orgId,int page, int pageSize){
        Pageable pageable =
                PageRequest.of(page, pageSize);
        return invoiceRepository.findByOrganizationId(orgId,pageable);
    }
    public Page<Invoice> getAllInvoicesByOrganizationIdAndStatus(UUID orgId,String status,int page, int pageSize){
        Pageable pageable =
                PageRequest.of(page, pageSize);
        return invoiceRepository.findByOrganizationIdAndStatus(orgId,status,pageable);
    }

    public Iterable<Invoice> getAllInvoices(){

        return invoiceRepository.findAll();
    }


    public String generateSequentialNumber(String orgId) {

        ObjAPIRes response = organizationServiceAPIClient.getInvoiceSequentialInfo(
                UUID.fromString(orgId)
                , "s", "s", "s");

        System.out.println("response ~~~~: "+response);

        Map<String, Object> data = response.getData();
        System.out.println("data ~~~~: "+data);
        Long seqStartNo = Long.valueOf(data.get("seq_start_no").toString());
        Long seqCurrenttNo = Long.valueOf(data.get("seq_current_no").toString());
        String id = data.get("id").toString();
        String seqText = data.get("seq_text").toString();
        String invoiceNoFormat = data.get("invoice_no_format").toString();

        Long nextNumber = seqCurrenttNo + 1;
        String invoiceNo = invoiceNoFormat;

        String date = Utility.getDateByEpoch(System.currentTimeMillis());
        String currentYear = date.split("/")[2];

        invoiceNo = invoiceNo.replace("ST", seqText)
                .replace("SQ", nextNumber.toString())
                .replace("FY", currentYear);


        invoiceSequentialService.updateInvoiceSequentialCurrentNo(nextNumber, UUID.fromString(id));

        return invoiceNo;

    }


      public Map<String, Object> calculateInvoice(List<SaveItemReq> invoiceItems,
                                                UUID invoiceTaxId, UUID invoiceDiscountId) {

        List<InvoiceItem> invoiceItemList =
                createInvoiceItems(invoiceItems);

        double totalDiscount = 0;
        double totalTax = 0;
        double subTotalPrice = 0;
        double totalPrice = 0;

        for (InvoiceItem item : invoiceItemList) {

            totalTax = totalTax + ((item.getTaxAmount() != null) ? item.getTaxAmount() : 0);
            totalDiscount = totalDiscount + ((item.getDiscountAmount() != null) ? item.getDiscountAmount() : 0);
            subTotalPrice = subTotalPrice +  (item.getRate() * item.getQuantity());

        }

        double invoiceDiscountAmount = 0.0;
        double invoiceTaxAmount = 0.0;

        Map<String, Object> invoiceCalculationSummary = new HashMap<>();

        if (invoiceTaxId != null) {

            ObjAPIRes objAPIRes =
                    adminServiceAPIClient.getTaxById(invoiceTaxId, "s", "s", "s");

            invoiceTaxAmount = (double) (subTotalPrice / 100) * Double.valueOf(objAPIRes.getData().get("value").toString());
            invoiceTaxAmount = Double.valueOf(Utility.formatToTwoDecimalPlaces(invoiceTaxAmount));

        }


        if (invoiceDiscountId != null) {

            ObjAPIRes objAPIRes =
                    organizationServiceAPIClient.getDiscountMasterBYID(invoiceDiscountId, "s", "s",
                            "s");


            invoiceCalculationSummary.put("discount_value_type", objAPIRes.getData().get("value_type").toString());
            invoiceCalculationSummary.put("discount_name", objAPIRes.getData().get("name").toString());

            if (objAPIRes.getData().get("value_type").toString().equalsIgnoreCase("amount")) {

                invoiceDiscountAmount = Double.valueOf(objAPIRes.getData().get("value").toString());


            } else {



                invoiceDiscountAmount = (double) ((subTotalPrice - totalDiscount) / 100)
                        * Double.valueOf(objAPIRes.getData().get("value").toString());

            }

            invoiceDiscountAmount = Double.valueOf(Utility.formatToTwoDecimalPlaces(invoiceDiscountAmount));

        }

        invoiceCalculationSummary.put("items", invoiceItemList);

        totalDiscount = totalDiscount + invoiceDiscountAmount;
        totalTax = totalTax + invoiceTaxAmount;
        totalPrice = subTotalPrice - totalDiscount + totalTax;

        invoiceCalculationSummary.put("sub_total_price", Utility.formatToTwoDecimalPlaces(subTotalPrice));
        invoiceCalculationSummary.put("total_price", Utility.formatToTwoDecimalPlaces(totalPrice));
        invoiceCalculationSummary.put("total_discount", Utility.formatToTwoDecimalPlaces(totalDiscount));
        invoiceCalculationSummary.put("total_tax", Utility.formatToTwoDecimalPlaces(totalTax));

        return invoiceCalculationSummary;

    }
    


    public List<InvoiceItem> createInvoiceItems(List<SaveItemReq> invoiceItems) {


/*
        private String name;
        private Integer quantity;
        @JsonProperty(value = "unit_id")
        private UUID unitId;
        private Double rate;
        @JsonProperty(value = "discount_id")
        private UUID discountId;
        @JsonProperty(value = "tax_id")
        private UUID taxId;*/


        /*@GetMapping("/discount/master")
        public ObjAPIRes getDiscountMasterBYID(@RequestParam UUID id,
                @RequestHeader("Authorization") String token,
                @RequestHeader("account_id") String accountId,
                @RequestHeader("token_type") String tokenType);*/

        List<InvoiceItem> invoiceItemList = invoiceItems.stream().map(item -> {

            // item.getDiscountId();
            // item.getTaxId();

            double discountAmount = 0.0;
            double taxAmount = 0.0;

            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setName(item.getName());
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setCreationTs(Utility.getCurrentTimestamp());
            invoiceItem.setDeleted(false);
            invoiceItem.setRate(item.getRate());
            invoiceItem.setUnitId(item.getUnitId());

            if (item.getDiscountId() != null) {

                ObjAPIRes objAPIRes =
                        organizationServiceAPIClient.getDiscountMasterBYID(item.getDiscountId(),
                                "s", "s",
                                "s");

                // data={id=84cf0f5c-bdb8-43df-84f3-b0d5217cd667,
                // name=extra, value=100.0, org_id=28b819e1-8629-47b9-83e4-88132e2faf35, value_type=Amount, discount_type=invoice}

                System.out.println("---------------------------------");
                System.out.println("");
                System.out.println("objAPIRes : "+objAPIRes);
                System.out.println("");
                System.out.println("");
                System.out.println("---------------------------------");


                if (objAPIRes.getData().get("value_type").toString().equalsIgnoreCase("amount")) {

                    discountAmount = Double.valueOf(objAPIRes.getData().get("value").toString());
                    discountAmount = discountAmount * item.getQuantity();


                } else {

                    discountAmount = (double) (item.getRate() / 100) * Double.valueOf(objAPIRes.getData().get("value").toString());
                    discountAmount = discountAmount * item.getQuantity();

                }
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("item : "+item);
                System.out.println("obj : "+objAPIRes.getData().get("value").toString());

                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");

                discountAmount = Double.valueOf(Utility.formatToTwoDecimalPlaces(discountAmount));

                invoiceItem.setDiscountId(item.getDiscountId());
                invoiceItem.setDiscountAmount(discountAmount);

            }

            if (item.getTaxId() != null) {

                ObjAPIRes objAPIRes =
                        adminServiceAPIClient.getTaxById(item.getTaxId(),
                                "s", "s", "s");

                taxAmount = (double) (item.getRate() / 100) * Double.valueOf(objAPIRes.getData().get("value").toString());
                taxAmount = taxAmount * item.getQuantity();

                taxAmount = Double.valueOf(Utility.formatToTwoDecimalPlaces(taxAmount));

                invoiceItem.setTaxId(item.getTaxId());
                invoiceItem.setTaxAmount(taxAmount);
            }


            return invoiceItem;

        }).collect(Collectors.toList());

        return invoiceItemList;
    }

    public UUID createInvoice(SaveInvoiceReq saveInvoiceReq) {

        UUID invoiceId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setTitle(saveInvoiceReq.getTitle());
        invoice.setDescription(saveInvoiceReq.getDescription());
        invoice.setOrgId(UUID.fromString(saveInvoiceReq.getOrgId()));
        invoice.setBranchId(UUID.fromString(saveInvoiceReq.getBranchId()));
        invoice.setCustomerId(UUID.fromString(saveInvoiceReq.getCustomerId()));
        invoice.setCurrencyId(UUID.fromString(saveInvoiceReq.getCurrencyId()));
        invoice.setDate(saveInvoiceReq.getDate());
        invoice.setDueDate(saveInvoiceReq.getDueDate());

        if (saveInvoiceReq.getStatus() != null &&
                !saveInvoiceReq.getStatus().isEmpty()) {
            invoice.setStatus(saveInvoiceReq.getStatus());
        } else {
            invoice.setStatus(Constants.invoiceStatus.PENDING);
        }

        invoice.setIsDeleted(false);
        invoice.setCreationTs(Utility.getCurrentTimestamp());
        invoice.setCustomerPayload(saveInvoiceReq.getCustomerPayload());

        String note = saveInvoiceReq.getNote();

        if (note != null && !note.isEmpty()) {
            invoice.setNote(note);
        }

        /*calculateInvoice(List<SaveItemReq> invoiceItems,
                UUID invoiceTaxId, UUID invoiceDiscountId)*/

        Map<String, Object> calculateInvoiceDetails = calculateInvoice(saveInvoiceReq.getInvoiceItems(),
                (saveInvoiceReq.getTaxId() != null && !saveInvoiceReq.getTaxId().isEmpty()) ?
                        UUID.fromString(saveInvoiceReq.getTaxId()) : null,
                (saveInvoiceReq.getDiscountId() != null && !saveInvoiceReq.getDiscountId().isEmpty()) ?
                        UUID.fromString(saveInvoiceReq.getDiscountId()) : null);

        if (saveInvoiceReq.getTaxId() != null && !saveInvoiceReq.getTaxId().isEmpty()) {
            invoice.setTaxID(UUID.fromString(saveInvoiceReq.getTaxId()));
        }

        if (saveInvoiceReq.getDiscountId() != null && !saveInvoiceReq.getDiscountId().isEmpty()) {
            invoice.setDiscountId(UUID.fromString(saveInvoiceReq.getDiscountId()));
        }

        String discountName = (calculateInvoiceDetails.get("discount_name") != null) ?
                calculateInvoiceDetails.get("discount_name").toString() : null;
        String discountValueType = (calculateInvoiceDetails.get("discount_value_type") != null) ?
                calculateInvoiceDetails.get("discount_value_type").toString() : null;
        double totalPrice = (calculateInvoiceDetails.get("total_price") != null) ?
                Double.valueOf(calculateInvoiceDetails.get("total_price").toString()) : 0;
        double subTotalPrice = (calculateInvoiceDetails.get("sub_total_price") != null) ?
                Double.valueOf(calculateInvoiceDetails.get("sub_total_price").toString()) : 0;
        double totalDiscount = (calculateInvoiceDetails.get("total_discount") != null) ?
                Double.valueOf(calculateInvoiceDetails.get("total_discount").toString()) : 0;
        double totalTax = (calculateInvoiceDetails.get("total_tax") != null) ?
                Double.valueOf(calculateInvoiceDetails.get("total_tax").toString()) : 0;

        if (saveInvoiceReq.getDiscountId() != null &&
                !saveInvoiceReq.getDiscountId().isEmpty()) {
            invoice.setDiscountId(UUID.fromString(saveInvoiceReq.getDiscountId()));
        }

        if (discountName != null) {
            invoice.setDiscountName(discountName);
        }

        if (discountValueType != null) {
            invoice.setDiscountValueType(discountValueType);
        }

        invoice.setTotal(totalPrice);
        invoice.setSubTotal(subTotalPrice);
        invoice.setDiscountAmount(totalDiscount);
        invoice.setTaxAmount(totalTax);

        List<InvoiceItem> invoiceItemsToSave = (List<InvoiceItem>) calculateInvoiceDetails.get("items");

        invoiceItemsToSave = invoiceItemsToSave.stream().map(item -> {

            item.setId(UUID.randomUUID());
            item.setInvoiceId(invoiceId);
            item.setCreationTs(Utility.getCurrentTimestamp());

            return item;

        }).collect(Collectors.toList());

        invoiceItemService.addInvoiceItems(invoiceItemsToSave);
        List<SaveItemReq> invoiceItemsList = (List<SaveItemReq>) saveInvoiceReq.invoiceItems;
        System.out.println(invoiceItemsList);
        for (SaveItemReq invoiceItem: invoiceItemsList)
        {
            System.out.println("-----------------------------");
            System.out.println("INvoice Item");
            System.out.println(invoiceItem);
            System.out.println("invoice data remember");
            System.out.println(invoiceItem.getIsRemember());
            System.out.println("-------------------------------");
            InvoiceItemMaster invoiceItemMasterData = invoiceItemMasterService.getInvoiceItemMasterByNamed(invoiceItem.getName().toLowerCase());
            System.out.println("Invoice Master Data");
            System.out.println(invoiceItemMasterData);
                if(invoiceItem.getIsRemember() == true)
                {
                    if (invoiceItemMasterData==null)
                    {
                        InvoiceItemMaster invoiceItemMaster = new InvoiceItemMaster();
                        invoiceItemMaster.setId(UUID.randomUUID());
                        invoiceItemMaster.setName(invoiceItem.getName());
                        invoiceItemMaster.setUnitId(invoiceItem.getUnitId());
                        invoiceItemMaster.setHsnCode(invoiceItem.getHsnCode());
                        invoiceItemMaster.setRate(invoiceItem.getRate());
                        invoiceItemMaster.setDiscountId(invoiceItem.getDiscountId());
                        invoiceItemMaster.setTaxId(invoiceItem.getTaxId());
                        invoiceItemMaster.setCreationTs(Utility.getCurrentTimestamp());
                        invoiceItemMaster.setModifiedTs(0);
                        invoiceItemMasterService.addInvoiceItemMaster(invoiceItemMaster);
                    } else {
                        invoiceItemMasterService.deleteInvoiceItemMaster(invoiceItemMasterData.getId());
                        InvoiceItemMaster invoiceItemMaster = new InvoiceItemMaster();
                        invoiceItemMaster.setId(UUID.randomUUID());
                        invoiceItemMaster.setName(invoiceItem.getName());
                        invoiceItemMaster.setUnitId(invoiceItem.getUnitId());
                        invoiceItemMaster.setHsnCode(invoiceItem.getHsnCode());
                        invoiceItemMaster.setRate(invoiceItem.getRate());
                        invoiceItemMaster.setDiscountId(invoiceItem.getDiscountId());
                        invoiceItemMaster.setTaxId(invoiceItem.getTaxId());
                        invoiceItemMaster.setCreationTs(Utility.getCurrentTimestamp());
                        invoiceItemMaster.setModifiedTs(0);
                        invoiceItemMasterService.addInvoiceItemMaster(invoiceItemMaster);
                    }
                }


        }

        String paymentMode = saveInvoiceReq.getPaymentMode();
        String remark = saveInvoiceReq.getRemark();

        UUID invoicePaymentId = UUID.randomUUID();

        InvoicePayment invoicePayment = new InvoicePayment();
        invoicePayment.setId(invoicePaymentId);
        invoicePayment.setCreationTs(Utility.getCurrentTimestamp());
        invoicePayment.setCustomerId(UUID.fromString(saveInvoiceReq.getCustomerId()));
        invoicePayment.setPaymentAmount(totalPrice);
        invoicePayment.setStatus(Constants.invoiceStatus.PENDING);


        if (paymentMode != null && !paymentMode.isEmpty()) {
            invoicePayment.setPaymentMode(paymentMode);
        }

        if (remark != null && !remark.isEmpty()) {
            invoicePayment.setRemark(remark);
        }

        invoice.setPaymentId(invoicePaymentId);

        invoicePaymentService.save(invoicePayment);

        String invoiceNo = generateSequentialNumber(saveInvoiceReq.getOrgId());
        invoice.setInvoiceNo(invoiceNo);


        invoiceRepository.save(invoice);


        return invoiceId;
    }

    public void modifyInvoice(SaveInvoiceReq saveInvoiceReq, Invoice invoice) {

        UUID invoiceId = UUID.fromString(saveInvoiceReq.getId());


        if (saveInvoiceReq.getTitle() != null && !saveInvoiceReq.getTitle().isEmpty()) {
            invoice.setTitle(saveInvoiceReq.getTitle());
        }

        if (saveInvoiceReq.getDescription() != null && !saveInvoiceReq.getDescription().isEmpty()) {
            invoice.setDescription(saveInvoiceReq.getDescription());
        }
        if (saveInvoiceReq.getPurchaseCode() != null && !saveInvoiceReq.getPurchaseCode().isEmpty()) {
            invoice.setPurchaseCode(saveInvoiceReq.getPurchaseCode());
        }


        if (saveInvoiceReq.getStatus() != null && !saveInvoiceReq.getStatus().isEmpty()) {
            invoice.setStatus(saveInvoiceReq.getStatus());
        }

        if (saveInvoiceReq.getCurrencyId() != null && !saveInvoiceReq.getCurrencyId().isEmpty()) {
            invoice.setCurrencyId(UUID.fromString(saveInvoiceReq.getCurrencyId()));
        }

        if (saveInvoiceReq.getCurrencyId() != null && !saveInvoiceReq.getCurrencyId().isEmpty()) {
            invoice.setCurrencyId(UUID.fromString(saveInvoiceReq.getCurrencyId()));
        }

        if (saveInvoiceReq.getDate() != null && !saveInvoiceReq.getDate().isEmpty()) {
            invoice.setDate(saveInvoiceReq.getDate());
        }

        if (saveInvoiceReq.getDueDate() != null && !saveInvoiceReq.getDueDate().isEmpty()) {
            invoice.setDueDate(saveInvoiceReq.getDueDate());
        }

        if (saveInvoiceReq.getCustomerPayload() != null && !saveInvoiceReq.getCustomerPayload().isEmpty()) {
            invoice.setCustomerPayload(saveInvoiceReq.getCustomerPayload());
        }

        invoice.setModifiedTs(Utility.getCurrentTimestamp());

        if (saveInvoiceReq.getTaxId() != null && !saveInvoiceReq.getTaxId().isEmpty()) {
            invoice.setTaxID(UUID.fromString(saveInvoiceReq.getTaxId()));
        }

        if (saveInvoiceReq.getDiscountId() != null && !saveInvoiceReq.getDiscountId().isEmpty()) {
            invoice.setDiscountId(UUID.fromString(saveInvoiceReq.getDiscountId()));
        }

        Map<String, Object> calculateInvoiceDetails = null;

        boolean priceUpdated = false;

        if (saveInvoiceReq.getInvoiceItems() != null &&
                !saveInvoiceReq.getInvoiceItems().isEmpty() ||
                (saveInvoiceReq.getTaxId() != null && !saveInvoiceReq.getTaxId().isEmpty()) ||
                (saveInvoiceReq.getDiscountId() != null && !saveInvoiceReq.getDiscountId().isEmpty())) {


            if (saveInvoiceReq.getInvoiceItems() == null ||
                    saveInvoiceReq.getInvoiceItems().isEmpty()) {

                saveInvoiceReq.setInvoiceItems(invoiceItemService.getAllInvoiceItem().stream().map(
                        item -> {

                            SaveItemReq saveItemReq = new SaveItemReq();
                            saveItemReq.setName(item.getName());
                            saveItemReq.setHsnCode(item.getHsnCode());
                            saveItemReq.setQuantity(item.getQuantity());
                            saveItemReq.setUnitId(item.getUnitId());
                            saveItemReq.setRate(item.getRate());
                            saveItemReq.setDiscountId(item.getDiscountId());
                            saveItemReq.setTaxId(item.getTaxId());

                            return saveItemReq;
                        }
                ).collect(Collectors.toList()));

            }


            calculateInvoiceDetails = calculateInvoice(saveInvoiceReq.getInvoiceItems(),
                    (saveInvoiceReq.getTaxId() != null && !saveInvoiceReq.getTaxId().isEmpty()) ?
                            UUID.fromString(saveInvoiceReq.getTaxId()) : null,
                    (saveInvoiceReq.getDiscountId() != null && !saveInvoiceReq.getDiscountId().isEmpty()) ?
                            UUID.fromString(saveInvoiceReq.getDiscountId()) : null);


            List<InvoiceItem> invoiceItemsToSave = (List<InvoiceItem>) calculateInvoiceDetails.get("items");

            invoiceItemsToSave = invoiceItemsToSave.stream().map(item -> {

                item.setId(UUID.randomUUID());
                item.setInvoiceId(invoiceId);
                item.setCreationTs(Utility.getCurrentTimestamp());

                return item;

            }).collect(Collectors.toList());

            int deleteItems = invoiceItemService.deleteByInvoiceId(invoiceId);
            invoiceItemService.addInvoiceItems(invoiceItemsToSave);


            String discountName = (calculateInvoiceDetails.get("discount_name") != null) ?
                    calculateInvoiceDetails.get("discount_name").toString() : null;
            String discountValueType = (calculateInvoiceDetails.get("discount_value_type") != null) ?
                    calculateInvoiceDetails.get("discount_value_type").toString() : null;
            double totalPrice = (calculateInvoiceDetails.get("total_price") != null) ?
                    Double.valueOf(calculateInvoiceDetails.get("total_price").toString()) : 0;
            double subTotalPrice = (calculateInvoiceDetails.get("sub_total_price") != null) ?
                    Double.valueOf(calculateInvoiceDetails.get("sub_total_price").toString()) : 0;
            double totalDiscount = (calculateInvoiceDetails.get("total_discount") != null) ?
                    Double.valueOf(calculateInvoiceDetails.get("total_discount").toString()) : 0;
            double totalTax = (calculateInvoiceDetails.get("total_tax") != null) ?
                    Double.valueOf(calculateInvoiceDetails.get("total_tax").toString()) : 0;

            if (saveInvoiceReq.getDiscountId() != null &&
                    !saveInvoiceReq.getDiscountId().isEmpty()) {
                invoice.setDiscountId(UUID.fromString(saveInvoiceReq.getDiscountId()));
            }

            if (discountName != null) {
                invoice.setDiscountName(discountName);
            }

            if (discountValueType != null) {
                invoice.setDiscountValueType(discountValueType);
            }

            invoice.setTotal(totalPrice);
            invoice.setSubTotal(subTotalPrice);
            invoice.setDiscountAmount(totalDiscount);
            invoice.setTaxAmount(totalTax);

            priceUpdated = true;

        }


        Optional<InvoicePayment> invoicePaymentOptional = invoicePaymentService.getInvoicePaymentById(invoice.getPaymentId());

        if (invoicePaymentOptional.isPresent() || priceUpdated) {

            InvoicePayment invoicePayment = invoicePaymentOptional.get();

            if (priceUpdated) {
                invoicePayment.setPaymentAmount(invoice.getTotal());
            }


            if (invoicePaymentOptional.isPresent()) {

                invoicePayment.setModifiedTs(Utility.getCurrentTimestamp());

                String paymentMode = saveInvoiceReq.getPaymentMode();
                String remark = saveInvoiceReq.getRemark();

                if (paymentMode != null && !paymentMode.isEmpty()) {
                    invoicePayment.setPaymentMode(paymentMode);
                }

                if (remark != null && !remark.isEmpty()) {
                    invoicePayment.setRemark(remark);
                }
            }

            invoicePaymentService.save(invoicePayment);

        }


        invoiceRepository.save(invoice);

    }

    public List<Invoice> getAllInvoice() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceByID(UUID id) {
        return invoiceRepository.findById(id);
    }


    public Invoice addInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoice(Map<String, Object> invoice, UUID id) {

        return customInvoiceRepository.updateInvoice(invoice, id);

    }

    public void deleteInvoice(UUID id) {

        invoiceRepository.deleteById(id);
    }

    @Transactional
    public void updateStatus(String status, UUID id)
    {
        invoiceRepository.updateStatus(status, id);
    }
}
