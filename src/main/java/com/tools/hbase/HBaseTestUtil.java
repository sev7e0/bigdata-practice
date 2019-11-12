package com.tools.hbase;

import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class HBaseTestUtil {

    public static void main(String[] args) {
        System.out.println(Bytes.toString(getTableName(null)));
    }

    public static byte[] getTableName(String name) {
        String format = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return Objects.isNull(name) ? ("table_" + format).getBytes() : name.getBytes();
    }

    public static byte[] getFamilyName(String name) {
        String format = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return Objects.isNull(name) ? ("family_" + format).getBytes() : name.getBytes();
    }

}
