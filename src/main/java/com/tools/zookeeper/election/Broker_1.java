package com.tools.zookeeper.election;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Broker_1 {
    private static final CountDownLatch shutdownLatch = new CountDownLatch(1);
    private static final String name = "Broker_1";
    public static void main(String[] args) throws InterruptedException {
        CuratorFramework connect = ZkElectionUtil.getConnect();

        try {
            ZkElectionUtil.electionMaster(connect, name.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("now leader is {}",new String(ZkElectionUtil.getLeader(connect)));

        shutdownLatch.await();
    }
}
