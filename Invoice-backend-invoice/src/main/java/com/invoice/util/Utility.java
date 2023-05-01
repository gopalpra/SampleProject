package com.invoice.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public static long getCurrentTimestamp(){
        return System.currentTimeMillis() / 1000;
    }
    public static String formatToTwoDecimalPlaces(double number) {

        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(number);

    }

    public static Long getEpochFromDateTime (String datetime) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // df.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Kolkata")));

        Date date = df.parse(datetime);
        return date.getTime();

    }

    public static String getDateByEpoch(long epochSec) {
        Date date = new Date(epochSec);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault());
        return format.format(date);
    }

    public static String getDateTimeByEpoch(long epochSec) {
        Date date = new Date(epochSec);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",Locale.getDefault());
        return format.format(date);
    }


    public static String getDateByEpochByFormat(long epochSec, String formatString) {
        Date date = new Date(epochSec);
        SimpleDateFormat format = new SimpleDateFormat(formatString,
                Locale.getDefault());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString, Locale.ENGLISH);
        // simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        // return new java.sql.Date(simpleDateFormat.parse(date).getTime());

        return format.format(date);
    }


}

