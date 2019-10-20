package com.tools.hadoop.mr.outputformat;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyFileOutputFormat extends FileOutputFormat<Text, NullWritable> {
    //与reduce的输出泛型一致


    String good = "hdfs://spark01:8020/outputformat/bad/r.txt";
    String bad = "hdfs://spark01:8020/outputformat/good/r.txt";

    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(taskAttemptContext.getConfiguration());

        Path badPath = new Path(bad);
        Path goodPath = new Path(good);
        return null;
    }
}
