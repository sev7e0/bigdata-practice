package com.tools.hadoop.mr.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 自定义partitioner
 */
public class MyPartitioner extends Partitioner<Text, LongWritable> {


    /**
     *
     * 重写方法
     *
     * @param arg0 输入数据
     * @param arg1
     * @param arg2
     * @return 返回值为分区序号
     */
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
