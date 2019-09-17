package com.tools.hadoop.mr;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HRegionLocator;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class HFileGenerator {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.addResource(new Path(""));
        configuration.set("hbase.fs.tmp.dir", "partition_" + UUID.randomUUID());
        String tableName = "demo";
        String input = "";
        String output = "";

        try {
            try {
                FileSystem fileSystem = FileSystem.get(URI.create(output), configuration);
                fileSystem.delete(new Path(output), true);
                fileSystem.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Connection connection = ConnectionFactory.createConnection(configuration);
            Table table = connection.getTable(TableName.valueOf(tableName));
            Job job = Job.getInstance(configuration);
            job.setJobName("gen hfile");

            job.setJarByClass(HFileGenerator.class);
            job.setOutputFormatClass(TextOutputFormat.class);
            job.setMapperClass(HFileImportMapper.class);
            FileInputFormat.setInputPaths(new JobConf(configuration), input);
            FileOutputFormat.setOutputPath(new JobConf(configuration), new Path(output));

            HFileOutputFormat2.configureIncrementalLoad(job, table, new HRegionLocator(TableName.valueOf(tableName), null));
            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class HFileImportMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, KeyValue> {

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

}
