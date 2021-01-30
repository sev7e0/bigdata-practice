package com.tools.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Title:  RedissonDelayQueue.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2021-01-25 11:56
 **/

public class RedissonDelayQueue {

	static final Logger logger = LoggerFactory.getLogger(RedissonDelayQueue.class);

	public static void main(String[] args) throws InterruptedException {
		final Config config = new Config();
		config.useSingleServer().setAddress("redis://localhost:6379/0");
		final RedissonClient redissonClient = Redisson.create(config);


		for (int i = 0; i < 100; i++) {
			final RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue("delay_queue");

			final RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
			final String s = "obj test666:" + i;
			delayedQueue.offer(s, 100-i, TimeUnit.SECONDS);
			logger.info("消息入队，内容：{},时间 ：{}", s, i);
			delayedQueue.destroy();
		}

	}
}
