package com.tools.hadoop.mr;

import com.tools.hbase.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

/**
 * bulk load like a ETL
 *  - Extract: from text file or another database into HDFS
 *  - Transform: data into HFile(HBase's own file format)
 *  - Load: load the HFile into HBase and tell region server where to find them
 */

@Slf4j
public class HFileGenerator {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Usage：hadoop jar HFileGenerator.jar inputPath outputPath tableName configPath");
            System.exit(0);
        }
        Job job = createJob(args[0], args[1], args[2], args[3]);
        if (Objects.isNull(job)) {
            log.error("error in create job!");
        }
        try {
            if (job.waitForCompletion(true)){
                log.info("execute job finish!");
                Utils.doBulkLoad(job.getConfiguration(),args[1],args[2]);
            }else {
                log.error("execute job failed!!");
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Job createJob(String inputPath, String outputPath, String tableName, String configPath) {
        Configuration configuration = new Configuration();
        configuration.addResource(new Path(configPath));
        configuration.set("hbase.fs.tmp.dir", "partition_" + UUID.randomUUID());
        Job job = null;
        try {
            try {
                FileSystem fileSystem = FileSystem.get(URI.create(outputPath), configuration);
                fileSystem.delete(new Path(outputPath), true);
                fileSystem.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Connection connection = ConnectionFactory.createConnection(configuration);
            Table table = connection.getTable(TableName.valueOf(tableName));
            job = Job.getInstance(configuration);
            job.setJobName("HFileGenerator Job");

            job.setJarByClass(HFileGenerator.class);
            job.setOutputFormatClass(TextOutputFormat.class);
            job.setMapperClass(HFileImportMapper.class);
            FileInputFormat.setInputPaths(new JobConf(configuration), inputPath);
            FileOutputFormat.setOutputPath(new JobConf(configuration), new Path(outputPath));

            HFileOutputFormat2.configureIncrementalLoad(job, table, connection.getRegionLocator(TableName.valueOf(tableName)));
        } catch (Exception e) {

        }
        return job;
    }

}
