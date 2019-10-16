package com.tools.hadoop.mr.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author LiJiaqi
 * @ClassName: WordCountMapper
 * @Description:使用mapreduce开发wordcount程序
 * @date 2018年8月22日  下午11:15:54
 */
public class LogCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    /**
     * 读取输入文件
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] words = line.split(" ");
        for (String word : words) {
            //通过上下文将结果输出
            context.write(new Text(word), new LongWritable(1L));
        }
    }
}
