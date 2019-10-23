package com.tools.hadoop.mr.outputformat;

import org.apache.hadoop.fs.FSDataOutputStream;
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


    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(taskAttemptContext.getConfiguration());

        String bad = "hdfs://spark01:8020/outputformat/good/r.txt";
        Path badPath = new Path(bad);
        String good = "hdfs://spark01:8020/outputformat/bad/r.txt";
        Path goodPath = new Path(good);
        FSDataOutputStream badStream = fileSystem.create(badPath);
        FSDataOutputStream goodStream = fileSystem.create(goodPath);
        return new MyRecordWriter(badStream, goodStream);
    }
}
