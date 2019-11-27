package com.tools.hbase.hdfs2hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import java.io.IOException;

public class HDFS2HBase  {

    public static class HBaseMap extends Mapper<LongWritable, Text, Text, NullWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(value,NullWritable.get());
        }
    }

    public static class HBaseReduce extends TableReducer<Text,NullWritable, ImmutableBytesWritable>{
        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            String[] data = key.toString().split("\t");
            Put put = new Put(data[0].getBytes());
            put.addColumn("info".getBytes(),"name".getBytes(),data[1].getBytes());
            put.addColumn("info".getBytes(),"sex".getBytes(),data[2].getBytes());
            put.addColumn("course".getBytes(),"match".getBytes(),data[3].getBytes());
            put.addColumn("course".getBytes(),"chinese".getBytes(),data[4].getBytes());
            context.write(new ImmutableBytesWritable(Bytes.toBytes(data[0])),put);
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJarByClass(HDFS2HBase.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));
        job.setMapperClass(HBaseMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        /**
         * reduce任务交由Hbase完善
         */
        TableMapReduceUtil.initTableReducerJob(args[1],HBaseReduce.class,job);
        job.setNumReduceTasks(1);
        job.waitForCompletion(false);

    }
}
