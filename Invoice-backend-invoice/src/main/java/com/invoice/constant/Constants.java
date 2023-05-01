package com.invoice.constant;

public interface Constants {

    interface valueType {

        String PERCENT = "percent";
        String AMOUNT = "amount";
    }

    interface taxType {

        String ITEM = "item";
        String INVOICE = "invoice";
    }


    // Draft / Approved / Declined/pending

    interface invoiceStatus {

        String DRAFT = "draft";
        String APPROVED = "approved";
        String DECLINED = "declined";
        String PENDING = "pending";
    }

    interface paymentStatus {

        String PENDING = "pending";
        String DONE = "done";
        String FAILURE = "failure";
    }

}