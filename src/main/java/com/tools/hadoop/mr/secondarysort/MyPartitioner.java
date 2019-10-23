package com.tools.hadoop.mr.secondarysort;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Partitioner;

//mapper的输出key类型是自定义的key类型OrderBean；输出value类型是单笔订单的总开销double -> DoubleWritable
public class MyPartitioner extends Partitioner<OrderBean, DoubleWritable> {
    @Override
    public int getPartition(OrderBean orderBean, DoubleWritable doubleWritable, int numReduceTasks) {
        //userid相同的，落入同一分区
        return (orderBean.getUserid().hashCode() & Integer.MAX_VALUE) % numReduceTasks;
    }
}