package com.tools.java;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @description:
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2019-01-02 15:15
 **/

public class CollectionTest {


    public static void main(String[] args) {
        filterByKeys();
        distinct();
    }

    /**
     * 使用distinct对list进行去重,不能设置过滤条件
     */
    private static void distinct() {
        int[] a = {1, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 5, 32};
        Arrays.stream(a).distinct().forEach(System.out::println);
    }


    /**
     * 使用filter对list中对象指定key进行去重
     */
    private static void filterByKeys() {
        User lee = new User(1, "lee");
        User java8 = new User(3, "java8");
        User master = new User(4, "master");
        User kafka = new User(5, "kafka");

        ArrayList<User> users = new ArrayList<>();
        users.add(lee);
        users.add(java8);
        users.add(java8);
        users.add(java8);
        users.add(master);
        users.add(kafka);
        users.add(lee);

        users.stream().filter(distinctByKey(User::getId)).forEach(u -> System.out.println(u.getId() + "---" + u.getName()));
    }

    //自定义过滤器  注意function的用法
    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    static class User {
        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
