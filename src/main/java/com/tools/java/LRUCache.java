package com.tools.java;

import java.util.HashMap;
import java.util.Objects;

/**
 * Title:sparklearn
 * description: 双链表+HashMap实现LRUCache
 *
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2018-11-28 13:55
 **/

public class LRUCache<K, V> {

    private CacheNode first;
    private CacheNode last;
    private Integer currentCacheSize;
    private Integer cacheCapcity;
    private HashMap<K, CacheNode> cache;

    public LRUCache(Integer cacheCapcity) {
        this.currentCacheSize = 0;
        this.cacheCapcity = cacheCapcity;
        this.cache = new HashMap<K, CacheNode>(cacheCapcity);
    }

    /**
     * cache的put操作
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        CacheNode node = cache.get(key);
        if (node == null) {
            //判断容量是否等于当前的size,是的话将会移除最后一个
            if (Objects.equals(currentCacheSize, cacheCapcity)) {
                cache.remove(key);
                removeLast();
            }
            //创建一个新的缓存节点
            node = new CacheNode();
            node.key = key;
        }
        node.value = value;
        moveToFirst(node);
        cache.put(key, node);
        //重新计算cache的大小
        currentCacheSize = cache.size();
    }

    /**
     * 根据key去获取value
     *
     * @param key cache的key值
     * @return 返回cache中key对应的value
     */
    public Object get(K key) {
        CacheNode cacheNode = cache.get(key);
        if (cacheNode == null) {
            return null;
        }
        //将找到的cache节点进行前移操作,并返回value值
        moveToFirst(cacheNode);
        return cacheNode.value;
    }

    /**
     * 清空操作
     */
    public void clear() {
        cache.clear();
        first = null;
        last = null;
        cacheCapcity = 0;
    }

    /**
     * 根据key进行remove操作
     *
     * @param key
     * @return
     */
    public Object remove(K key) {
        CacheNode cacheNode = cache.get(key);
        if (cacheNode != null) {
            if (null != cacheNode.pre) {
                cacheNode.pre.next = cacheNode.next;
            }
            if (null != cacheNode.next) {
                cacheNode.next.pre = cacheNode.pre;
            }
            if (cacheNode == first) {
                first = cacheNode.next;
            }
            if (cacheNode == last) {
                last = cacheNode.pre;
            }
        }
        currentCacheSize--;
        return cache.remove(key);
    }

    /**
     * 将节点移动到最前
     *
     * @param node 被移动的cache节点
     */
    private void moveToFirst(CacheNode node) {
        if (first == node) {
            return;
        }
        if (node.next != null) {
            node.next.pre = node.pre;
        }
        if (node.pre != null) {
            node.pre.next = node.next;
        }
        if (node == last) {
            last = last.pre;
        }
        if (first == null || last == null) {
            first = last = node;
            return;
        }

        node.next = first;
        first.pre = node;
        first = node;
        first.pre = null;
    }

    /**
     * 移除最末尾的操作,在cache达到容量时进行的操作
     */
    private void removeLast() {
        if (last != null) {
            cache.remove(last.key);
            last = last.pre;
            if (last.pre == null) {
                first = null;
            }
            last.next = null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        CacheNode node = first;
        while (node != null) {
            sb.append(String.format("%s:%s ", node.key, node.value));
            node = node.next;
        }

        return sb.toString();
    }

    /**
     * cache的节点内部类
     */
    class CacheNode {
        CacheNode next;
        CacheNode pre;
        Object key;
        Object value;

        public CacheNode() {
        }
    }

    public static void main(String[] args) {

        LRUCache<Integer, String> lru = new LRUCache<Integer, String>(3);

        lru.put(1, "a");    // 1:a
        System.out.println(lru.toString());
        lru.put(2, "b");    // 2:b 1:a
        System.out.println(lru.toString());
        lru.put(3, "c");    // 3:c 2:b 1:a
        System.out.println(lru.toString());
        lru.put(4, "d");    // 4:d 3:c 2:b
        System.out.println(lru.toString());
        lru.put(5, "e");    // 5:e 4:d 3:c
        System.out.println(lru.toString());
        lru.put(1, "aa");   // 1:aa 4:d 3:c
        System.out.println(lru.toString());
        lru.put(2, "bb");   // 2:bb 1:aa 4:d
        System.out.println(lru.toString());
        lru.put(5, "e");    // 5:e 2:bb 1:aa
        System.out.println(lru.toString());
        lru.get(1);         // 1:aa 5:e 2:bb
        System.out.println(lru.toString());
        lru.remove(11);     // 1:aa 5:e 2:bb
        System.out.println(lru.toString());
        lru.remove(1);      //5:e 2:bb
        System.out.println(lru.toString());
        lru.put(1, "aaa");  //1:aaa 5:e 2:bb
        System.out.println(lru.toString());
    }
}
