package com.tools.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.pubsub.StatefulRedisClusterPubSubConnection;
import io.lettuce.core.cluster.pubsub.api.async.RedisClusterPubSubAsyncCommands;
import io.lettuce.core.cluster.pubsub.api.sync.RedisClusterPubSubCommands;
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
         * 在redis集群中使用订阅功能
         *
         * 在redis集群中可以是用订阅，但有几点需要注意：
         *
         * 用户在集群的一个节点上发布消息，集群会自动向所有节点广播，不论这台机器是否订阅了
         * 这个channel，这也就表示在集群中订阅消息时，不需要连接指定的消息发布的节点，任意
         * 任意一个节点都可以。
         */


        RedisClusterClient clusterClient = RedisClusterClient.create(LettuceTools.URL.getValue());

        StatefulRedisClusterPubSubConnection<String, String> connection = clusterClient.connectPubSub();

        //同步
        RedisClusterPubSubCommands<String, String> sync1 = connection.sync();

        sync1.subscribe("channel");

        //异步
        RedisClusterPubSubAsyncCommands<String, String> async1 = connection.async();

        async1.subscribe("channel");




        StatefulRedisClusterPubSubConnection<String, String> connection0 = clusterClient.connectPubSub();
        connection0.addListener(new MyListener());
        connection0.setNodeMessagePropagation(true);
        RedisClusterPubSubCommands<String, String> sync2 = connection0.sync();
        sync2.masters().commands().subscribe("__keyspace@0__:*");


        /**
         * 注意事项
         *
         * 复制到副本节点的键，特别是考虑到到期，会在保存该键的所有节点上生成键空间事件。如果一个密钥过期并被复制，它将在主副
         * 本和所有副本上过期。每个redis服务器都会发出keyspace事件。因此，订阅非主节点将使您的应用程序看到同一个密钥的同一类
         * 型的多个事件，因为redis是分布式的。
         *
         * 订阅可以通过使用nodeselection api或对单个集群节点连接调用subscribe（…）来发出。订阅注册不会传播到拓扑更改时添
         * 加的新节点。
         */
    }
}
