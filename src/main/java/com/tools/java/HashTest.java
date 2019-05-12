package com.tools.java;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Title:sparklearn
 * description:
 *
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2018-12-03 16:22
 **/

public class HashTest {
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    public static void main(String[] args) {

        String str = "this is a test";

        System.out.println(str.hashCode());

        Integer hash = str.hashCode() ^ str.hashCode() >>> 16;

        System.out.println(hash);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();


        int num = 6;

        System.out.println(num / 2);
        System.out.println(num >> 1);

//        System.out.println(tableSizeFor(128));

    }

    private static final int tableSizeFor(int n) {
        n = n - 1;
        System.out.println(n);
        n |= n >>> 1;
        System.out.println(n);
        n |= n >>> 2;
        System.out.println(n);
        n |= n >>> 4;
        System.out.println(n);
        n |= n >>> 8;
        System.out.println(n);
        n |= n >>> 16;
        System.out.println(n);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
