package com.tools.hadoop.mr.outputformat;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class MyFileOutputFormatMain extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = new Configuration();

        Job job = Job.getInstance(configuration, MyFileOutputFormatMain.class.getName());


        job.setJarByClass(MyFileOutputFormatMain.class);


        TextInputFormat.addInputPath(job, new Path(strings[0]));
        MyFileOutputFormat.setOutputPath(job, new Path(strings[1]));
        job.setMapperClass(MyFileOutputFormatMapper.class);
        job.setOutputFormatClass(MyFileOutputFormat.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        return job.waitForCompletion(true) ? 0 : 1;


    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new MyFileOutputFormatMain(), args);
    }

    private static class MyFileOutputFormatMapper extends Mapper {
        @Override
        protected void map(Object key, Object value, Context context) throws IOException, InterruptedException {
            context.write(value, NullWritable.get());
        }
    }
}
