package com.tools.hadoop.mr.outputformat;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class MyRecordWriter extends RecordWriter<Text, NullWritable> {
    FSDataOutputStream badOut;
    FSDataOutputStream goodOut;

    public MyRecordWriter(FSDataOutputStream badOut, FSDataOutputStream goodOut) {
        this.badOut = badOut;
        this.goodOut = goodOut;
    }

    @Override
    public void write(Text text, NullWritable nullWritable) throws IOException, InterruptedException {
        if (text.toString().split("\t")[9].equals("0")) {
            goodOut.write(text.toString().getBytes());
            goodOut.write("\r\n".getBytes());
        } else {
            badOut.write(text.toString().getBytes());
            badOut.write("\r\n".getBytes());
        }
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        if (goodOut != null) {
            goodOut.close();
        }
        if (badOut != null) {
            badOut.close();
        }
    }
}
