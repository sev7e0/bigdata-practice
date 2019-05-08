package com.tools.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class QuickStartByLettuce {
    private static final Logger logger = LoggerFactory.getLogger(QuickStartByLettuce.class);

    private final static String URI = "redis://localhost:6379/0";

    public static void main(String[] args) {

        /**
         * 同步方式
         */

        logger.info("使用同步API执行命令");
        //创建RedisClient实例
        RedisClient redisClient = RedisClient.create(URI);

        //创建redis连接
        StatefulRedisConnection<String, String> connect = redisClient.connect();

        //获取用于同步执行的命令API。lettuce也支持异步(async())和反应式执行模型(reactive())。
        //返回RedisCommands
        RedisCommands<String, String> redisCommands = connect.sync();

        //可直接使用redis的command
        redisCommands.set("test", "value");

        logger.info(redisCommands.get("test"));


        /**
         * 需要手动关闭连接，连接默认设计为长连接且线程安全
         * 当前链接失效时会自动重连，一直到close()被调用
         */
        connect.close();






        /**
         * 异步方式
         */

        logger.info("使用异步API执行命令");
        StatefulRedisConnection<String, String> aconnect = redisClient.connect();

        RedisAsyncCommands<String, String> asyncCommands = aconnect.async();

        asyncCommands.set("async", "command");

        //在lettuce中使用异步API执行command将会返回RedisFuture，
        //他是继承自CompletionStage，可以取消(cancel())，也可以查询执行状态(isDone(),isCancelled())
        RedisFuture<String> async = asyncCommands.get("async");

        try {

            //可以从RedisFuture获取到返回的结果。
            logger.info(async.get());

            //将会等待10秒，再去获取RedisFuture返回的值
            //超时将会抛出TimeoutException
            logger.info(async.get(5, TimeUnit.SECONDS));

            //有结果返回时将会调用。
            async.thenAccept(s -> logger.info(s));

            //有结果返回后，使用异步线程执行
            async.thenAcceptAsync(s -> logger.info("返回后使用异步线程执行"));

        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
        } catch (TimeoutException e) {
            logger.error(e.getMessage());
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /**
         * 同步使用future，暂未完成
         * https://github.com/lettuce-io/lettuce-core/wiki/Asynchronous-API#synchronizing-futures
         */




        //关闭实例，释放线程和资源。
        redisClient.shutdown();
    }
}
