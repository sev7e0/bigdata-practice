package com.tools.redis;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 应对缓存穿透、缓存击穿、缓存雪崩的几种方案
 */
public class CacheTuning {

    private static Jedis jedis = new Jedis();
    private static HashMap<String, String> map = new HashMap<>();
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println(jedis.ping());
        map.put("1", "a");
        map.put("2", "b");

//        for (int i = 0; i < 1; i++) {
//            threadPool.execute(() -> map.keySet().forEach(key -> System.out.printf(get(key))));
//        }
//        System.out.println(get("2"));

        bloomFilter();
    }

    /**
     * 缓存穿透
     */
    /**
     * 1.使用bloom filter进行拦截
     *
     * 有很多种方法可以有效地解决缓存穿透问题，最常见的则是采用布隆过滤器，将所有可能存在的数据哈希到一个足够大的bitmap中，一个一定不存在的数据会被 这个bitmap拦截掉，
     * 从而避免了对底层存储系统的查询压力。另外也有一个更为简单粗暴的方法（我们采用的就是这种），如果一个查询返回的数据为空（不管是数 据不存在，还是系统故障），
     * 我们仍然把这个空结果进行缓存，但它的过期时间会很短，最长不超过五分钟。
     */
    private static void bloomFilter(){
        int size =  1000000;
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size);

        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }

        LocalTime before = LocalTime.now();
        if (bloomFilter.mightContain(200099)){
            System.out.println("mightContain");
        }
        LocalTime now = LocalTime.now();
        System.out.println(Duration.between(before, now).getNano());

    }


    /**
     * 缓存击穿
     * 缓存在某个时间点过期的时候，恰好在这个时间点对这个Key有大量的并发请求过来，
     * 这些请求发现缓存过期一般都会从后端DB加载数据并回设到缓存，这个时候大并发的请求可能会瞬间把后端DB压垮。
     */
    /**
     * 1.使用分布式互斥锁的方式 解决缓存中找不到对应值的问题
     */
    private static String get(String key) {
        String stop = "stop";
        String value = jedis.get(key);
        if (value == null) {
            if (jedis.setnx(stop, "1") == 1) {
                System.out.println("已获取到锁，正在更新缓存");
                jedis.expire(stop, 3 * 60);
                value = dbGet(key);
                jedis.set(key, value);
                System.out.println("缓存更新完成！！！");
                jedis.del(stop);
            } else {
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

    /**
     * 2.不由redis控制过期时间，由程序维护，无论有没有查询到数据都直接返回。在获得的数据时若发现超时，则由程序发起异步线程进行缓存更新。
     * 优点是不会产生死锁，缺点数据一致性较低
     */
    private static String getByTimeOut(String key) {
        String stop = "stop";
        AtomicReference<String> value = new AtomicReference<>(jedis.get(key));
        String[] split = value.get().split(".");
        String relValue = split[0];
        Long timeout = Long.valueOf(split[1]);
        if (timeout < LocalTime.now().getLong(ChronoField.NANO_OF_DAY)) {
            threadPool.execute(() -> {
                if(jedis.setnx(stop,"1") == 1){
                    System.out.println("已获取到锁，正在更新缓存");
                    jedis.expire(stop, 3 * 60);
                    value.set(dbGet(key));
                    jedis.set(key, value.get());
                    System.out.println("缓存更新完成！！！");
                    jedis.del(stop);
                }
            });
        }
        return value.get();
    }

    private static String dbGet(String key) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return map.get(key);
    }

    /**
     * 缓存雪崩
     *
     * 缓存雪崩是指在我们设置缓存时采用了相同的过期时间，导致缓存在某一时刻同时失效，请求全部转发到DB，DB瞬时压力过重雪崩。
     */
    /**
     * 1.使用加锁或者队列的单线程方式，保证在有大量的数据失效时，不会有大量的并发请求发送到DB，导致压力过大
     */
    /**
     * 2.在原有的过期时间上 随机添加一些时间，由于过期时间不同，就能减轻在过期时产生的压力
     */
}
