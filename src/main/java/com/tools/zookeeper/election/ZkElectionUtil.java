package com.tools.zookeeper.election;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.Arrays;
import java.util.Objects;


@Slf4j
public class ZkElectionUtil {
    private static final String CONNECTSTRING = "localhost:2181";
    private static final int SESSIONTIMEOUT = 2000;
    private static final String LOCKNODE = "/rootNode/lock";
    private static final CuratorFramework client;
    // 初始化客户端
    static {
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(SESSIONTIMEOUT, 3);
        client = CuratorFrameworkFactory.newClient(CONNECTSTRING, exponentialBackoffRetry);
        client.start();
    }

    /**
     * 创建节点
     * @param data
     * @return
     */
    private boolean getLock(byte[] data) {
        try {
            if (Objects.isNull(client.checkExists().forPath(LOCKNODE))) {
                client.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(LOCKNODE, data);
            } else {
                return false;
            }
        } catch (Exception e) {
            log.warn("create node path fail, reason: {}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 选主
     * @param data
     * @throws Exception
     */
    void electionMaster(byte[] data) throws Exception {
        //尝试创建zk临时节点
        if (getLock(data)) {
            log.info("now you are leader");
        } else {
            log.warn("now you are follower,  leader was: {}", getLeader());
            client.getData()
                    // 每次选举失败，重新注册节点监听事件
                    .usingWatcher((CuratorWatcher) event -> {
                        log.info("leader node was changed, will start election");
                        // 递归调用
                        electionMaster(data);
                    })
                    .forPath(LOCKNODE);
        }
    }

    /**
     * 获取创建成功的数据
     * @return
     */
    private String getLeader() {
        try {
            return Arrays.toString(client.getData().forPath(LOCKNODE));
        } catch (Exception e) {
            log.error("get leader error: {}",e.getMessage());
        }
        return "no leader";
    }

}
