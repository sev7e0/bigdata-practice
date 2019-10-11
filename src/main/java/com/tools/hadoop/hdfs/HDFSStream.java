package com.tools.hadoop.hdfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class HDFSStream {

    private FileSystem fileSystem = null;

    private Configuration configuration = null;

    private static final String DFS_PATH = "/hadoop/yarn-site.xml";
    private static final String LOCAL_PATH = "src/main/java/com/tools/hadoop/config/yarn-site.xml";

    @Before
    public void init() throws Exception {
        configuration = new Configuration();
        fileSystem = FileSystem.get(new URI("hdfs://spark02:9000"), configuration, "hadoopadmin");
    }


    /**
     * upload file by stream
     */
    @Test
    public void uploadByIO() throws IllegalArgumentException, IOException {
        FSDataOutputStream dataOutputStream = fileSystem.create(new Path(DFS_PATH), true);
        FileInputStream fileInputStream = new FileInputStream(LOCAL_PATH);
        IOUtils.copy(fileInputStream, dataOutputStream);
    }

    /**
     * download file by stream
     */
    @Test
    public void downloadByIO() throws IllegalArgumentException, IOException {
        FSDataInputStream open = fileSystem.open(new Path(DFS_PATH));
        FileOutputStream fileOutputStream = new FileOutputStream(LOCAL_PATH);
        IOUtils.copy(open, fileOutputStream);
    }

    /**
     * 通过流的方式将文件打印到屏幕上
     */
    @Test
    public void downloadByIOToDisplay() throws IllegalArgumentException, IOException {
        FSDataInputStream open = fileSystem.open(new Path(DFS_PATH));
        IOUtils.copy(open, System.out);
    }


}
