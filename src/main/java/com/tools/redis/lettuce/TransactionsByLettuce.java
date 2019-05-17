package com.tools.redis.lettuce;

import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class TransactionsByLettuce {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RedisClient client = RedisClient.create(LettuceTools.URL.getValue());


        /**
         *
         * Transactions using the asynchronous API
         *
         * 与非事务方式接近，同样是返回RedisFuture，可以对这个返回的future使用与
         * 非事务方式同样的操作
         */
        RedisAsyncCommands<String, String> async = client.connect().async();

        async.multi();

        async.set("key3", "value3");

        RedisFuture<String> set = async.get("key5");

        RedisFuture<TransactionResult> future = async.exec();

        TransactionResult objects = future.get();
        log.info("第一次返回为{}, 第二次返回为{}", set.get(), objects.get(1));
        if (objects.get(0) == set.get()) {
            log.info("结果相同");
        }


        /**
         * Transactions using the reactive API
         *
         * 使用react api可以在一步执行多个命令
         *
         * 以下代码启动事务，在事务中执行两个命令，最后执行事务
         */

        RedisReactiveCommands<String, String> reactive = client.connect().reactive();

        reactive.multi().subscribe(multiResponse -> {
            reactive.set("key", "1").subscribe();
            reactive.incr("key").subscribe();
            reactive.exec().subscribe();
        });

        /**
         * Transactions on clustered connections
         *
         * 默认情况下，集群会自动路由，意味着你不能确定你的命令是在
         * 那一台节点上执行的，所以当执行在集群环境时，使用普通的事务
         * 命令即可。
         */

        RedisCommands<String, String> redis = client.connect().sync();
        redis.multi();
        redis.set("one", "1");
        redis.set("two", "2");
        redis.mget("one", "two");
        redis.llen("key");

        redis.exec(); // result: list("OK", "OK", list("1", "2"), 0L)


        /**
         * Mult executing multiple asynchronous commands
         */
        RedisAsyncCommands<String, String> async1 = client.connect().async();
        async1.multi();
        RedisFuture<String> set1 = async1.set("one", "1");
        RedisFuture<String> set2 = async1.set("two", "2");
        RedisFuture<List<KeyValue<String, String>>> mget = async1.mget("one", "two");
        RedisFuture<Long> llen = async1.llen("key");

        set1.thenAccept(value -> log.info(value)); // OK

        RedisFuture<TransactionResult> exec = async1.exec(); // result: list("OK", "OK", list("1", "2"), 0L)
        exec.thenAccept(value -> log.info(value.get(0)));

    }
}
