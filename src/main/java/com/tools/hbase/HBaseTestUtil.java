package com.tools.hbase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HBaseTestUtil {

    public static void main(String[] args) {
        System.out.println(getTableName());
    }
    public static byte[] getTableName(){
        String format = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return ("table_"+format).getBytes();
    }

    public static byte[] getFamilyName(){
        String format = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return ("family_"+format).getBytes();
    }

}
