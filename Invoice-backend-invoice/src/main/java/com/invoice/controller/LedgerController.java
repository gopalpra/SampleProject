package com.invoice.controller;

import com.invoice.entity.Ledger;
import com.invoice.repository.LedgerRepository;
import com.invoice.service.LedgerService;
import com.invoice.util.HTTPResponse;
import com.invoice.util.HttpConstant;
import com.invoice.util.RestTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * @author chhavi priya tanwar
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/ledger")

public class LedgerController {

    @Autowired
    private LedgerService ledgerService;

    @Autowired
    private HTTPResponse httpResponse;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    public static Logger logger = LoggerFactory.getLogger(LedgerController.class);

    @GetMapping("/index")
    public String index() {
        logger.info("   ");
        logger.info("Ledger");
        logger.info("  ");
        return "Ledger";
    }



//----------------------------------------Get------------------------------------------------------------------------------

    //GetAll ledger record
    @GetMapping
    public Map<String, Object> getAllLedger(){
        List<Ledger> ledgerList = ledgerService.getAllLedger();
        logger.info(ledgerList.toString());
        if (ledgerList.size()>0){
            return httpResponse.getHttpResponse(ledgerList, HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
        }else {
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
        }
    }



//----------------------------------------Get By Id -----------------------------------------------------------------------


    //GetById ledger record
    @GetMapping("/getById")
    public Map<String,Object> getLedgerById(@RequestParam(value = "id") String id)
    {

        try {
            UUID uuid = UUID.fromString(id);
            logger.info("id: {}", uuid);
            Optional<Ledger> ledgerList = ledgerService.getLedgerById(uuid);

            if (ledgerList.isPresent()) {
                return httpResponse.getHttpResponse(ledgerList.get(), HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
            } else {
                return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
            }

        } catch (IllegalArgumentException e) {
            return httpResponse.getHttpResponse((Object) null, "Please provide a valid Id", (short) HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            return httpResponse.getHttpResponse(null, HttpConstant.INTERNAL_SERVER_ERROR, HttpConstant.INTERNAL_SERVER_ERROR_STATUS_CODE);
        }

    }


//----------------------------------------Create---------------------------------------------------------------------------

    //Create ledger record
    @PostMapping
    public Map<String,Object> createLedger(@RequestBody() Map<String,Object> data,
                                           @RequestHeader("Authorization") String token,
                                           @RequestHeader("account_id") String accountId,
                                           @RequestHeader("token_type") String tokenType){


        //get value from map
        Double amount = (Double) data.get("amount");
        String balanceType = (String) data.get("balance_type");
        String type = (String) data.get("type");

        UUID org_id;
        UUID invoice_id;
        UUID customer_id;
        UUID branch_id;


        //print map value
        logger.info("");
        logger.info("----------Post----------------------------");
        logger.info("amount : "+amount);
        logger.info("balanceType : "+balanceType);
        logger.info("type : "+type);
        logger.info("-----------------------------------------");
        logger.info("");

        //apply conditions
        if(amount == null || amount.equals("")){
            logger.error("Amount should not be empty");
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_AMOUNT, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if(balanceType == null || balanceType.equals("")){
            logger.error("BalanceType should not be empty");
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_BALANCE_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        if(type == null || type.equals("")){
            logger.error("Type should not be empty");
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        //get value from map
        String org_id_str = (String) data.get("org_id");
        String invoice_id_str = (String) data.get("invoice_id");
        String customer_id_str = (String) data.get("customer_id");
        String branch_id_str = (String) data.get("branch_id");

        //check for null or empty UUID values
        if(org_id_str == null || org_id_str.isEmpty()){
            logger.error("org_id should not be empty");
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_ORG_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if(invoice_id_str == null || invoice_id_str.isEmpty()){
            logger.error("invoice_id should not be empty");
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_INVOICE_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if(customer_id_str == null || customer_id_str.isEmpty()){
            logger.error("customer_id should not be empty");
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_CUSTOMER_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        if(branch_id_str == null || branch_id_str.isEmpty()){
            logger.error("branch_id should not be empty");
            return httpResponse.getHttpResponse(null, HttpConstant.BLANK_BRANCH_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        try {
            org_id = UUID.fromString(data.get("org_id").toString());
            invoice_id = UUID.fromString(data.get("invoice_id").toString());
            customer_id = UUID.fromString(data.get("customer_id").toString());
            branch_id = UUID.fromString(data.get("branch_id").toString());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid UUID: " + e.getMessage());
            return httpResponse.getHttpResponse(null, HttpConstant.INVALID_UUID, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }






        // check if balanceType is credit or debit
        if (!balanceType.equalsIgnoreCase("credit") && !balanceType.equalsIgnoreCase("debit")) {
            logger.error("BalanceType must be either 'credit' or 'debit'");
            return httpResponse.getHttpResponse(null, HttpConstant.INVALID_BALANCE_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        // check if type is credit or debit
        if (!type.equalsIgnoreCase("credit") && !type.equalsIgnoreCase("debit")) {
            logger.error("Type must be either 'credit' or 'debit'");
            return httpResponse.getHttpResponse(null, HttpConstant.INVALID_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }

        //Auto Generate
        UUID id = UUID.randomUUID();

        //get current time
        long creation_ts = System.currentTimeMillis() /1000;

        // get the latest ledger record
        Ledger latestLedger = ledgerService.getLatestLedger();
        Double balance = (latestLedger != null) ? latestLedger.getBalance() : 0.0;

        if("credit".equals(balanceType)){
            balance += amount;
            System.out.println("");
            System.out.println("------credit balance----------");
            System.out.println("balance : "+balance);
            System.out.println("");
        }
        if ("debit".equals(balanceType)){
            balance -= amount;
            System.out.println("");
            System.out.println("-----debit balance-------------");
            System.out.println("balance : "+balance);
            System.out.println("");
        }



        //----------------------Use of RestTemplateUtil for organization_id-------------------------------------
        Ledger customerProfile1= new Ledger();
        customerProfile1.setId(id);
        Map<String,Object> map = restTemplateUtil.getOrganizationData(UUID.fromString(org_id_str),accountId,token,tokenType);
//        Optional<Object> listData =  map.get("data");
        String orgName = null;
        if(map.get("data")!=null) {
            Map<String, Object> data1 = (Map<String, Object>) map.get("data");
            if (!(data1.get("id").equals(org_id_str))) {
                customerProfile1.setOrgId(UUID.fromString(org_id_str));
            }

//            if (customerType.equalsIgnoreCase(Constants.CustomerType.ORGANIZATION)) {
//                orgName = (String) data1.get("name");
//                System.out.println("orgname: " + orgName);
//            }

        }else {
            return httpResponse.getHttpResponse(null, HttpConstant.ORG_NOT_FOUND, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        //------------------------end of RestTemplateUtil-----------------------------------------------



        // create new ledger record
        Ledger newLedger = new Ledger(id,org_id,invoice_id,customer_id,branch_id,amount,balanceType,type,balance,creation_ts,0);
        ledgerService.createLedger(newLedger);


        return httpResponse.getHttpResponse(newLedger, HttpConstant.DATA_CREATED, HttpConstant.SUCCESS_STATUS_CODE);

    }


//----------------------------------------Update---------------------------------------------------------------------------

    //UpdateById ledger record
    @PutMapping("/put")
    public Map<String,Object> updateLedger(@RequestBody() Map<String,Object> data){

        try {

            logger.info("UPDATE LEDGER");

            //get value from map
            UUID id = UUID.fromString(data.get("id").toString());
            logger.info("id : {}", id);
            Double amount = (Double) data.get("amount");
            logger.info("amount : {}", amount);
            String balanceType = (String) data.get("balance_type");
            logger.info("balanceType : {}", balanceType);
            String type = (String) data.get("type");
            logger.info("type : {}", type);
            Double balance = (Double) data.get("balance");
            logger.info("balance : {}", balance);


            //apply conditions
//            if (data.containsKey("id")) {
//                logger.error("Id should not be empty");
//                return httpResponse.getHttpResponse(null, HttpConstants.BLANK_ID, HttpConstants.BAD_REQUEST_STATUS_CODE);
//            }


            if (id == null || id.equals(""))
            {
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            if (amount == null || amount.equals("")) {
                logger.error("Amount should not be empty");
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_AMOUNT, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            if (balanceType == null || balanceType.equals("")) {
                logger.error("BalanceType should not be empty");
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_BALANCE_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            if (type == null || type.equals("")) {
                logger.error("Type should not be empty");
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            if (balance == null || balance.equals("")) {
                logger.error("Balance should not be empty");
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_BALANCE, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            // check if ledger record with given id exists in database
            Optional<Ledger> optionalLedger = ledgerService.getLedgerById(id);
            System.out.println("optionalLedger : "+optionalLedger);
            if (optionalLedger.isPresent()) {
//                logger.error("Ledger record with given id does not exist");
//                return httpResponse.getHttpResponse(null, HttpConstants.DATA_NOT_FOUND, HttpConstants.NOT_FOUND_STATUS_CODE);


                // create map containing fields to be updated
                Map<String, Object> updateMap = new HashMap<>();

                //id
//                if(id.equals(optionalLedger.get().getId())){
//                    updateMap.put("id",id);
//                }




                updateMap.put("amount", amount);
                updateMap.put("balanceType", balanceType);
                updateMap.put("type", type);
                updateMap.put("balance", balance);
                updateMap.put("modifiedTs", System.currentTimeMillis() / 1000);

                System.out.println("updateMap : "+updateMap);

                // update ledger record with given id
                Ledger updatedLedger = ledgerService.updateLedger(updateMap,id);
                System.out.println("updatedLedger : "+updatedLedger);

                return httpResponse.getHttpResponse(updatedLedger, HttpConstant.DATA_UPDATED, HttpConstant.SUCCESS_STATUS_CODE);
            }
            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.BAD_REQUEST_STATUS_CODE);
        }
        catch (Exception ex)
        {
            return httpResponse.getHttpResponse(null, HttpConstant.INTERNAL_SERVER_ERROR, HttpConstant.INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }



//----------------------------------------Delete------------------------------------------------------------------------------

    //DeleteById ledger record
    @DeleteMapping
    public Map<String,Object> deleteLedgerById(@RequestParam(value = "id") String id)
    {

        try {
            UUID uuid = UUID.fromString(id);
            logger.info("id: {}", uuid);
            Optional<Ledger> ledgerList = ledgerService.getLedgerById(uuid);

            if (ledgerList.isPresent()) {
                ledgerService.deleteLedger(UUID.fromString(id));
                return httpResponse.getHttpResponse(null, HttpConstant.DATA_DELETE, HttpConstant.SUCCESS_STATUS_CODE);
            } else {
                return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
            }

        } catch (IllegalArgumentException e) {
            return httpResponse.getHttpResponse((Object) null, "Please provide a valid Id", (short) HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            return httpResponse.getHttpResponse(null, HttpConstant.INTERNAL_SERVER_ERROR, HttpConstant.INTERNAL_SERVER_ERROR_STATUS_CODE);
        }

    }


//------------------------------Custom Update------------------------------------------------------------------------------

    @PutMapping("/custom_update")
    public Map<String, Object> customUpdateLedger(@RequestBody () Map<String,Object> data) {

        try {

            logger.info("updateLedger() called");
            logger.info("data: {}", data);
            logger.info("");

            UUID id = UUID.fromString(data.get("id").toString());
            logger.info("id : {}", id);

            Double amount = Double.valueOf(data.get("amount").toString());
            logger.info("amount : {}", amount);

            String balanceType = (String) data.get("balance_type");
            logger.info("balanceType : {}", balanceType);

            String type = (String) data.get("type");
            logger.info("type : {}", type);

            Double balance = Double.valueOf(data.get("balance").toString());
            logger.info("balance : {}", balance);


            if (id == null || id.equals("")) {
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_ID, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            if (amount == null || amount.equals("")) {
                logger.error("Amount should not be empty");
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_AMOUNT, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            if (balanceType == null || balanceType.equals("")) {
                logger.error("BalanceType should not be empty");
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_BALANCE_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            if (type == null || type.equals("")) {
                logger.error("Type should not be empty");
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }

            if (balance == null || balance.equals("")) {
                logger.error("Balance should not be empty");
                return httpResponse.getHttpResponse(null, HttpConstant.BLANK_BALANCE, HttpConstant.BAD_REQUEST_STATUS_CODE);
            }


            Optional<Ledger> ledgerOptionalData = ledgerService.getLedgerById(id);


            Map<String, Object> ledgerMapData = new HashMap<>();
            if (ledgerOptionalData.isPresent()) {
                //No Validation for Map Value

                if (id.equals(ledgerOptionalData.get().getId())) {
                    ledgerMapData.put("id", id);
                    System.out.println("");
                    System.out.println("-------id---------");
                    System.out.println("id : " + id);
                    System.out.println("------------------");
                    System.out.println("");
                }

                // check if balanceType is credit or debit
                if (!balanceType.equalsIgnoreCase("credit") && !balanceType.equalsIgnoreCase("debit")) {
                    logger.error("BalanceType must be either 'credit' or 'debit'");
                    return httpResponse.getHttpResponse(null, HttpConstant.INVALID_BALANCE_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
                }

                // check if type is credit or debit
                if (!type.equalsIgnoreCase("credit") && !type.equalsIgnoreCase("debit")) {
                    logger.error("Type must be either 'credit' or 'debit'");
                    return httpResponse.getHttpResponse(null, HttpConstant.INVALID_TYPE, HttpConstant.BAD_REQUEST_STATUS_CODE);
                }

                ledgerMapData.put("amount", amount);
                ledgerMapData.put("balanceType", balanceType);
                ledgerMapData.put("type", type);
                ledgerMapData.put("balance", balance);
                ledgerMapData.put("modifiedTs", System.currentTimeMillis() / 1000);

                //Call Update Function from Service
                Ledger result = ledgerService.updateLedger(ledgerMapData, id);
                return httpResponse.getHttpResponse(result, HttpConstant.DATA_UPDATED, HttpConstant.SUCCESS_STATUS_CODE);
            }

            return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.BAD_REQUEST_STATUS_CODE);

        } catch (IllegalArgumentException e) {
            return httpResponse.getHttpResponse((Object) null, "Please provide a valid Id", (short) HttpStatus.BAD_REQUEST.value());
        } catch (Exception ex) {
            return httpResponse.getHttpResponse(null, HttpConstant.INTERNAL_SERVER_ERROR, HttpConstant.INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }


    @GetMapping("/getByCustomerId")
    public Map<String,Object> getLedgersByCustomerId(@RequestParam(value = "customer_id") String customerId)
    {

        try {
            UUID uuid = UUID.fromString(customerId);
            logger.info("customerId: {}", uuid);
            List<Ledger> ledgerList = ledgerService.getLedgersByCustomerId(uuid);

            if (!ledgerList.isEmpty()) {
                return httpResponse.getHttpResponse(ledgerList, HttpConstant.SUCCESS, HttpConstant.SUCCESS_STATUS_CODE);
            } else {
                return httpResponse.getHttpResponse(null, HttpConstant.DATA_NOT_FOUND, HttpConstant.NOT_FOUND_STATUS_CODE);
            }

        } catch (IllegalArgumentException e) {
            return httpResponse.getHttpResponse((Object) null, "Please provide a valid customer Id", (short) HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            return httpResponse.getHttpResponse(null, HttpConstant.INTERNAL_SERVER_ERROR, HttpConstant.INTERNAL_SERVER_ERROR_STATUS_CODE);
        }

    }

    @GetMapping("/pagination")
    public ResponseEntity<Map<String, Object>> getLedgersByCustomerId(
            @RequestParam(value = "customer_id") String customerIdStr,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            UriComponentsBuilder uriBuilder) {

        try {
            // Parse the customer ID from the input string
            UUID customerId = UUID.fromString(customerIdStr);

            // Check if the customer ID is valid and exists in the database
            if (!ledgerService.isValidCustomerId(customerId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(httpResponse.getHttpResponse(null, "Invalid or unknown customer ID. Data not found.", HttpConstant.NOT_FOUND_STATUS_CODE));
            }

            long totalItems = ledgerService.getTotalLedgerCountByCustomerId(customerId);
            if (size > totalItems) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(httpResponse.getHttpResponse(null, "No more data available for the requested page size.", HttpConstant.NOT_FOUND_STATUS_CODE));
            }
            Page<Ledger> ledgerPage = ledgerService.getLedgersByCustomerId(customerId, PageRequest.of(page, size));

            if (ledgerPage.hasContent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("data", ledgerPage.getContent());
                response.put("currentPage", page);
                response.put("totalItems", ledgerPage.getTotalElements());
                response.put("totalPages", ledgerPage.getTotalPages());

                // add next and previous page IDs to response body
                if (ledgerPage.hasNext()) {
                    response.put("next", page + 1);
                }
                if (ledgerPage.hasPrevious()) {
                    response.put("prev", page - 1);
                }

                return ResponseEntity.ok(response);
            } else {
                // check if page is out of bounds
                int totalPages = (int) Math.ceil((double) totalItems / size);

                if (page >= totalPages) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(httpResponse.getHttpResponse(null, "Invalid page number. Please enter a valid page number.", HttpConstant.NOT_FOUND_STATUS_CODE));
                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(httpResponse.getHttpResponse(null, "Requested page size is too large.", HttpConstant.BAD_REQUEST_STATUS_CODE));
                }
            }

        } catch (IllegalArgumentException e) {
            // The input customer ID is not a valid UUID
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(httpResponse.getHttpResponse(null, "Invalid or missing customer ID.", HttpConstant.BAD_REQUEST_STATUS_CODE));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(httpResponse.getHttpResponse(null, HttpConstant.INTERNAL_SERVER_ERROR, HttpConstant.INTERNAL_SERVER_ERROR_STATUS_CODE));
        }
    }

}
