package com.tools.redis;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MutexLock {

    private static Jedis jedis = new Jedis();
    private static HashMap<String, String> map = new HashMap<>();


    public static void main(String[] args) {
        System.out.println(jedis.ping());
        map.put("1","a");
        map.put("2","b");
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        for (int i = 0 ; i<4; i++){
            threadPool.execute(() -> map.keySet().forEach(key-> System.out.printf(get(key))));
        }
//        System.out.println(get("2"));

    }

    private static String get(String key){
        String stop = "stop";
        String value = jedis.get(key);
        if (value == null){
            if (jedis.setnx(stop, "1") == 1) {
                System.out.println("已获取到锁，正在更新缓存");
                jedis.expire(stop, 3*60);
                value = dbGet(key);
                jedis.set(key, value);
                System.out.println("缓存更新完成！！！");
                jedis.del(stop);
            }
            else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("当前已被加锁，准备重试");
                value = get(key);
            }

        }
        return value;
    }

    private static String dbGet(String key) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return map.get(key);
    }
}
