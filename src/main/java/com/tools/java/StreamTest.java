package com.tools.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @description: 关于java8中stream的使用
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2018-12-20 13:58
 * @about: Java 8 中的 Stream 是对集合（Collection）对象功能的增强，它专注于对集合对象进行各种非常便利、高效的聚合操作（aggregate operation），或者大批量数据操作 (bulk data operation)。
 * Stream API 借助于同样新出现的 Lambda 表达式，极大的提高编程效率和程序可读性。
 * 同时它提供串行和并行两种模式进行汇聚操作，并发模式能够充分利用多核处理器的优势，使用 fork/join 并行方式来拆分任务和加速处理过程。
 * 通常编写并行代码很难而且容易出错, 但使用 Stream API 无需编写一行多线程的代码，就可以很方便地写出高性能的并发程序。
 * 所以说，Java 8 中首次出现的 java.util.stream 是一个函数式语言+多核时代综合影响的产物。
 **/

public class StreamTest {

    private static Logger logger = LoggerFactory.getLogger(StreamTest.class);

    public static void main(String[] args) {
        //构造流的几种方式
        Stream<String> a = Stream.of("a", "b", "c");

        String[] strings = {"a", "b", "c"};
        Stream<String> stringStream = Stream.of(strings);
        Stream<String> stringStream1 = Arrays.stream(strings);
        List<String> list = Arrays.asList(strings);
        Stream<String> stringStream2 = list.stream();

        /**
         * 对于基本数值型，目前有三种对应的包装类型 Stream
         */
        IntStream.of(1, 2, 3).forEach(in -> logger.info(String.valueOf(in)));
        //rangeClosed表示右侧为闭区间
        LongStream.rangeClosed(1, 8).filter(l -> l > 5).forEach(in -> logger.info(String.valueOf(in)));
        DoubleStream.of(1L, 100L, 5).average().orElse(0L);


        OptionalDouble average1 = IntStream.builder().add(1).add(4).build().average();
        //使用{}占位符。避免字符串连接操作，减少String对象（不可变）带来的内存开销
        logger.info("平均值为：{}", average1.orElse(0));

        Integer[] integers = IntStream.range(0, 10).mapToObj(in -> in + 1).toArray(Integer[]::new);
        Stream.of(integers).forEach(i -> logger.info("生成转化为integer数组：{}", i));

    }
}
