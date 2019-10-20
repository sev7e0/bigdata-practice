package com.tools.hadoop.mr.inputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SmallFiles2SequenceFile extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int code = ToolRunner.run(new SmallFiles2SequenceFile(), args);
        System.exit(code);
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration configuration = new Configuration();

        configuration.set("mapreduce.map.output.compress", "true");
        //设置map输出的压缩算法是：BZip2Codec，它是hadoop默认支持的压缩算法，且支持切分
        configuration.set("mapreduce.map.output.compress.codec", "org.apache.hadoop.io.compress.BZip2Codec");
        //开启job输出压缩功能
        configuration.set("mapreduce.output.fileoutputformat.compress", "true");
        //指定job输出使用的压缩算法
        configuration.set("mapreduce.output.fileoutputformat.compress.codec", "org.apache.hadoop.io.compress.BZip2Codec");

        Job job = Job.getInstance(configuration, SmallFiles2SequenceFile.class.getName());

        job.setJarByClass(SmallFiles2SequenceFile.class);

        job.setMapperClass(SmallFiles2SequenceMapper.class);

        job.setInputFormatClass(WholeFileInputFormat.class);

        WholeFileInputFormat.addInputPath(job, new Path(strings[0]));

        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        SequenceFileOutputFormat.setOutputPath(job, new Path(strings[1]));

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        return job.waitForCompletion(true) ? 0 : 1;

    }
}
