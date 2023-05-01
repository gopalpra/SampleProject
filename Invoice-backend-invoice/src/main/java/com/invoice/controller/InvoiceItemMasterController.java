package com.invoice.controller;

import com.invoice.entity.InvoiceItemMaster;
import com.invoice.service.InvoiceItemMasterService;
import com.invoice.util.HTTPResponse;
import com.invoice.util.HttpConstant;
import com.invoice.util.RestTemplateUtil;
import com.invoice.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class InvoiceItemMasterController
{
    public static Logger logger = LoggerFactory.getLogger(InvoiceItemMasterController.class);

    @Autowired
    private InvoiceItemMasterService invoiceItemMasterService;
    @Autowired
    private HTTPResponse httpResponse;

    @Autowired
    private RestTemplateUtil restTemplateUtil;


    @GetMapping("/master")
    public String index() {
        logger.info("Invoice item master service get started");
        return "Invoice item master service get started";
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------GET INVOICE ITEM MASTER LIST------------------------------------------------------------------------
    @GetMapping("/invoice/item/master/list")
    public Map<String, Object> getInvoiceItemMasterList(@RequestHeader("Authorization") String token,
                                                        @RequestHeader("account_id") String accountId,
                                                        @RequestHeader("token_type") String tokenType)
    {

        logger.info("getInvoiceItemMasterList() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("  ");
        List<InvoiceItemMaster> invoiceItemMasters =  invoiceItemMasterService.getAllInvoiceItemMaster();
        if (invoiceItemMasters.size() > 0) {
            return httpResponse.getHttpResponse(invoiceItemMasters, HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
        } else {
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------GET INVOICE ITEM MASTER BY ID-------------------------------------------------------------------------

    @GetMapping("/invoice/item/master")
    public Map<String,Object> getInvoiceItemMasterById(@RequestParam UUID id,
                                                       @RequestHeader("Authorization") String token,
                                                       @RequestHeader("account_id") String accountId,
                                                       @RequestHeader("token_type") String tokenType)
    {
        logger.info("getInvoiceItemMasterById() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);

        logger.info("id: {}",id);
        Optional<InvoiceItemMaster> invoiceItemMaster = invoiceItemMasterService.getInvoiceItemMasterById(id);
        if (invoiceItemMaster.isPresent())
        {
            return httpResponse.getHttpResponse(invoiceItemMaster.get(), HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
        }
        return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------GET INVOICE ITEM MASTER BY NAME-------------------------------------------------------------------------

    @GetMapping("/invoice/item/master/name")
    public Map<String,Object> getInvoiceItemMasterByName(@RequestParam("name") String name,
                                                       @RequestHeader("Authorization") String token,
                                                       @RequestHeader("account_id") String accountId,
                                                       @RequestHeader("token_type") String tokenType) {
        logger.info("getInvoiceItemMasterByName() called");
        logger.info("token: {}", token);
        logger.info("admin_id: {}", accountId);
        logger.info("token_type: {}", tokenType);

        logger.info("name: {}", name);
        List<InvoiceItemMaster> invoiceItemMaster = invoiceItemMasterService.getAllInvoiceItemMasterByName(name.toLowerCase());
        if (invoiceItemMaster.size() > 0) {
            return httpResponse.getHttpResponse(invoiceItemMaster, HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
        } else {
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);

        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------ADD INVOICE ITEM MASTER-------------------------------------------------------------------------
    @PostMapping("/invoice/item/master")
    public Map<String,Object> addInvoiceItemMaster(@RequestBody Map<String,Object> data,
                                                   @RequestHeader("Authorization") String token,
                                                   @RequestHeader("account_id") String accountId,
                                                   @RequestHeader("token_type") String tokenType)

    {

        logger.info("addInvoiceItemMaster() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("");

        String name = data.get("name").toString();
        String strUnitId = data.get("unit_id").toString();
        UUID unitId = UUID.fromString(strUnitId);
        String hsnCode = data.get("hsn_code").toString();
        String strRate = data.get("rate").toString();
        Double rate= Double.parseDouble(strRate);
        String strDiscountId = data.get("discount_id").toString();
        UUID discountId = UUID.fromString(strDiscountId);
        String strTaxId = data.get("tax_id").toString();
        UUID taxId = UUID.fromString(strTaxId);


        logger.info("name: {}",name);
        logger.info("unit_id: {}",unitId);
        logger.info("hsn_code: {}",hsnCode);
        logger.info("rate: {}",rate);
        logger.info("discount_id: {}",discountId);
        logger.info("tax_id: {}",taxId);



        if (name == null || name.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_NAME, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (unitId == null || unitId.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_UNIT_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (hsnCode == null || hsnCode.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_HSN_CODE, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (rate == null || rate.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_RATE, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (discountId == null || discountId.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_DISCOUNT_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (taxId == null || taxId.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_TAX_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        for(InvoiceItemMaster invoiceItemMaster:invoiceItemMasterService.getAllInvoiceItemMaster())
        {
            if(invoiceItemMaster.getName().equalsIgnoreCase(name))
            {
                return httpResponse.getHttpResponse(null,"Invoice item name "+HttpConstant.ALREADY_EXIST,HttpConstant.BAD_REQUEST_STATUS_CODE);
            }
        }

        UUID id = UUID.randomUUID();
       InvoiceItemMaster invoiceItemMaster = new InvoiceItemMaster();
       invoiceItemMaster.setId(id);
       if(Validator.checkName(name)) {
           invoiceItemMaster.setName(name);
       }
        else
        {
            return httpResponse.getHttpResponse(null,HttpConstant.INVALID_NAME,HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        invoiceItemMaster.setUnitId(unitId);
        invoiceItemMaster.setHsnCode(hsnCode);
        invoiceItemMaster.setRate(rate);
        invoiceItemMaster.setTaxId(taxId);
        invoiceItemMaster.setDiscountId(discountId);
        invoiceItemMaster.setCreationTs(System.currentTimeMillis()/1000);
        invoiceItemMaster.setModifiedTs(0);
        invoiceItemMasterService.addInvoiceItemMaster(invoiceItemMaster);
        return httpResponse.getHttpResponse(invoiceItemMaster,HttpConstant.DATA_CREATED,HttpConstant.SUCCESS_STATUS_CODE);

    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------UPDATE INVOICE ITEM MASTER------------------------------------------------------------------------
    @PutMapping("/invoice/item/master")
    public Map<String,Object> updateInvoiceItemMaster(@RequestBody Map<String,Object> data,
                                                               @RequestHeader("Authorization") String token,
                                                               @RequestHeader("account_id") String accountId,
                                                               @RequestHeader("token_type") String tokenType) {
        logger.info("updateInvoiceItemMaster() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("");
        UUID id = UUID.fromString(data.get("id").toString());
        String name = data.get("name").toString();
        String strUnitId = data.get("unit_id").toString();
        UUID unitId = UUID.fromString(strUnitId);
        String hsnCode = data.get("hsn_code").toString();
        String strRate = data.get("rate").toString();
        Double rate= Double.parseDouble(strRate);
        String strDiscountId = data.get("discount_id").toString();
        UUID discountId = UUID.fromString(strDiscountId);
        String strTaxId = data.get("tax_id").toString();
        UUID taxId = UUID.fromString(strTaxId);


        logger.info("id: {}",id);
        logger.info("name: {}",name);
        logger.info("unit_id: {}",unitId);
        logger.info("hsn_code: {}",hsnCode);
        logger.info("rate: {}",rate);
        logger.info("discount_id: {}",discountId);
        logger.info("tax_id: {}",taxId);

        if (id==null || id.equals(""))
        {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if (name==null || name.equals(""))
        {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_NAME, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if (unitId == null || unitId.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_UNIT_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (hsnCode == null || hsnCode.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_HSN_CODE, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (rate == null || rate.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_RATE, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (discountId == null || discountId.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_DISCOUNT_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (taxId == null || taxId.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_TAX_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        Optional<InvoiceItemMaster> invoiceItemMaster = invoiceItemMasterService.getInvoiceItemMasterById(id);

        Map<String,Object> invoiceItemMasterData = new HashMap<>();
        if(invoiceItemMaster.isPresent())
        {
            if(!name.equalsIgnoreCase(invoiceItemMaster.get().getName()))
            {
                invoiceItemMasterData.put("name",name);
            }
            else
            {
                invoiceItemMasterData.put("name",invoiceItemMaster.get().getName());
            }
            if(!unitId.equals(invoiceItemMaster.get().getUnitId()))
            {
                invoiceItemMasterData.put("unitId",unitId);
            }
            else
            {
                invoiceItemMasterData.put("unitId",invoiceItemMaster.get().getUnitId());
            }
            if(!hsnCode.equalsIgnoreCase(invoiceItemMaster.get().getHsnCode()))
            {
                invoiceItemMasterData.put("hsnCode",hsnCode);
            }
            else
            {
                invoiceItemMasterData.put("hsnCode",invoiceItemMaster.get().getHsnCode());
            }
            if(!rate.equals(invoiceItemMaster.get().getRate()))
            {
                invoiceItemMasterData.put("rate",rate);
            }
            else
            {
                invoiceItemMasterData.put("rate",invoiceItemMaster.get().getRate());
            }
            if(!taxId.equals(invoiceItemMaster.get().getTaxId()))
            {
                invoiceItemMasterData.put("taxId",taxId);
            }
            else
            {
                invoiceItemMasterData.put("taxId",invoiceItemMaster.get().getTaxId());
            }
            if(!discountId.equals(invoiceItemMaster.get().getDiscountId()))
            {
                invoiceItemMasterData.put("discountId",discountId);
            }
            else
            {
                invoiceItemMasterData.put("discountId",invoiceItemMaster.get().getDiscountId());
            }
            invoiceItemMasterData.put("creationTs",invoiceItemMaster.get().getCreationTs());
            invoiceItemMasterData.put("modifiedTs", System.currentTimeMillis() / 1000);
            InvoiceItemMaster result = invoiceItemMasterService.updateInvoiceItemMaster(invoiceItemMasterData,id);
            return httpResponse.getHttpResponse(result, HttpConstant.DATA_UPDATED, HttpConstant.SUCCESS_STATUS_CODE);
        }


        return httpResponse.getHttpResponse(null,HttpConstant.DATA_NOT_FOUND, HttpConstant.BAD_REQUEST_STATUS_CODE);

    }


    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------DELETE INVOICE ITEM MASTER-------------------------------------------------------------------------

    @DeleteMapping("/invoice/item/master")
    public Map<String, Object> deleteInvoiceItemMaster(@RequestParam (value = "id") UUID id,
                                                       @RequestHeader("Authorization") String token,
                                                       @RequestHeader("account_id") String accountId,
                                                       @RequestHeader("token_type") String tokenType) {
        logger.info("deleteInvoiceItemMaster() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("id: {}",id);
        Optional<InvoiceItemMaster> invoiceItemMaster =  invoiceItemMasterService.getInvoiceItemMasterById(id);
        if (invoiceItemMaster.isPresent()) {
            invoiceItemMasterService.deleteInvoiceItemMaster(id);
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_DELETE, HttpConstant.SUCCESS_STATUS_CODE);
        }
        return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
    }


}


