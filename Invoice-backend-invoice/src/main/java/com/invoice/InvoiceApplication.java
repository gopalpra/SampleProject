package com.invoice;

import com.invoice.dto.ListAPIRes;
import com.invoice.dto.ObjAPIRes;
import com.invoice.util.AdminServiceAPIClient;
import com.invoice.util.OrganizationServiceAPIClient;
import com.invoice.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication(scanBasePackages = "com.invoice")
@EnableFeignClients
public class InvoiceApplication {

    @Autowired
    AdminServiceAPIClient adminServiceAPIClient;
    @Autowired
    OrganizationServiceAPIClient organizationServiceAPIClient;

    private static AdminServiceAPIClient adminServiceAPIClient2;
    private static OrganizationServiceAPIClient organizationServiceAPIClient2;

    @PostConstruct
    public void init() {
        adminServiceAPIClient2 = adminServiceAPIClient;
        organizationServiceAPIClient2 = organizationServiceAPIClient;
    }


    public static void main(String[] args) {
        SpringApplication.run(InvoiceApplication.class, args);
        System.out.println("================================");
        System.out.println("Invoice Service Started");
        System.out.println("================================");

        // test();

    }


    public static void test() {

        InvoiceApplication obj = new InvoiceApplication();

        System.out.println("");
        System.out.println("AdminServiceAPIClient");
        System.out.println(adminServiceAPIClient2);
        System.out.println("");

        ObjAPIRes response = obj.organizationServiceAPIClient2.getInvoiceSequentialInfo(
                UUID.fromString("1d4e5afd-20bd-4522-93df-b4154f5d6a44")
                , "s", "s", "s");

        System.out.println(">>>>>>>>>>>");
        System.out.println("");
        System.out.println("Response");
        System.out.println(response.toString());
        System.out.println("");
        System.out.println(">>>>>>>>>>>");

        generateSequentialNumber("Sa");

    }


    public static void generateSequentialNumber(String orgId) {

        InvoiceApplication obj = new InvoiceApplication();

        ObjAPIRes response = obj.organizationServiceAPIClient2.getInvoiceSequentialInfo(
                UUID.fromString("1d4e5afd-20bd-4522-93df-b4154f5d6a44")
                , "s", "s", "s");

        Map<String, Object> data = response.getData();
        Long seqStartNo = Long.valueOf(data.get("seq_start_no").toString());
        Long seqCurrenttNo = Long.valueOf(data.get("seq_current_no").toString());
        String seqText = data.get("seq_text").toString();
        String invoiceNoFormat = data.get("invoice_no_format").toString();
        String seqYear = data.get("seq_year").toString();

        Long nextNumber = seqCurrenttNo + 1;
        String invoiceNo = invoiceNoFormat;

        String date = Utility.getDateByEpoch(System.currentTimeMillis());
        String currentYear = date.split("/")[2];

        invoiceNo = invoiceNo.replace("ST", seqText)
                .replace("SQ", nextNumber.toString())
                .replace("FY", currentYear);

    }

}
