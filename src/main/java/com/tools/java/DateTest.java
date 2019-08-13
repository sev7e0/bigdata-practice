package com.tools.java;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @description: TODO
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2019-01-16 14:24
 **/

public class DateTest {
    public static void main(String[] args) {
        String str = "2015年4月27日";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        Date format = null;
        Calendar calendar = null;
        try {
            format = sdf.parse(str);
            calendar = Calendar.getInstance();
            calendar.setTime(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(calendar.get(Calendar.YEAR));

        System.out.println(Math.round(12.5));
        System.out.println(Math.round(5 >> 2));
        System.out.println(new Integer(5).floatValue() / 2);
        System.out.println(Math.round(0f / 2));
        System.out.println(5f / 2);



    }
}
