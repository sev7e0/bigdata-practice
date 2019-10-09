package com.tools.hadoop.hdfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.hdfs.tools.HDFSConcat;
import org.junit.Before;
import org.junit.Test;

public class HDFSClientDemo {

	FileSystem fileSystem = null;

	Configuration configuration = null;
	@Before
	public void init() throws Exception {
	    configuration = new Configuration();
//		configuration.set("fs.defaultFS","hdfs://spark01:9000" );
		//设置上传目标文件的副本数，
//		configuration.set("dfs.replication","5");
//		configuration.set("fs.file.impl","org.apache.hadoop.fs.LocalFileSystem" );
//		configuration.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem" );
		//******注意：以上配置都是由由就近原则进行配置调用******
		//con.set > 自定义配置文件 > jar中配置文件 >  服务端配置
		
		//可以在获取fileSystem对象时，可以配置uri 和用户名
		fileSystem = FileSystem.get(new URI("hdfs://spark01:9000"),configuration,"hadoopadmin");
		
	}

	/**
	 * 
	* @Title: downloadCommod  
	* @Description: 从hdfs中拷贝文件到其他文件系统
	* @param @throws IllegalArgumentException
	* @param @throws IOException   
	* @return void      
	* @throws
	 */
	@Test
	public void downloadCommod() throws IllegalArgumentException, IOException {
		//在文件拷贝与上传的过程中路径要精确到文件名
		fileSystem.copyToLocalFile(new Path("/eclipse/upload01"), new Path("/home/sev7e0/bigdata/hbase-2.0.1-src.tar.gz"));
		fileSystem.close();
	}
	
	/**
	 * 
	* @Title: uploadCommond  
	* @Description: 从其他文件系统上传文件到hdfs中
	* @param @throws Exception
	* @param @throws IOException   
	* @return void      
	* @throws
	 */
	@Test
	public void uploadCommond() throws Exception, IOException {
		fileSystem.copyFromLocalFile(new Path("/home/sev7e0/bigdata/hbase-2.0.1-src.tar.gz"),
				//没有精确到文件名，导致上传的文件名是upload01
				new Path("/eclipse/upload02/hbase-2.0.1-src.tar.gz"));
		fileSystem.close();
	}
	/**
	 * 
	* @Title: getConfigeration  
	* @Description: 获取服务器相关配置信息
	* @param    
	* @return void      
	* @throws
	 */
	@Test
	public void getConfigeration() {
		Iterator<Entry<String, String>> iterator = configuration.iterator();
		while(iterator.hasNext()) {
			System.out.println("name:"+iterator.next().getKey()+"  value:"+iterator.next().getValue());
		}
	}

	/**
	 * 
	* @Title: mkdirOnHDFS  
	* @Description: 通过java客户端创建hdfs文件目录
	* @param @throws IllegalArgumentException
	* @param @throws IOException   
	* @return void      
	* @throws
	 */
	@Test
	public void mkdirOnHDFS() throws IllegalArgumentException, IOException {
		Boolean mkdirRes =  fileSystem.mkdirs(new Path("/eclipse/0819testdir/"));
		System.out.println(mkdirRes);
	}
	/**
	 * 
	* @Title: deleteFromHDFS  
	* @Description: 删除目录及文件
	* @param @throws IllegalArgumentException
	* @param @throws IOException   
	* @return void      
	* @throws
	 */
	@Test
	public void deleteFromHDFS() throws IllegalArgumentException, IOException {
//		Boolean boolean1 =  fileSystem.delete(new Path("/eclipse/0819testdir/"));
		Boolean boolean1 =  fileSystem.delete(new Path("/eclipse/0819testdir/"),true);
		System.out.println(boolean1);
	}
	/**
	 * 
	* @Title: listFileFromHDFS  
	* @Description:  获取文件目录
	* @param @throws FileNotFoundException
	* @param @throws IllegalArgumentException
	* @param @throws IOException   
	* @return void      
	* @throws
	 */
	@Test
	public void listFileFromHDFS() throws FileNotFoundException, IllegalArgumentException, IOException {
		RemoteIterator<LocatedFileStatus> list = fileSystem.listFiles(new Path("/eclipse"),true);
		
		while(list.hasNext()) {
			System.out.println(list.next().getBlockSize());
			System.out.println(list.next().getGroup());
			System.out.println(list.next().isDirectory());
//			System.out.println(list.next().isFile());
			System.out.println(list.next().getPermission());
			System.out.println("---------");
		}
	}
	
	
	/**
	 * 
	* @Title: listFileStatus  
	* @Description: 获取文件状态
	* @param @throws FileNotFoundException
	* @param @throws IllegalArgumentException
	* @param @throws IOException   
	* @return void      
	* @throws
	 */
	@Test
	public void listFileStatus() throws FileNotFoundException, IllegalArgumentException, IOException {
		FileStatus[] fileStatus =  fileSystem.listStatus(new Path("/eclipse"));
		for (int i = 0; i < fileStatus.length; i++) {
			System.out.println(fileStatus[i].isDirectory());
		}
	}
	
	
	
	
	
	
	
	
}
