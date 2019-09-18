package com.tools.hadoop.mr;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class HFileImportMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, KeyValue> {

    protected final String CF_KQ = "cf";

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        log.info("read line: {}", line);
        String[] strings = line.split(" ");
        String row = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "_" + strings[1];
        ImmutableBytesWritable writable = new ImmutableBytesWritable(Bytes.toBytes(row));
        KeyValue keyValue = new KeyValue(Bytes.toBytes(row), this.CF_KQ.getBytes(), strings[1].getBytes(), strings[2].getBytes());
        context.write(writable, keyValue);
    }
}