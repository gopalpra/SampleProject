package com.invoice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator
{


    final static String NUMBER_PATTERN = "(0/91)?[7-9][0-9]{9}";
    final static String PHONE_NUMBER_PATTERN = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    //final static String PHONE_NUMBER_PATTERN = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    final static String ACCOUNT_ID_PATTERN = "(?i)^(?=.*[a-z])[a-z0-9]{8,20}$";
    final static String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    final static String TIME_PATTERN = "\\d{2}:\\d{2}";
    private static Pattern pattern;
    private static Matcher matcher;

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    public static boolean validateTime(String time) {
        pattern = Pattern.compile(TIME_PATTERN);
        return pattern.matcher(time).matches();
    }

    public static boolean checkNumber(String value) {

        pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean checkAccountId(String value) {

        pattern = Pattern.compile(ACCOUNT_ID_PATTERN);
        matcher = pattern.matcher(value);
        return matcher.matches();
    }
    public static boolean checkName(String name)
    {
        pattern=Pattern.compile( "^[a-zA-Z]*$");
        matcher = pattern.matcher(name);
        return matcher.matches();
    }


    public static boolean emailRegex(String email) {

        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }
    public static boolean dateRegex(String date) {

//        Pattern VALID_DATE_REGEX =
//                Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/(\\d\\d\\d\\d)");
        Pattern VALID_DATE_REGEX =
                Pattern.compile("^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$");
//        System.out.println(VALID_DATE_REGEX);

        Matcher matcher = VALID_DATE_REGEX.matcher(date);
        return matcher.matches();
    }

    public static boolean validateDate(String date) {
        if (!Validator.dateRegex(date)) {
            logger.info("Invalid Date Format : {}", date);
            return false;
        } else {
            return true;
        }
    }

    public static boolean validateEmailId(String emailId) {
        if (!Validator.emailRegex(emailId)) {
            logger.info("Invalid EmailId : {}", emailId);
            return false;
        } else {
            return true;
        }
    }

    public static boolean validateMobileNo(String mobileNo) {
        if (!Validator.checkNumber(mobileNo)) {
            logger.info("Invalid MobileNo : {}", mobileNo);
            return false;
        } else {
            return true;
        }
    }


    public static boolean validateEmailIds(List<String> emailIds) {

        for (String emailId : emailIds) {
            if (!Validator.emailRegex(emailId)) {
                logger.info("Invalid EmailId : {}", emailId);
                return false;
            }
        }
        return true;
    }

    public static boolean validateMobileNos(List<String> mobileNos) {

        for (String mobileNo : mobileNos) {
            if (!Validator.checkNumber(mobileNo)) {
                logger.info("Invalid MobileNo : {}", mobileNo);
                return false;
            }
        }
        return true;
    }
}
