package com.zookeeper.server;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class DistributeServer {

	//zookeeper连接地址
	private static final String connectString = "spark01:2181,spark02:2181,spark03:2181";
	//超时时间  用于zookeeper判断当前节点等待心跳最长时间
	private static final Integer sessionTimeout = 2000;
	//根节点
	private static final String parentNode = "/Servers";

	private ZooKeeper zk = null;

	/**
	 * 1.getconnect 2.regserver 3.service
	 * 
	 * @throws IOException
	 */

	public void getConnect() throws Exception {
		zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			public void process(WatchedEvent event) {

			}
		});
	}

	/**
	 * 
	* @Title: regServer  
	* @Description:  向zookeeper注册服务
	* @param @param hostName
	* @param @throws Exception
	* @param @throws InterruptedException   
	* @return void      
	* @throws
	 */
	public void regServer(String hostName) throws Exception, InterruptedException {
		String creatPath = zk.create(parentNode + "/server", hostName.getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println(hostName + "-------- is on line-----" + creatPath);
	}

	public void handleService(String hostName) {
		System.out.println(hostName + " start working");
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DistributeServer distributeServer = new DistributeServer();

		distributeServer.getConnect();

		distributeServer.regServer(args[0]);

		distributeServer.handleService(args[0]);

		Thread.sleep(Long.MAX_VALUE);
	}

}
