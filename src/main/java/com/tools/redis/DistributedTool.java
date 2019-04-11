package com.tools.redis;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * 基于redis实现的分布式锁,当前只是针对单节点模式。
 *
 * redis集群情况下可以考虑使用redisson。
 */
public class DistributedTool {

    private static final String LOCK_STATUS = "OK";
    /**
     * 当key不存在时进行当前操作，若存在则不操作
     */
    private static final String SET_IF_NOT_EXIST = "NX";
    /**
     * 设定key的超时时间
     */
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     *
     * @param jedis redis客户端
     * @param key 使用key来当作锁，保证唯一性
     * @param lockId 要保证每次加锁和解锁是一个来自客户端，只用一个key是无法保证的，这里使用value值作为一次完整加锁解锁请求id来保证。
     * @param time 超时时间，设定了超时时间后，即使持有锁的客户端发生崩溃，key也会因为过期而自动删除，从而释放锁。
     * @return 加锁状态
     */
    public static Boolean acquireDistributedLock(Jedis jedis, String key, String lockId, Long time){
        String status = jedis.set(key, lockId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, time);
        if (LOCK_STATUS.equals(status)){
            System.out.println("获取锁成功，当前："+key+"-"+lockId);
            return true;
        }
        return false;
    }


    /**
     *
     * @param jedis redis客户端
     * @param key 锁
     * @param lockId 锁对应的请求id
     * @return 释放锁结果
     *
     * 使用lua脚本是为了保证操作的原子性，redisson中使用同样的方式进行锁释放。
     *
     * 为什么锁redis使用lua是线程安全的？
     *  因为redis本身就是单线程的，而redis内置了lua的解析器，从而能保证线程安全（不够严谨）
     */
    public static Boolean releaseDistributedLock(Jedis jedis, String key, String lockId){
        //lua脚本
        String luaScript="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //调用evel交给redis服务端执行脚本
        Object status = jedis.eval(luaScript, Collections.singletonList(key), Collections.singletonList(lockId));
        if (LOCK_STATUS.equals(status)){
            System.out.println("释放锁成功，当前："+key+"-"+lockId);
            return true;
        }
        return false;
    }

}
