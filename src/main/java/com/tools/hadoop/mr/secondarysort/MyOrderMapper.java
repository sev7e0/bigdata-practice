package com.tools.hadoop.mr.secondarysort;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static java.time.LocalDateTime.parse;


/**
 * 泛型描述 <p> 输入key 输入value 输出key 输出value</>
 */
public class MyOrderMapper extends Mapper<LongWritable, Text, OrderBean, DoubleWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] values = Optional.ofNullable(value).map(v -> v.toString().split("\t")).get();
        String yearMonthString;
        try {
            yearMonthString = getYearMonthString(values[1], "yyyy-MM-dd HH:mm:ss.SSS");
        }catch (Exception e){
            return;
        }
        if (values.length == 6){
            //13764633023     2014-12-01 02:20:42.000 全视目Allseelook 原宿风暴显色美瞳彩色隐形艺术眼镜1片 拍2包邮    33.6    2       18067781305
            OrderBean orderBean = new OrderBean(values[0],
                    yearMonthString,
                    values[2],
                    Double.parseDouble(values[3]),
                    Integer.parseInt(values[4]),
                    values[5]);

            DoubleWritable doubleWritable = new DoubleWritable();
            doubleWritable.set(Double.parseDouble(values[3])*Integer.parseInt(values[4]));
            context.write(orderBean, doubleWritable);
        }
    }

    public static String getYearMonthString(String dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = parse(dateTime, formatter);
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        return year + "" + month;
    }

}
