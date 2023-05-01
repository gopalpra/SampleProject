package com.invoice.controller;

import com.invoice.entity.InvoiceSequential;
import com.invoice.service.InvoiceSequentialService;
import com.invoice.util.HTTPResponse;
import com.invoice.util.HttpConstant;
import com.invoice.util.RestTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class InvoiceSequentialController
{
    public static Logger logger = LoggerFactory.getLogger(InvoiceSequentialController.class);

    @Autowired
    private InvoiceSequentialService invoiceSequentialService;
    @Autowired
    private HTTPResponse httpResponse;

    @Autowired
    private RestTemplateUtil restTemplateUtil;


    @GetMapping("/sequential")
    public String index() {
        logger.info("Invoice sequential service get started");
        return "Invoice sequential service get started";
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------GET INVOICE SEQUENTIAL LIST------------------------------------------------------------------------
    @GetMapping("/invoice/sequential/list")
    public Map<String, Object> getInvoiceSequentialList(@RequestHeader("Authorization") String token,
                                                        @RequestHeader("account_id") String accountId,
                                                        @RequestHeader("token_type") String tokenType)
    {

        logger.info("getInvoiceSequentialList() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("  ");
        List<InvoiceSequential> invoiceSequential = invoiceSequentialService.getAllInvoiceSequential();
        if (invoiceSequential.size() > 0) {
            return httpResponse.getHttpResponse(invoiceSequential, HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
        } else {
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------GET INVOICE SEQUENTIAL BY ID-------------------------------------------------------------------------

    @GetMapping("/invoice/sequential")
    public Map<String,Object> getInvoiceSequentialById(@RequestParam UUID id,
                                                       @RequestHeader("Authorization") String token,
                                                       @RequestHeader("account_id") String accountId,
                                                       @RequestHeader("token_type") String tokenType)
    {
        logger.info("getInvoiceSequentialById() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);

        logger.info("id: {}",id);
        Optional<InvoiceSequential> invoiceSequential = invoiceSequentialService.getInvoiceSequentialById(id);
        if (invoiceSequential.isPresent())
        {
            return httpResponse.getHttpResponse(invoiceSequential.get(), HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
        }
        return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------GET INVOICE SEQUENTIAL BY ORG ID-------------------------------------------------------------------------

    @GetMapping("/invoice/sequential/orgId")
    public Map<String,Object> getInvoiceSequentialByOrgId(@RequestParam(value = "org_id") UUID orgId,
                                                          @RequestHeader("Authorization") String token,
                                                          @RequestHeader("account_id") String accountId,
                                                          @RequestHeader("token_type") String tokenType)
    {
        logger.info("getInvoiceSequentialByOrgId() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);

        logger.info("id: {}",orgId);
        Optional<InvoiceSequential> invoiceSequential = invoiceSequentialService.getInvoiceSequentialByOrgId(orgId);
        if (invoiceSequential.isPresent())
        {
            return httpResponse.getHttpResponse(invoiceSequential.get(), HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
        }
        return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------ADD INVOICE SEQUENTIAL-------------------------------------------------------------------------
    @PostMapping( value ="/invoice/sequential",produces = "application/json")
    public Map<String,Object> addInvoiceSequential(@RequestParam(value = "id",required = false) String id,
                                                   @RequestParam("org_id") String orgId,
                                                   @RequestParam("branch_id") String branchId,
                                                   @RequestParam("seq_start_no") Long seqStartNo,
                                                   @RequestParam("seq_text") String seqText,
                                                   @RequestParam("seq_year") String seqYear,
                                                   @RequestParam("invoice_no_format") String invoiceNoFormat,
                                                   @RequestParam(value = "is_deleted",required = false) Boolean isDeleted,
                                                   @RequestHeader("Authorization") String token,
                                                   @RequestHeader("account_id") String accountId,
                                                   @RequestHeader("token_type") String tokenType)

    {

        logger.info("addInvoiceSequential() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("");
        logger.info("orgId: {}",orgId);
        logger.info("branchId: {}",branchId);
        logger.info("seqStartNo: {}",seqStartNo);
        logger.info("seqText: {}",seqText);
        logger.info("invoiceNoFormat: {}",invoiceNoFormat);
        if (orgId==null || orgId.equals(""))
        {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_ORG_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (seqStartNo == null || seqStartNo.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_SEQUENTIAL_START_NO, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if (seqText == null || seqText.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_SEQ_TEXT, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if (seqYear == null || seqYear.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_SEQ_YEAR, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if(!(seqYear.equalsIgnoreCase("YYYY")||seqYear.equalsIgnoreCase("YY")))
        {
            return httpResponse.getHttpResponse(null, HttpConstant.INVALID_SEQ_YEAR, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if (invoiceNoFormat == null || invoiceNoFormat.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_INVOICE_NUMBER_FORMAT, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
//        for(InvoiceSequential invoiceSequential:invoiceSequentialService.getAllInvoiceSequential())
//        {
////            if(invoiceSequential.getInvoiceNoFormat().equalsIgnoreCase(invoiceNoFormat))
////            {
////                return httpResponse.getHttpResponse(null,"Invoice number format "+HttpConstant.ALREADY_EXIST,HttpConstant.BAD_REQUEST_STATUS_CODE);
////            }
//            if(invoiceSequential.getSeqStartNo()==seqStartNo)
//            {
//                return httpResponse.getHttpResponse(null,"Sequential start number "+HttpConstant.ALREADY_EXIST,HttpConstant.BAD_REQUEST_STATUS_CODE);
//            }
//        }

        UUID idUuid = UUID.randomUUID();
        InvoiceSequential invoiceSequential = new InvoiceSequential();
        invoiceSequential.setId(idUuid);
        invoiceSequential.setOrgId(UUID.fromString(orgId));
        if (branchId == null || branchId.equals("")) {
            invoiceSequential.setBranchId(null);
        }
        else {
            invoiceSequential.setBranchId(UUID.fromString(branchId));
        }
        if (seqStartNo.equals(null)) {
            invoiceSequential.setSeqStartNo(Long.valueOf(00000001));
            invoiceSequential.setSeqCurrentNo(Long.valueOf(00000001));
        } else {
            invoiceSequential.setSeqStartNo(seqStartNo);
            invoiceSequential.setSeqCurrentNo(seqStartNo);
        }
        if (seqText.equalsIgnoreCase(null)) {
            invoiceSequential.setSeqText("INV");
        } else {
            invoiceSequential.setSeqText(seqText);
        }

        String[] array = invoiceNoFormat.split("/");
        System.out.println(array[0] + "   " + array[1] + "   " + array[2]);

        if (array[0].equalsIgnoreCase("ST") && (array[1]).equalsIgnoreCase("SQ") && array[2].equalsIgnoreCase("FY") ||
                array[0].equalsIgnoreCase("ST") && (array[1]).equalsIgnoreCase("FY") && array[2].equalsIgnoreCase("SQ") ||
                array[0].equalsIgnoreCase("FY") && (array[1]).equalsIgnoreCase("ST") && array[2].equalsIgnoreCase("SQ") ||
                array[0].equalsIgnoreCase("FY") && (array[1]).equalsIgnoreCase("SQ") && array[2].equalsIgnoreCase("ST") ||
                array[0].equalsIgnoreCase("SQ") && (array[1]).equalsIgnoreCase("ST") && array[2].equalsIgnoreCase("FY") ||
                array[0].equalsIgnoreCase("SQ") && (array[1]).equalsIgnoreCase("FY") && array[2].equalsIgnoreCase("ST")) {

            invoiceSequential.setInvoiceNoFormat(invoiceNoFormat);
        } else {
            return httpResponse.getHttpResponse(null, HttpConstant.INVALID_INVOICE_NUMBER_FORMAT, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        invoiceSequential.setSeqYear(seqYear);
        invoiceSequential.setCreationTs(System.currentTimeMillis() / 1000);
        invoiceSequential.setDeleted(true);
        invoiceSequential.setModifiedTs(0);
        invoiceSequentialService.addInvoiceSequential(invoiceSequential);
        return httpResponse.getHttpResponse(invoiceSequential, HttpConstant.DATA_CREATED, HttpConstant.SUCCESS_STATUS_CODE);



    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------UPDATE INVOICE SEQUENTIAL------------------------------------------------------------------------
    @PostMapping( value ="/invoice/edit/sequential",produces = "application/json")
    public Map<String,Object> updateInvoiceSequential(@RequestParam("org_id") String orgId,
                                                      @RequestParam("branch_id") String branchId,
                                                      @RequestParam("seq_start_no") Long seqStartNo,
                                                      @RequestParam("seq_text") String seqText,
                                                      @RequestParam("seq_year") String seqYear,
                                                      @RequestParam("invoice_no_format") String invoiceNoFormat,
                                                      @RequestHeader("Authorization") String token,
                                                      @RequestHeader("account_id") String accountId,
                                                      @RequestHeader("token_type") String tokenType) {
        logger.info("updateInvoiceSequential() called");
        logger.info("token: {}", token);
        logger.info("admin_id: {}", accountId);
        logger.info("token_type: {}", tokenType);
        logger.info("");
        logger.info("orgId: {}", orgId);
        logger.info("branchId: {}", branchId);
        logger.info("seqStartNo: {}", seqStartNo);
        logger.info("seqText: {}", seqText);
        logger.info("invoiceNoFormat: {}", invoiceNoFormat);
        if (orgId == null || orgId.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_ORG_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if (seqStartNo == null || seqStartNo.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_SEQUENTIAL_START_NO, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if (seqYear == null || seqYear.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_SEQ_YEAR, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if(!(seqYear.equalsIgnoreCase("YYYY")||seqYear.equalsIgnoreCase("YY")))
        {
            return httpResponse.getHttpResponse(null, HttpConstant.INVALID_SEQ_YEAR, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if (seqText == null || seqText.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_SEQ_TEXT, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if (invoiceNoFormat == null || invoiceNoFormat.equals("")) {
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_INVOICE_NUMBER_FORMAT, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        Map<String, Object> seqData = new HashMap<>();
        Optional<InvoiceSequential> invoiceSequentialData = invoiceSequentialService.getInvoiceSequentialByOrgId(UUID.fromString(orgId));
        System.out.println("-------------------------------------");
        System.out.println("invoice seq data");
        System.out.println(invoiceSequentialData);
        System.out.println("-------------------------------------");
        System.out.println("-------------------------------------");
        System.out.println("-------------------------------------");
        UUID id = invoiceSequentialData.get().getId();
        if (invoiceSequentialData.isPresent())
        {

            if (!orgId.equals(invoiceSequentialData.get().getOrgId()))
            {
                seqData.put("orgId", UUID.fromString(orgId));
            } else {
                seqData.put("orgId", invoiceSequentialData.get().getOrgId());
            }
            if (branchId == null || branchId.equals("")) {
                seqData.put("branchId", null);
            }
            else
            {
                if (!branchId.equals(invoiceSequentialData.get().getBranchId())) {
                    seqData.put("branchId", UUID.fromString(branchId));

                } else {
                    seqData.put("branchId", invoiceSequentialData.get().getBranchId());
                }

            }



            if (invoiceSequentialData.get().getSeqStartNo() != seqStartNo) {
                seqData.put("seqStartNo", seqStartNo);
            } else {
                seqData.put("seqStartNo", invoiceSequentialData.get().getSeqStartNo());
            }
            if (!invoiceSequentialData.get().getSeqText().equalsIgnoreCase(seqText)) {
                seqData.put("seqText", seqText);
            } else {
                seqData.put("seqText", invoiceSequentialData.get().getSeqText());
            }
            if (!invoiceSequentialData.get().getSeqYear().equalsIgnoreCase(seqYear)) {
                seqData.put("seqYear", seqYear);
            } else {
                seqData.put("seqYear", invoiceSequentialData.get().getSeqYear());
            }
            if (!invoiceSequentialData.get().getInvoiceNoFormat().equalsIgnoreCase(invoiceNoFormat)) {
                seqData.put("invoiceNoFormat", invoiceNoFormat);
            } else {
                seqData.put("invoiceNoFormat", invoiceSequentialData.get().getInvoiceNoFormat());
            }

            seqData.put("isDeleted", invoiceSequentialData.get().isDeleted());

            seqData.put("creationTs", invoiceSequentialData.get().getCreationTs());
            seqData.put("modifiedTs", System.currentTimeMillis()/ 1000);
            System.out.println("seq data");
            System.out.println(seqData);
            InvoiceSequential result = invoiceSequentialService.updateInvoiceSequential(seqData, id);

            System.out.println("--------------------------------------");
            System.out.println("result");
            System.out.println(result);
            System.out.println("--------------------------------------");
            return httpResponse.getHttpResponse(result, HttpConstant.DATA_UPDATED, HttpConstant.SUCCESS_STATUS_CODE);
        }

        else{
            return httpResponse.getHttpResponse(null,HttpConstant.DATA_NOT_FOUND, HttpConstant.BAD_REQUEST_STATUS_CODE);

        }

    }


    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------DELETE INVOICE SEQUENTIAL-------------------------------------------------------------------------

    @DeleteMapping("/invoice/sequential")
    public Map<String, Object> deleteInvoiceSequential(@RequestParam (value = "id") UUID id,
                                                       @RequestHeader("Authorization") String token,
                                                       @RequestHeader("account_id") String accountId,
                                                       @RequestHeader("token_type") String tokenType) {
        logger.info("deleteInvoiceSequential() called");
        logger.info("token: {}",token);
        logger.info("admin_id: {}",accountId);
        logger.info("token_type: {}",tokenType);
        logger.info("id: {}",id);
        Optional<InvoiceSequential> invoiceSequential = invoiceSequentialService.getInvoiceSequentialById(id);
        if (invoiceSequential.isPresent()) {
            invoiceSequentialService.deleteInvoiceSequential(id);
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_DELETE, HttpConstant.SUCCESS_STATUS_CODE);
        }
        return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------UPDATE INVOICE SEQUENTIAL-------------------------------------------------------------------------

    @PostMapping("/invoice/sequential/currentno")
    public Map<String, Object> updateInvoiceSequentialCurrentNumber(@RequestParam (value = "id") UUID id,
                                                                    @RequestParam (value = "seq_current_no") Long seqCurrentNo ,
                                                                    @RequestHeader("Authorization") String token,
                                                                    @RequestHeader("account_id") String accountId,
                                                                    @RequestHeader("token_type") String tokenType) {
        logger.info(" updateInvoiceSequentialCurrentNumber() called");
        logger.info("token: {}", token);
        logger.info("admin_id: {}", accountId);
        logger.info("token_type: {}", tokenType);
        logger.info("id: {}", id);
        logger.info("seq_current_no: {}", seqCurrentNo);
        try {
            if (id == null || id.equals("")) {
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            if (seqCurrentNo.equals(null) || seqCurrentNo.equals(""))
            {
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_SEQUENTIAL_CURRENT_NO, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }
            Optional<InvoiceSequential> invoiceSequential = invoiceSequentialService.getInvoiceSequentialById(id);
            if (invoiceSequential.isPresent()) {
                invoiceSequentialService.updateInvoiceSequentialCurrentNo(seqCurrentNo, id);
                return httpResponse.getHttpResponse(null, HttpConstant.DATA_UPDATED, HttpConstant.SUCCESS_STATUS_CODE);
            }
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
        } catch (Exception e) {
            return httpResponse.getHttpResponse(null, HttpConstant.INTERNAL_SERVER_ERROR, HttpConstant.INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }

}

