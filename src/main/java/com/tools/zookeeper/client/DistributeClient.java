package com.tools.zookeeper.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.ZooKeeper;

public class DistributeClient {

	private static final String connectString = "spark01:2181,spark02:2181,spark03:2181";

	private static final Integer sessionTimeout = 2000;

	private static final String parentNode = "/Servers";

	private ZooKeeper zk = null;

	private volatile List<String> serverList = null;

	public void getConnect() throws Exception {
		zk = new ZooKeeper(connectString, sessionTimeout, event -> {
			try {
				getServerList();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public void getServerList() throws Exception {
		List<String> children = zk.getChildren(parentNode, true);

		List<String> list = new ArrayList<String>();

		for (String child : children) {
			byte[] data = zk.getData(parentNode + "/" + child, false, null);
			list.add(new String(data));
		}
		serverList = list;
		handllerService();
	}

	public void handllerService() {
		System.out.println(serverList);
	}

	public static void main(String[] args) throws Exception {
		DistributeClient client = new DistributeClient();
		client.getConnect();
		Thread.sleep(Long.MAX_VALUE);
	}

}
