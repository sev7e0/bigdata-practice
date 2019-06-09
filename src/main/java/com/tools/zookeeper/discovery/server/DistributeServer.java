package com.tools.zookeeper.discovery.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class DistributeServer {

    //zookeeper连接地址
    private static final String connectString = "localhost:2181";
    //超时时间  用于zookeeper判断当前节点等待心跳最长时间
    private static final Integer sessionTimeout = 2000;
    //根节点  ------ 启动前需要手动创建
    private static final String parentNode = "/Servers";

    private ZooKeeper zk = null;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);


    /**
     * 创建链接
     * @throws Exception
     */
    public void getConnect() throws Exception {
        zk = new ZooKeeper(connectString, sessionTimeout, event -> {
            log.info("链接状态更改----"+event.getState());
            countDownLatch.countDown();
        });
        /**
         * 由于创建是异步的可能会导致链接未创建就执行
         * 所以这里使用CountDownLatch进行阻塞
         *
         * 在链接创建后使用Watch进行解除阻塞。
         */
        countDownLatch.await();
        //根据两个能够确定一个会话，可以实现客户端会话复用
        log.info("sessionId为：{}",zk.getSessionId());
        log.info("会话密钥为：{}",zk.getSessionPasswd());


    }

    /**
     * @param @param  hostName
     * @param @throws Exception
     * @param @throws InterruptedException
     * @return void
     * @throws
     * @Title: regServer
     * @Description: 向zookeeper注册服务
     */
    public void regServer(String hostName) throws Exception {
        //支持异步创建，不支持递归创建，即不存在父节点的情况下不可以创建
        String creatPath = zk.create(parentNode + "/server", hostName.getBytes(), Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info("{}-------- is on line-----{}",hostName,creatPath);
    }

    /**
     *
     * @param hostName
     */
    public void handleService(String hostName) {
        log.info("{} start working",hostName);
    }

    public static void main(String[] args) throws Exception {

        DistributeServer distributeServer = new DistributeServer();

        distributeServer.getConnect();

        distributeServer.regServer(args[0]);

        distributeServer.handleService(args[0]);

        Thread.sleep(Long.MAX_VALUE);
    }

}
