package com.tools.hadoop.mr.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author LiJiaqi
 * @ClassName: WordCountMapper
 * @Description:使用mapreduce开发wordcount程序
 * @date 2018年8月22日 下午11:15:54
 */
public class ParititionerAppMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    /**
     * 读取输入文件
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //按行读取文件
        String line = value.toString();
        // 20111230115903	262c9791427904631304a5eea4484bd5	音乐	4	1	http://mp3.baidu.com/
        String[] words = line.split(" ");
        // 通过上下文将结果输出 userid 262c9791427904631304a5eea4484bd5
        context.write(new Text(words[1]), new LongWritable(Long.parseLong(words[1])));
    }
}
