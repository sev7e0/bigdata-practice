package com.tools.hadoop.mr.dataclean;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class DataClean {
    /**
     *
     * 注意：若要IDEA中，本地运行MR程序，需要将resources/mapred-site.xml中的mapreduce.framework.name属性值，设置成local
     * @param args
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        //判断一下，输入参数是否是两个，分别表示输入路径、输出路径
        if (args == null || args.length != 2) {
            System.out.println("please input Path!");
            System.exit(0);
        }

        Configuration configuration = new Configuration();

        //调用getInstance方法，生成job实例
        Job job = Job.getInstance(configuration, DataClean.class.getSimpleName());

        //设置jar包，参数是包含main方法的类
        job.setJarByClass(DataClean.class);

        //设置输入/输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //设置处理Map阶段的自定义的类
        job.setMapperClass(DataCleanMapper.class);

        //注意：此处设置的map输出的key/value类型，一定要与自定义map类输出的kv对类型一致；否则程序运行报错
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        //注意：因为不需要reduce聚合阶段，所以，需要显示设置reduce task个数是0
        job.setNumReduceTasks(0);

        // 提交作业
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    /**
     *
     * 自定义mapper类
     * 注意：若自定义的mapper类，与main方法在同一个类中，需要将自定义mapper类，声明成static的
     */
    public static class DataCleanMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        NullWritable nullValue = NullWritable.get();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //自定义计数器，用于记录残缺记录数
            Counter counter = context.getCounter("DataCleaning", "damagedRecord");

            //获得当前行数据
            //样例数据：20111230111645  169796ae819ae8b32668662bb99b6c2d        塘承高速公路规划线路图  1       1       http://auto.ifeng.com/roll/20111212/729164.shtml
            String line = value.toString();

            String[] fields = line.split("\t");

            if(fields.length != 6) {
                //若不是，则不输出，并递增自定义计数器
                counter.increment(1L);
            } else {
                //若是6，则原样输出
                context.write(value, nullValue);
            }
        }
    }
}
