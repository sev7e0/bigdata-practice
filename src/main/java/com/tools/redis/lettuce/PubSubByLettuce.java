package com.tools.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class PubSubByLettuce {

    private static Logger logger = LoggerFactory.getLogger(PubSubByLettuce.class);

    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create(LettuceTools.URL.getValue());

        /**
         * 同步订阅 synchronous subscription
         */
        StatefulRedisPubSubConnection<String, String> pubSub = redisClient.connectPubSub();

        pubSub.addListener(new MyListener());

        RedisPubSubCommands<String, String> sync = pubSub.sync();

        sync.subscribe("channel");


        /**
         * 异步订阅 asynchronous subscription
         */
        StatefulRedisPubSubConnection<String, String> pubSub1 = redisClient.connectPubSub();

        pubSub1.addListener(new MyListener());

        RedisPubSubAsyncCommands<String, String> async = pubSub1.async();

        //异步将会返回future
        RedisFuture<Void> future = async.subscribe("channel");

        future.whenComplete((s,th)->{
            if (th instanceof Exception){
                logger.info(th.getMessage());
            }
        });


        /**
         * 使用reactive订阅
         */

        StatefulRedisPubSubConnection<String, String> pubSub2 = redisClient.connectPubSub();

        RedisPubSubReactiveCommands<String, String> reactive = pubSub2.reactive();

        reactive.subscribe("channel").subscribe();

        //将会接收到所有进来的消息，可以进行过滤操作，observe会在取消订阅时停止。
        reactive.observeChannels().doOnNext(message-> logger.info(message.getMessage())).subscribe();


        /**
         *
         */

    }
}
