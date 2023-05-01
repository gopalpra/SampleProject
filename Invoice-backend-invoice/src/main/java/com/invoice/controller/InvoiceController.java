package com.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.constant.Constants;
import com.invoice.dto.CalculateInvoiceReq;
import com.invoice.entity.*;
import com.invoice.dto.SaveInvoiceReq;
import com.invoice.service.InvoiceItemService;
import com.invoice.service.InvoiceSequentialService;
import com.invoice.service.InvoiceService;
import com.invoice.util.HTTPResponse;
import com.invoice.util.HttpConstant;
import com.invoice.util.RestTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class InvoiceController {
    public static Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceItemService invoiceItemService;

    @Autowired
    private InvoiceSequentialService invoiceSequentialService;
    @Autowired
    private RestTemplateUtil restTemplateUtil;
    @Autowired
    private HTTPResponse httpResponse;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/invoice/index")
    public String index() {
        return "Invoice service get started";
    }

//---------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------GET INVOICE BY ID-------------------------------------------------------------------------

    @GetMapping("/invoice")
    public ResponseEntity<HTTPResponse> getInvoiceById(@RequestParam UUID id) {
        logger.info("getInvoiceById() called");
        logger.info("id: {}", id);
        Optional<Invoice> invoice = invoiceService.getInvoiceByID(id);
        if (invoice.isPresent()) {
            List<InvoiceItem> invoiceItem = invoiceItemService.getInvoiceItemByInvoiceId(id);
            if (invoiceItem.size() > 0) {
                Map map = objectMapper.convertValue(invoice.get(), Map.class);
                map.put("items:", invoiceItem);
                return ResponseEntity.ok(new HTTPResponse(map, "success", HttpConstant.SUCCESS_STATUS_CODE));
            }

//                return ResponseEntity.badRequest().body(new HTTPResponse(null, "invoice item data not found", HttpConstant.NOT_FOUND_STATUS_CODE));
        }
        return ResponseEntity.badRequest().body(new HTTPResponse(null, "invoice data not found", HttpConstant.NOT_FOUND_STATUS_CODE));
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------ADD INVOICE-------------------------------------------------------------------------


    @PostMapping("/invoice")
    public ResponseEntity<HTTPResponse> saveInvoice
            (@RequestBody SaveInvoiceReq saveInvoiceReq) {

        logger.info("Save Invoice API");
        logger.info("");
        logger.info("SaveInvoiceReq Request");
        logger.info("{}", saveInvoiceReq.toString());

        String invoiceId = saveInvoiceReq.getId();

        // Edit Invoice
        if (invoiceId != null && !invoiceId.isEmpty()) {

            Optional<Invoice> invoice = invoiceService.getInvoiceByID(UUID.fromString(invoiceId));

            if (!invoice.isPresent()) {

                logger.info("Invoice not found");
                return ResponseEntity.badRequest()
                        .body(new HTTPResponse(null, "invoice data not found",
                                HttpConstant.NOT_FOUND_STATUS_CODE));


            } else {

                if(!invoice.get().getStatus().equals(Constants.invoiceStatus.PENDING) &&
                        !invoice.get().getStatus().equals(Constants.invoiceStatus.DRAFT)){

                    return ResponseEntity.badRequest()
                            .body(new HTTPResponse(null, "invoice can't be edited",
                                    HttpConstant.NOT_FOUND_STATUS_CODE));

                } else {

                    Map<String, Object> invoiceResponse = new HashMap<>();
                    invoiceResponse.put("invoice_id", invoiceId);

                    invoiceService.modifyInvoice(saveInvoiceReq, invoice.get());
                    return ResponseEntity.ok(new HTTPResponse(invoiceResponse, "Invoice modified successfully!", HttpConstant.SUCCESS_STATUS_CODE));


                }

            }


        }

        // Create Invoice
        else {
            invoiceId = invoiceService.createInvoice(saveInvoiceReq).toString();

            Map<String, Object> invoiceResponse = new HashMap<>();
            invoiceResponse.put("invoice_id", invoiceId);

            return ResponseEntity.ok(new HTTPResponse(invoiceResponse, "Invoice created successfully!", HttpConstant.SUCCESS_STATUS_CODE));

        }

    }


    @PostMapping("/invoice/calculate")
    public ResponseEntity<HTTPResponse> calculateInvoice(
            @RequestBody CalculateInvoiceReq calculateInvoiceReq) {

        logger.info("Calculate Invoice API");
        logger.info("");
        logger.info("{}", calculateInvoiceReq.toString());

        return ResponseEntity.ok(new HTTPResponse(
                invoiceService.calculateInvoice(calculateInvoiceReq.getInvoiceItems(),
                        (calculateInvoiceReq.getTaxId() != null && !calculateInvoiceReq.getTaxId().isEmpty()) ?
                                UUID.fromString(calculateInvoiceReq.getTaxId()) : null,
                        (calculateInvoiceReq.getDiscountId() != null && !calculateInvoiceReq.getDiscountId().isEmpty()) ?
                                UUID.fromString(calculateInvoiceReq.getDiscountId()) : null),
                "success", HttpConstant.SUCCESS_STATUS_CODE));

    }




/*
    @PostMapping("/invoice/bkp")
    public ResponseEntity<HTTPResponse> addInvoice(@RequestParam("title") String title,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("invoice_no") UUID invoiceNo,
                                                   @RequestParam("org_id") UUID orgId,
                                                   @RequestParam("branch_id") UUID branchId,
                                                   @RequestParam("customer_id") UUID customerId,
                                                   @RequestParam("customer_code") String customerCode,
                                                   @RequestParam("currency_id") UUID currencyId,
                                                   @RequestParam("logo_url") String logoUrl,
                                                   @RequestParam("date") String date,
                                                   @RequestParam("due_date") String dueDate,
                                                   @RequestParam("payment_id") UUID paymentId,
                                                   @RequestParam("status") String status,
                                                   @RequestParam("note") String note,
                                                   @RequestParam("tax_id") UUID taxId,
                                                    @RequestParam("discount_id") UUID discountId,
                                                   @RequestParam("discount_amount") Double discountAmount,
                                                   @RequestParam("discount_name") String discountName,
                                                @RequestParam("tax_amount") Double taxAmount,
                                                     @RequestParam("discount_type") String discountType,
                                                   @RequestBody Map<String,List<ReqInvoiceItem>> items) throws JsonProcessingException {
        logger.info("addInvoice() called");
        logger.info("");
        logger.info("title: {}", title);
        logger.info("description: {}", description);
        logger.info("invoice_no: {}", invoiceNo);
        logger.info("org_id: {}", orgId);
        logger.info("branch_id: {}", branchId);
        logger.info("customer_id: {}", customerId);
        logger.info("customer_code: {}", customerCode);
        logger.info("logo_url: {}", logoUrl);
        logger.info("date: {}", date);
        logger.info("due_date: {}", dueDate);
        logger.info("payment_id: {}", paymentId);
        logger.info("status: {}", status);
        logger.info("note: {}", note);
        logger.info("tax_id: {}", taxId);
        logger.info("tax_amount: {}", taxAmount);
        logger.info("discount_id: {}", discountId);
        logger.info("discount_amount: {}", discountAmount);
        logger.info("discount_name: {}", discountName);
        logger.info("discount_type: {}", discountType);
        logger.info("items: {}", items);
        if (title == null || title.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "title should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (description == null || description.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "description should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (invoiceNo == null || invoiceNo.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "invoice no should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (orgId == null || orgId.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "org id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (branchId == null || branchId.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "branch id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (customerId == null || customerId.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "customer id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (customerCode == null || customerCode.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "customer code should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (currencyId == null || currencyId.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "currency id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (logoUrl == null || logoUrl.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "logo url should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (date == null || date.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "date should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (!Validator.validateDate(date)) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "date format must be correct", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (dueDate == null || dueDate.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "due date should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (!Validator.validateDate(dueDate)) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "due date format must be correct", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (paymentId == null || paymentId.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "payment id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (status == null || status.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "status should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (!status.equalsIgnoreCase("DRAFT") && !status.equalsIgnoreCase("APPROVED") && !status.equalsIgnoreCase("DECLINED")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "status must be correct", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }

        if (note == null || note.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "note should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (taxId == null || taxId.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "tax id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (taxAmount == null || taxAmount.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "tax amount should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (discountId == null || discountId.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "discount id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (discountAmount == null || discountAmount.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "discount amount should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (discountType == null || discountType.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "discount type should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (discountName == null || discountName.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "discount name should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        if (items == null || items.equals("")) {
            return ResponseEntity.badRequest().body(new HTTPResponse(null, "items should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
        }
        UUID invoiceId = UUID.randomUUID();
        long creation_ts = System.currentTimeMillis() / 1000;
        double subTotal = 0;
        double total = 0;
        InvoiceItem invoiceItem = new InvoiceItem();
        List<ReqInvoiceItem> responseData = items.get("items");
        List<InvoiceItem> items1 = new ArrayList<>();
        for (ReqInvoiceItem item : responseData) {
            invoiceItem.setId(UUID.randomUUID());
            invoiceItem.setInvoiceId(invoiceId);
            if (item.getName() != null || !item.getName().equals("")) {
                invoiceItem.setName(item.getName());
            } else {
                return ResponseEntity.badRequest().body(new HTTPResponse(null, "item name should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
            }
            if (item.getQuantity() != null || !item.getQuantity().equals("")) {
                invoiceItem.setQuantity(item.getQuantity());
            } else {
                return ResponseEntity.badRequest().body(new HTTPResponse(null, "quantity should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
            }
            if (item.getUnitId() != null || !item.getUnitId().equals("")) {
                invoiceItem.setUnitId(item.getUnitId());
            } else {
                return ResponseEntity.badRequest().body(new HTTPResponse(null, "unit id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
            }
            if (item.getRate() != null || !item.getRate().equals("")) {
                invoiceItem.setRate(item.getRate());
            } else {
                return ResponseEntity.badRequest().body(new HTTPResponse(null, "rate should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
            }
            if (item.getDiscountId() != null || !item.getDiscountId().equals("")) {
                invoiceItem.setDiscountId(item.getDiscountId());
            } else {
                return ResponseEntity.badRequest().body(new HTTPResponse(null, "discount id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
            }
            if (item.getTaxId() != null || !item.getTaxId().equals("")) {
                invoiceItem.setTaxId(item.getTaxId());
            } else {
                return ResponseEntity.badRequest().body(new HTTPResponse(null, "tax id should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
            }
            if (item.getDiscountAmount() != null || !item.getDiscountAmount().equals("")) {
                invoiceItem.setDiscountAmount(item.getDiscountAmount());
            } else {
                return ResponseEntity.badRequest().body(new HTTPResponse(null, "discount amount  should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
            }
            if (item.getTaxAmount() != null || !item.getTaxAmount().equals("")) {
                invoiceItem.setTaxAmount(item.getTaxAmount());
            } else {
                return ResponseEntity.badRequest().body(new HTTPResponse(null, "tax amount  should not be empty", HttpConstant.BAD_REQUEST_STATUS_CODE));
            }
            invoiceItem.setDeleted(true);
            invoiceItem.setCreationTs(creation_ts);
            invoiceItem.setModifiedTs(0);
            subTotal += invoiceItem.getRate() * invoiceItem.getQuantity() - invoiceItem.getDiscountAmount() - invoiceItem.getTaxAmount();
            invoiceItemService.addInvoiceItem(invoiceItem);
            items1.add(invoiceItem);
        }

        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setTitle(title);
        invoice.setDescription(description);
        invoice.setInvoiceNo(invoiceNo);
        invoice.setOrgId(orgId);
        invoice.setBranchId(branchId);
        invoice.setCustomerId(customerId);
        invoice.setCustomerCode(customerCode);
        invoice.setCurrencyId(currencyId);
        invoice.setLogoUrl(logoUrl);
        invoice.setDate(date);
        invoice.setDueDate(dueDate);
        invoice.setPaymentId(paymentId);
        invoice.setStatus(status);
        invoice.setNote(note);
        invoice.setSubTotal(subTotal);
        invoice.setTaxID(taxId);
        invoice.setTaxAmount(taxAmount);
        invoice.setDiscountId(discountId);
        invoice.setDiscountAmount(discountAmount);
        invoice.setDiscountName(discountName);
        invoice.setDiscountValueType(discountType);
        total = subTotal - discountAmount - taxAmount;
        invoice.setTotal(total);
        invoice.setIsDeleted(true);
        invoice.setCreationTs(System.currentTimeMillis() / 1000);
        invoice.setModifiedTs(0);
        invoiceService.addInvoice(invoice);
        Map map = objectMapper.convertValue(invoice, Map.class);
        map.put("items:", items1);
        return ResponseEntity.ok(new HTTPResponse(map, "data is created successfully", HttpConstant.SUCCESS_STATUS_CODE));
    }
*/


//    //---------------------------------------------------------------------------------------------------------------------------------------------------------
////--------------------------------------------------------UPDATE INVOICE-------------------------------------------------------------------------
//    @PutMapping("/invoice")
//    public ResponseEntity<HTTPResponse> updateInvoice(@RequestParam(value = "id") UUID id,
//                                                      //pass only those fields which you want to update
//                                                      @RequestParam(value = "title",required = false) String title,
//                                                   @RequestParam(value ="description",required = false) String description,
//                                                   @RequestParam(value ="invoice_no",required = false) UUID invoiceNo,
//                                                   @RequestParam(value ="org_id",required = false) UUID orgId,
//                                                   @RequestParam(value ="branch_id",required = false) UUID branchId,
//                                                   @RequestParam(value="customer_id",required = false) UUID customerId,
//                                                   @RequestParam(value="customer_code",required = false) String customerCode,
//                                                   @RequestParam(value="currency_id",required = false) UUID currencyId,
//                                                   @RequestParam(value="logo_url",required = false) String logoUrl,
//                                                   @RequestParam(value="date",required = false) String date,
//                                                   @RequestParam(value="due_date",required = false) String dueDate,
//                                                   @RequestParam(value="payment_id",required = false) UUID paymentId,
//                                                   @RequestParam(value="status",required = false) String status,
//                                                   @RequestParam(value="note",required = false) String note,
//                                                   @RequestParam(value="tax_id",required = false) UUID taxId,
//                                                    @RequestParam(value="discount_id",required = false) UUID discountId,
//                                                   @RequestParam(value="discount_amount",required = false) Double discountAmount,
//                                                   @RequestParam(value="discount_name",required = false) String discountName,
//                                                @RequestParam(value="tax_amount",required = false) Double taxAmount,
//                                                     @RequestParam(value="discount_type",required = false) String discountType,
//                                                     @RequestParam(value="is_deleted",required = false) Boolean isDeleted,
//                                                   @RequestBody(required=false) Map<String,List<ReqInvoiceItem>> items) {
//
//        logger.info("updateInvoice() called");
//        logger.info("");
//        logger.info("title: {}", title);
//        logger.info("description: {}", description);
//        logger.info("invoice_no: {}", invoiceNo);
//        logger.info("org_id: {}", orgId);
//        logger.info("branch_id: {}", branchId);
//        logger.info("customer_id: {}", customerId);
//        logger.info("customer_code: {}", customerCode);
//        logger.info("logo_url: {}", logoUrl);
//        logger.info("date: {}", date);
//        logger.info("due_date: {}", dueDate);
//        logger.info("payment_id: {}", paymentId);
//        logger.info("status: {}", status);
//        logger.info("note: {}", note);
//        logger.info("tax_id: {}", taxId);
//        logger.info("tax_amount: {}", taxAmount);
//        logger.info("discount_id: {}", discountId);
//        logger.info("discount_amount: {}", discountAmount);
//        logger.info("discount_name: {}", discountName);
//        logger.info("discount_type: {}", discountType);
//        logger.info("items: {}", items);
//        Optional<Invoice> invoice = invoiceService.getInvoiceByID(id);
//        Map<String,Object> invoiceData = new HashMap<>();
//        Map<String,Object> invoiceItemData = new HashMap<>();
//
//        if(invoice.isPresent())
//        {
//            List<InvoiceItem> invoiceItemList = invoiceItemService.getInvoiceItemByInvoiceId(id);
//            List<ReqInvoiceItem> responseData = items.get("items");
//
//            for(ReqInvoiceItem item : responseData)
//            {
//                if(item.getName()!=null)
//                {
//                    if(!item.getName().equals("")&&!item.getName().equalsIgnoreCase(invoiceItemList.get().getName()))
//                    {
//                        invoiceData.put("name",item.getName());
//                    }
//                }
//            }
//
//            if(invoice.get().getTitle()!=null)
//            {
//                if(!invoice.get().getTitle().equals("")&&!invoice.get().getTitle().equalsIgnoreCase(title))
//                {
//                    invoiceData.put("title",title);
//                }
//            }
//            if(invoice.get().getDescription()!=null)
//            {
//                if(!invoice.get().getDescription().equals("")&&!invoice.get().getDescription().equalsIgnoreCase(description))
//                {
//                    invoiceData.put("description",description);
//                }
//            }
//            if(invoice.get().getInvoiceNo()!=null)
//            {
//                if(!invoice.get().getInvoiceNo().equals("")&&!invoice.get().getInvoiceNo().equals(invoiceNo))
//                {
//                    invoiceData.put("invoiceNo",invoiceNo);
//                }
//            }
//            if(invoice.get().getOrgId()!=null)
//            {
//                if(!invoice.get().getOrgId().equals("")&&!invoice.get().getOrgId().equals(orgId))
//                {
//                    invoiceData.put("orgId",orgId);
//                }
//            }
//            if(invoice.get().getBranchId()!=null)
//            {
//                if(!invoice.get().getBranchId().equals("")&&!invoice.get().getBranchId().equals(branchId))
//                {
//                    invoiceData.put("branchId",branchId);
//                }
//            }
//            if(invoice.get().getCustomerId()!=null)
//            {
//                if(!invoice.get().getCustomerId().equals("")&&!invoice.get().getCustomerId().equals(customerId))
//                {
//                    invoiceData.put("customerId",customerId);
//                }
//            }
//            if(invoice.get().getCustomerCode()!=null)
//            {
//                if(!invoice.get().getCustomerCode().equals("")&&!invoice.get().getCustomerCode().equalsIgnoreCase(customerCode))
//                {
//                    invoiceData.put("customerCode",customerCode);
//                }
//            }
//            if(invoice.get().getCurrencyId()!=null)
//            {
//                if(!invoice.get().getCurrencyId().equals("")&&!invoice.get().getCurrencyId().equals(currencyId))
//                {
//                    invoiceData.put("currencyId",currencyId);
//                }
//            }
//            if(invoice.get().getLogoUrl()!=null)
//            {
//                if(!invoice.get().getLogoUrl().equals("")&&!invoice.get().getLogoUrl().equalsIgnoreCase(logoUrl))
//                {
//                    invoiceData.put("logoUrl",logoUrl);
//                }
//            }
//            if(invoice.get().getDate()!=null)
//            {
//                if(!invoice.get().getDate().equals("")&&!invoice.get().getDate().equalsIgnoreCase(date))
//                {
//                    invoiceData.put("date",date);
//                }
//            }
//            if(invoice.get().getDueDate()!=null)
//            {
//                if(!invoice.get().getDueDate().equals("")&&!invoice.get().getDueDate().equalsIgnoreCase(dueDate))
//                {
//                    invoiceData.put("dueDate",dueDate);
//                }
//            }
//            if(invoice.get().getPaymentId()!=null)
//            {
//                if(!invoice.get().getPaymentId().equals("")&&!invoice.get().getPaymentId().equals(paymentId))
//                {
//                    invoiceData.put("paymentId",paymentId);
//                }
//            }
//            if(invoice.get().getStatus()!=null)
//            {
//                if(!invoice.get().getStatus().equals("")&&!invoice.get().getStatus().equalsIgnoreCase(status))
//                {
//                    invoiceData.put("status",status);
//                }
//            }
//            if(invoice.get().getNote()!=null)
//            {
//                if(!invoice.get().getNote().equals("")&&!invoice.get().getNote().equalsIgnoreCase(note))
//                {
//                    invoiceData.put("note",note);
//                }
//            }
//            if(invoice.get().getTaxID()!=null)
//            {
//                if(!invoice.get().getTaxID().equals("")&&!invoice.get().getTaxID().equals(taxId))
//                {
//                    invoiceData.put("taxId",taxId);
//                }
//            }
//            if(invoice.get().getDiscountId()!=null)
//            {
//                if(!invoice.get().getDiscountId().equals("")&&!invoice.get().getDiscountId().equals(discountId))
//                {
//                    invoiceData.put("discountId",discountId);
//                }
//            }
//            if(invoice.get().getDiscountAmount()!=null)
//            {
//                if(!invoice.get().getDiscountAmount().equals("")&&!invoice.get().getDiscountAmount().equals(discountAmount))
//                {
//                    invoiceData.put("discountAmount",discountAmount);
//                }
//            }
//            if(invoice.get().getDiscountName()!=null)
//            {
//                if(!invoice.get().getDiscountName().equals("")&&!invoice.get().getDiscountName().equalsIgnoreCase(discountName))
//                {
//                    invoiceData.put("discountName",discountName);
//                }
//            }
//            if(invoice.get().getTaxAmount()!=null)
//            {
//                if(!invoice.get().getTaxAmount().equals("")&&!invoice.get().getTaxAmount().equals(taxAmount))
//                {
//                    invoiceData.put("taxAmount",taxAmount);
//                }
//            }
//            if(invoice.get().getDiscountValueType()!=null)
//            {
//                if(!invoice.get().getDiscountValueType().equals("")&&!invoice.get().getDiscountValueType().equalsIgnoreCase(discountType))
//                {
//                    invoiceData.put("discountValueType",discountType);
//                }
//            }
//            if(invoice.get().getIsDeleted()!=null)
//            {
//                if(!invoice.get().getIsDeleted().equals("")&&!invoice.get().getIsDeleted().equals(isDeleted))
//                {
//                    invoiceData.put("isDeleted",isDeleted);
//                }
//            }
//            invoiceData.put("modifiedTs", System.currentTimeMillis() / 1000);
////            Country result=countryService.updateCountry(countryData,id);
////
////            return ResponseEntity.ok(new HTTPResponse(result,"data is updated successfully",HttpConstant.SUCCESS_STATUS_CODE));
//        }
//        return ResponseEntity.badRequest().body(new HTTPResponse(null,"data not found",HttpConstant.BAD_REQUEST_STATUS_CODE));
////
//    }
//
////    ---------------------------------------------------------------------------------------------------------------------------------------------------------
////--------------------------------------------------------DELETE INVOICE -------------------------------------------------------------------------

/*
    @DeleteMapping("/invoice")
    public ResponseEntity<HTTPResponse> deleteInvoice(@RequestParam UUID id) {
        logger.info("deleteInvoice() called");
        logger.info("id: {}",id);
        Optional<Invoice> invoice = invoiceService.getInvoiceByID(id);
        if (invoice.isPresent()) {
            List<InvoiceItem> invoiceItem= invoiceItemService.getInvoiceItemByInvoiceId(id);
            if(invoiceItem.size()>0)
            {
                invoiceItemService.deleteInvoiceItemByInvoiceId(id);
                invoiceService.deleteInvoice(id);
                return ResponseEntity.ok(new HTTPResponse(null, "data is deleted successfully", HttpConstant.SUCCESS_STATUS_CODE));
            }
//            return ResponseEntity.badRequest().body(new HTTPResponse(null, "invoice item data not found", HttpConstant.NOT_FOUND_STATUS_CODE));
        }
        return ResponseEntity.badRequest().body(new HTTPResponse(null, "data not found", HttpConstant.NOT_FOUND_STATUS_CODE));
    }
*/


    //---------------------------------------------------------------------------------------------------------------------
    //----------------------------GET CURRENCY LIST-------------------------------------------------------------------------
    @GetMapping("/currency/list")
    public Map<String,Object> getCurrencyList(@RequestHeader("Authorization") String token,
                                             @RequestHeader("account_id") String accountId,
                                             @RequestHeader("token_type") String tokenType) {
        logger.info("getCurrenyList() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("  ");
        Map<String,Object> currencyData = restTemplateUtil.getCurrencyData(accountId,token,tokenType);
        System.out.println("currencyData: "+currencyData);
        List<Object> dataPayload = (List<Object>) currencyData.get("data");
        if (dataPayload.size() > 0) {
            return httpResponse.getHttpResponse(dataPayload, HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
        } else {
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
        }
    }

    //---------------------------------------------------------------------------------------------------------------------
    //----------------------------GET TAX LIST-------------------------------------------------------------------------
    @GetMapping("/tax/list")
    public Map<String,Object> getTaxList(@RequestHeader("Authorization") String token,
                                              @RequestHeader("account_id") String accountId,
                                              @RequestHeader("token_type") String tokenType) {
        logger.info("getTaxList() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("  ");
        Map<String,Object> taxData = restTemplateUtil.getTAXData(accountId,token,tokenType);
        System.out.println("TaxData: "+taxData);
        List<Object> dataPayload = (List<Object>) taxData.get("data");
        if (dataPayload.size() > 0) {
            return httpResponse.getHttpResponse(dataPayload, HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
        } else {
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
        }
    }



    //---------------------------------------------------------------------------------------------------------------------
    //----------------------------GET TAX LIST-------------------------------------------------------------------------
    @PutMapping("/invoice/status")
    public Map<String,Object> updateStatus(@RequestBody Map<String,Object> data,
                                        @RequestHeader("Authorization") String token,
                                         @RequestHeader("account_id") String accountId,
                                         @RequestHeader("token_type") String tokenType) {
        logger.info("updateStatus() called");
        logger.info("data: {}", data);
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("  ");

        String status = (String) data.get("status");
        String idStr = (String)  data.get("invoice_id");
        UUID id = UUID.fromString(idStr);

        if(status==null ||status.equals(""))
        {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_STATUS, HttpConstant.BAD_REQUEST_STATUS_CODE);

        }
        if(idStr==null ||idStr.equals(""))
        {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);

        }
        Optional<Invoice> invoiceData = invoiceService.getInvoiceByID(id);
        if (invoiceData.isPresent())
        {
            System.out.println("--------------------------------");
            System.out.println("status");
            System.out.println(status);
            System.out.println("--------------------------------");
            System.out.println("invoiceData.get().getStatus()");
            System.out.println(invoiceData.get().getStatus());
            System.out.println("--------------------------------");
            System.out.println("invoiceData.get().getStatus()==Constants.invoiceStatus.DRAFT && status.equalsIgnoreCase(Constants.invoiceStatus.PENDING)");
            System.out.println(invoiceData.get().getStatus().equalsIgnoreCase(Constants.invoiceStatus.DRAFT) && status.equalsIgnoreCase(Constants.invoiceStatus.PENDING));
            System.out.println("--------------------------------");
            System.out.println("invoiceData.get().getStatus()==Constants.invoiceStatus.PENDING && status.equalsIgnoreCase(Constants.invoiceStatus.APPROVED)");
            System.out.println(invoiceData.get().getStatus().equalsIgnoreCase(Constants.invoiceStatus.PENDING) && status.equalsIgnoreCase(Constants.invoiceStatus.APPROVED));
            System.out.println("--------------------------------");
            System.out.println("invoiceData.get().getStatus()==Constants.invoiceStatus.PENDING && status.equalsIgnoreCase(Constants.invoiceStatus.DECLINE)");
            System.out.println(invoiceData.get().getStatus().equalsIgnoreCase(Constants.invoiceStatus.PENDING) && status.equalsIgnoreCase(Constants.invoiceStatus.DECLINED));
            System.out.println();

            if(invoiceData.get().getStatus().equalsIgnoreCase(Constants.invoiceStatus.DRAFT) && status.equalsIgnoreCase(Constants.invoiceStatus.PENDING))
                {
                    System.out.println("In draft condition");
                    invoiceService.updateStatus(status,id);

                    return httpResponse.getHttpResponse(null, HttpConstant.DATA_UPDATED, HttpConstant.SUCCESS_STATUS_CODE);

                }

                else if (invoiceData.get().getStatus().equalsIgnoreCase(Constants.invoiceStatus.PENDING) && status.equalsIgnoreCase(Constants.invoiceStatus.APPROVED))
                {
                    System.out.println("in pending condition");
                    invoiceService.updateStatus(status,id);

                    return httpResponse.getHttpResponse(null, HttpConstant.DATA_UPDATED, HttpConstant.SUCCESS_STATUS_CODE);

                }
                else if(invoiceData.get().getStatus().equalsIgnoreCase(Constants.invoiceStatus.PENDING) && status.equalsIgnoreCase(Constants.invoiceStatus.DECLINED))
                {
                    System.out.println("in pending decline");
                    invoiceService.updateStatus(status,id);

                    return httpResponse.getHttpResponse(null, HttpConstant.DATA_UPDATED, HttpConstant.SUCCESS_STATUS_CODE);

                }
                else
                {
                    return httpResponse.getHttpResponse(null, HttpConstant.INCORRECT_STATUS, HttpConstant.BAD_REQUEST_STATUS_CODE);
                }
        }
        else
        {
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
        }
    }



}
