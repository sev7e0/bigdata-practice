package com.tools.hadoop.mr.duplicate;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Title:MR
 * description:
 *
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2018-10-29 16:20
 **/

public class DuplicateRemoval {
    private final static Logger logger = LoggerFactory.getLogger(DuplicateRemoval.class);

    /**
     * map将输入中的value复制到输出数据的key上，并直接输出
     */
    public static class Map extends Mapper<Object, Text, Text, Text> {

        /**
         * 实现map函数
         */
        @Override
        public void map(Object key, Text value, Context context)

                throws IOException, InterruptedException {
            context.write(value, new Text(""));
        }
    }

    /**
     * reduce将输入中的key复制到输出数据的key上，并直接输出
     */
    public static class Reduce extends Reducer<Text, Text, Text, Text> {
        /**
         * 实现reduce函数
         */
        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            context.write(key, new Text(""));
        }
    }

    /**
     * 入口方法
     */
    public static void main(String[] args) throws Exception {

        //默认不做任何配置,都由配置文件中加载
        Configuration conf = new Configuration();
        /**
         * 默认入口携带第一个参数为输入路径,第二个为输出路径
         */
        Path path = new Path(args[1]);

        //从配置文件中获取当前文件系统,判断是os还是hdfs
        FileSystem fs = FileSystem.get(conf);

        if (fs.exists(path)) {
            logger.error("Usage: Data Deduplication <in> <out>");
            //当输出文件已经存在时,进行删除
            fs.delete(path, true);
            System.exit(2);
        }

        //Job job = new Job(conf);//已经不推荐使用
        //推荐使用当前方式
        Job job = Job.getInstance(conf, "DuplicateRemoval");
        job.setJarByClass(DuplicateRemoval.class);

        //设置输入和输出目录
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, path);

        //设置Map处理类
        job.setMapperClass(Map.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        //设置Combine和Reduce处理类
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);

        //设置输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}
