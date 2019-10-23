package com.tools.java;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static java.time.LocalDateTime.parse;

public class DateTimeUtils {
    public static LocalDateTime parseDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return parse(dateTime, formatter);
    }

    //日期格式转换工具类：将2014-12-14 20:42:14.000转换成201412
    public static String getYearMonthString(String dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = parse(dateTime, formatter);
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        return year + "" + month;
    }

}
