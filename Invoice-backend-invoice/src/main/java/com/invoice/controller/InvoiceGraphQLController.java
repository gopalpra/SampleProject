package com.invoice.controller;
import com.invoice.entity.Invoice;
import com.invoice.service.InvoiceService;
import com.invoice.util.HTTPResponse;
import com.invoice.util.HttpConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@CrossOrigin(origins = "*",allowedHeaders  = "*")
@Controller
public class InvoiceGraphQLController {
    public static Logger logger = LoggerFactory.getLogger(InvoiceController.class);
    @Autowired
    private InvoiceService invoiceService;
    /*
        @SchemaMapping(typeName = "Query",value = "allInvoiceItems")
        public List<InvoiceItem> allInvoiceItems() {
            return invoiceItemService.getAllInvoiceItems();
        }
        @QueryMapping
        public InvoiceItem invoiceItem(@Argument Integer id) {
            return invoiceItemService.getAllInvoiceItemById(id).get();
        }
    */
    @SchemaMapping(typeName = "Query",value = "allInvoices")
    public Page<Invoice> getInvoices(@Argument("page_size") Integer pageSize,
                                     @Argument("page") Integer page) {
        logger.info("getInvoices) called");
        logger.info("");
        logger.info("Page : {}", page);
        logger.info("Page size : {}", pageSize);
        Page<Invoice> invoices = invoiceService.getAllInvoices(page, pageSize);
        System.out.println("invoices: "+invoices.toList());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String authorization = attributes.getRequest().getHeader("Authorization");
        String tokenType = attributes.getRequest().getHeader("token_type");
        String accountIds = attributes.getRequest().getHeader("account_id");

        System.out.println("Attribute : "+attributes);

        HTTPResponse httpResponse = new HTTPResponse();
//        if(invoices.equals(null))
//        {
//            httpResponse.setData(null);
//            httpResponse.setMessage(HttpConstant.DATA_NOT_FOUND);
//            httpResponse.setStatus(HttpConstant.BAD_REQUEST_STATUS_CODE);
//            System.out.println("--------------------------------------");
//            System.out.println("httpResponse");
//            System.out.println(httpResponse);
//            System.out.println("--------------------------------------------");
//           return httpResponse.toString();
//        }
//       else
//        {
//            Map<String,Object> map = new HashMap<>();
//            map.put("content",invoices.getContent());
//            System.out.println("-------------------------------------------------");
//            System.out.println("content"+map);
//            System.out.println("-------------------------------------------------");
//            map.put("totalElements",invoices.getTotalElements());
//            System.out.println("-------------------------------------------------");
//            System.out.println("totalElements"+map);
//            System.out.println("-------------------------------------------------");
//            map.put("totalPages",invoices.getTotalPages());
//            System.out.println("-------------------------------------------------");
//            System.out.println("totalPages"+map);
//            System.out.println("-------------------------------------------------");
//            map.put("last",invoices.isLast());
//            System.out.println("-------------------------------------------------");
//            System.out.println("last"+map);
//            System.out.println("-------------------------------------------------");
//            System.out.println("-------------------------------------------------");
//            httpResponse.setData(map);
//            httpResponse.setMessage(HttpConstant.SUCCESS);
//            httpResponse.setStatus(HttpConstant.SUCCESS_STATUS_CODE);
//            System.out.println("--------------------------------------");
//            System.out.println("httpResponse");
//            System.out.println(httpResponse.toString());
//            System.out.println("--------------------------------------------");
//            return httpResponse.toString();
//        }
        return invoices;
    }
    @SchemaMapping(typeName = "Query",value = "findByCustomerId")
    public Page<Invoice> getInvoiceByCustomerId(@Argument("customer_id") String customerId,
                                                @Argument(name = "status",value = "") String status,
                                                @Argument("page_size") Integer pageSize,
                                                @Argument("page") Integer page) {
        logger.info("getInvoiceByCustomerId) called");
        logger.info("");
        logger.info("Page : {}", page);
        logger.info("Page size : {}", pageSize);
        logger.info("customer_id : {}", customerId);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String authorization = attributes.getRequest().getHeader("Authorization");
        String tokenType = attributes.getRequest().getHeader("token_type");
        String accountIds = attributes.getRequest().getHeader("account_id");

        System.out.println("Attribute : "+attributes);

        UUID id = UUID.fromString(customerId);
        Page<Invoice> invoices = null;
        if(status == null || status.equalsIgnoreCase(""))
        {
            invoices = invoiceService.getAllInvoicesByCustomerId(id,page, pageSize);
        }
        else
        {
            invoices = invoiceService.getAllInvoicesByCustomerIdAndStatus(id,status,page, pageSize);
        }
        System.out.println("invoices: "+invoices.getContent());
        return invoices;
    }
    @SchemaMapping(typeName = "Query",value = "findByOrganizationId")
    public Page<Invoice> getInvoiceByOrganizationId(@Argument("org_id") String orgId,
                                                    @Argument(name = "status",value = "") String status,
                                                    @Argument("page_size") Integer pageSize,
                                                    @Argument("page") Integer page) {
        logger.info("getInvoiceByOrganizationId) called");
        logger.info("");
        logger.info("Page : {}", page);
        logger.info("Page size : {}", pageSize);
        logger.info("orgId : {}", orgId);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String authorization = attributes.getRequest().getHeader("Authorization");
        String tokenType = attributes.getRequest().getHeader("token_type");
        String accountIds = attributes.getRequest().getHeader("account_id");

        System.out.println("Attribute : "+attributes);

        UUID id = UUID.fromString(orgId);
        Page<Invoice> invoices = null;
        if(status == null || status.equalsIgnoreCase("")) {
            invoices = invoiceService.getAllInvoicesByOrganizationId(id, page, pageSize);
        }
        else
        {
            invoices = invoiceService.getAllInvoicesByOrganizationIdAndStatus(id,status, page, pageSize);
        }
        System.out.println("invoices: "+invoices.getContent());
        return invoices;
    }
}