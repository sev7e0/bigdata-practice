package com.tools.hadoop.mr.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class MyPartitioner extends Partitioner<Text, LongWritable> {

    @Override
    public int getPartition(Text arg0, LongWritable arg1, int arg2) {

        if (arg0.toString().equals("hadoop")) {
            return 0;
        }
        if (arg0.toString().equals("spark")) {
            return 1;
        }
        if (arg0.toString().equals("hbase")) {
            return 2;
        }
        return 3;
    }

}
