package com.invoice.constant;
public interface ServiceAPI
{
    interface Admin
    {
        String country = "/country";

        String currencyMaster = "/currency/master";
        String countryList = "/country/list";
        String countryWithTax = "/country/tax";

        String currency = "/currency/list";
        String tax = "/invoice/tax/list";
    }
    interface Organization
    {
        String orgData = "/org/profile";
        String orgBranch = "/org/branch";
    }
}