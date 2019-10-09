package com.tools.hadoop.hdfs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;


public class HDFSStreamAccess {
	
	FileSystem fileSystem = null;

	Configuration configuration = null;
	@Before
	public void init() throws Exception {
	    configuration = new Configuration();
		fileSystem = FileSystem.get(new URI("hdfs://spark01:9000"),configuration,"hadoopadmin");
		
	}
	
	
	/**  
	* @Title: upload  
	* @Description: 通过流的方式上传文件
	* @param @throws IllegalArgumentException
	* @param @throws IOException   
	* @return void      
	* @throws  
	*/  
	@Test
	public void  uploadByIO() throws IllegalArgumentException, IOException {
		FSDataOutputStream dataOutputStream = fileSystem.create(new Path("/eclipse/hbase.tar.gz"),true);
		FileInputStream fileInputStream = new FileInputStream("/home/sev7e0/bigdata/hbase-2.0.1-src.tar.gz");
		org.apache.commons.compress.utils.IOUtils.copy(fileInputStream, dataOutputStream);
	}
	
	/**  
	* @Title: downloadByIO  
	* @Description: 通过流的方式下载文件
	* @param @throws IllegalArgumentException
	* @param @throws IOException   
	* @return void      
	* @throws  
	*/  
	@Test
	public void downloadByIO() throws IllegalArgumentException, IOException {
		FSDataInputStream open = fileSystem.open(new Path("/eclipse/hbase.tar.gz"));
		
		FileOutputStream fileOutputStream = new FileOutputStream("/home/sev7e0/bigdata/hbase-src.tar.gz");
		
		org.apache.commons.compress.utils.IOUtils.copy(open, fileOutputStream);
	}
	
	
	/**  
	* @Title: downloadByIOToDisplay  
	* @Description: 通过流的方式将文件打印到屏幕上
	* @param @throws IllegalArgumentException
	* @param @throws IOException   
	* @return void      
	* @throws  
	*/  
	@Test
	public void downloadByIOToDisplay() throws IllegalArgumentException, IOException {
		FSDataInputStream open = fileSystem.open(new Path("/eclipse/hbase.tar.gz"));
		
		org.apache.commons.compress.utils.IOUtils.copy(open, System.out);
	}
	
	
	
	
	
	
	
	
	
	

}
