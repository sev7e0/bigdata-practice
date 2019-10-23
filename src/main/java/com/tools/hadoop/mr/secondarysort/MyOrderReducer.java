package com.tools.hadoop.mr.secondarysort;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MyOrderReducer extends Reducer<OrderBean, DoubleWritable, Text,DoubleWritable> {

    @Override
    protected void reduce(OrderBean key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        int num = 0;
        for(DoubleWritable value: values) {
            if(num < 2) {
                String keyOut = key.getUserid() + "-----" + key.getDatetime();
                context.write(new Text(keyOut), value);
                num++;
            } else {
                break;
            }
        }

    }
}
