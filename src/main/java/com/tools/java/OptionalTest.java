package com.tools.java;

import java.time.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Title:sparklearn
 * description:
 *
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2018-11-27 17:14
 **/

public class OptionalTest {
    public static void main(String[] args) {
        //Optional 为了解决空指针问题
        Optional<String> sparkLearn = Optional.of("Spark learn");
        Optional<String> learn = Optional.ofNullable(null);
        Optional<String> sparkContext = Optional.empty();
        System.out.println(sparkLearn.isPresent()); //true
        System.out.println(learn.isPresent()); //false
        System.out.println(sparkContext.isPresent());//false
        System.out.println(sparkContext.orElse("defult"));//defult

        System.out.println(sparkLearn.get());

        System.out.println(sparkLearn.filter(s -> s == "Spark learn"));


        Integer a = null;
        Integer b = null;

        Optional<Integer> a1 = Optional.ofNullable(a);
        Optional<Integer> b1 = Optional.ofNullable(b);

        sum(a1, b1);

        String str = "this is a wordcount test ha ha ha ";
        wordCount(str);

        //java8中java时间API
        System.out.println(LocalTime.now());
        System.out.println(LocalDate.now());
        System.out.println(ZonedDateTime.now());

        final LocalDateTime from = LocalDateTime.of(2017, Month.APRIL, 16, 0, 0, 0);
        final LocalDateTime to = LocalDateTime.of(2017, Month.APRIL, 16, 23, 59, 59);

        final Duration duration = Duration.between(from, to);
        System.out.println("Duration in days: " + duration.toDays());
        System.out.println("Duration in hours: " + duration.toNanos());

        //java8 自带base64算法
        String base64 = "Base64 finally in Java 8!";
        String encode = Base64.getEncoder().encodeToString(base64.getBytes());
        System.out.println(encode);

        byte[] decode = Base64.getDecoder().decode(encode);
        String s = new String(decode);
//        String s = String.valueOf(decode);
        System.out.println(s);

    }

    private static void sum(Optional<Integer> a, Optional<Integer> b) {

        Integer integer = a.orElse(0);
        Integer integer1 = b.orElse(10);

        System.out.println(integer + integer1);
    }

    private static void wordCount(String string) {
        String[] split = string.split(" ");

        Arrays.stream(split).map(sp -> new HashMap<String, Integer>().put(sp, 1)).forEach(map -> {
        });
    }
}
