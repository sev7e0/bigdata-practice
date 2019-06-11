package com.tools.zookeeper.election;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Objects;


@Slf4j
public class ZkElectionUtil {
    private static final String CONNECTSTRING = "localhost:2181";
    private static final int SESSIONTIMEOUT = 200;
    private static final String PARENTNODE = "/rootNode";
    private static final String LOCKNODE = PARENTNODE+"/lock";


    /**
     * 获取连接
     * @return
     */
    public static CuratorFramework getConnect(){

        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(SESSIONTIMEOUT, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient(CONNECTSTRING, exponentialBackoffRetry);

        client.start();

        return client;
    }

    /**
     * 确保root节点一定存在
     * @param client
     */
    private static void ensureRootPath(CuratorFramework client){
        try {
            Stat stat = client.checkExists().forPath(PARENTNODE);
            if (Objects.isNull(stat)){
                client.create()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(PARENTNODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean createLocalPath(CuratorFramework client, byte[] data){
        try {
            Stat stat = client.checkExists().forPath(LOCKNODE);
            if (Objects.isNull(stat)){
                client.create()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(LOCKNODE);
            }else {
                return false;
            }
        } catch (Exception e) {
            log.error("create node path fail,reason: {}", e.getMessage());
            return false;
        }
        return true;
    }

    public static void electionMaster(CuratorFramework client, byte[] data) throws Exception {
        ensureRootPath(client);
        boolean res = createLocalPath(client, data);
        if (res){
            log.info("now your is leader");
        }else {
            log.warn("election fail");
            getLeader(client);
            client.getData()
                    .usingWatcher((CuratorWatcher) event -> {
                        log.info("leader node was change, will be start election");
                        electionMaster(client, data);})
                    .forPath(LOCKNODE);
        }
    }

    public static byte[] getLeader(CuratorFramework client){
        try {
            return client.getData().forPath(LOCKNODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

}
