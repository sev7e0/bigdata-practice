package com.tools.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Title:  RedissonDelayQueue.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2021-01-25 11:56
 **/

public class RedissonDelayQueueConsumer {

	static final Logger logger = LoggerFactory.getLogger(RedissonDelayQueueConsumer.class);

	public static void main(String[] args) throws InterruptedException {
		final Config config = new Config();
		config.useSingleServer().setAddress("redis://localhost:6379/0");
		final RedissonClient redissonClient = Redisson.create(config);

		final RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue("delay_queue");

		while (true) {
			final String poll = blockingQueue.poll(2, TimeUnit.SECONDS);
			if (Objects.isNull(poll)) {
				continue;
			}
			logger.info("消息出队队，内容：{}", poll);
		}
	}
}
