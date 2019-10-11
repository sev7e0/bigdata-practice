package com.tools.hadoop.hdfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.stream.Stream;

@Slf4j
public class HDFSClient {

    private FileSystem fileSystem = null;
    private Configuration configuration = null;

    private static final String DFS_PATH = "/hadoop/yarn-site.xml";
    private static final String LOCAL_PATH = "src/main/java/com/tools/hadoop/config/yarn-site.xml";

    @Before
    public void init() throws Exception {
        configuration = new Configuration();

        // ******注意：以上配置都是由由就近原则进行配置调用******
        // configuration.set > 自定义配置文件 > jar中配置文件 >  服务端配置
        fileSystem = FileSystem.get(new URI("hdfs://spark02:9000"), configuration, "hadoopadmin");
        // 问题：配置HA，为什么在链接standby的namenode时会报错。

    }

    //流方式下载
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


    // client方式
    /**
     * download file
     */
    @Test
    public void downloadCommand() throws IllegalArgumentException, IOException {
        //在文件拷贝与上传的过程中路径要精确到文件名
        fileSystem.copyToLocalFile(new Path(DFS_PATH), new Path(LOCAL_PATH));
        fileSystem.close();
    }


    /**
     * upload file
     */
    @Test
    public void uploadCommand() {
        try {
            // hdfs路径要精确到文件名。
            fileSystem.copyFromLocalFile(new Path(LOCAL_PATH), new Path(DFS_PATH));
            fileSystem.close();
        } catch (IOException e) {
            log.error("upload file to hdfs failed :{}", e.getMessage());
        }
    }

    /**
     * get cluster config
     */
    @Test
    public void getConfiguration() {
        Iterator<Entry<String, String>> iterator = configuration.iterator();
        while (iterator.hasNext()) {
            System.out.println("name:" + iterator.next().getKey() + "  ----  value:" + iterator.next().getValue());
        }
        try {
            fileSystem.close();
        } catch (IOException e) {
            log.error("fileSystem close error :{}", e.getMessage());
        }
    }

    /**
     * mkdir on hdfs
     */
    @Test
    public void mkdirOnHDFS() {
        try {
            Boolean mkdirRes = fileSystem.mkdirs(new Path(DFS_PATH));
            fileSystem.close();
            System.out.println(mkdirRes);
        } catch (IOException e) {
            log.error("make directory on hdfs failed :{}", e.getMessage());
        }
        /**
         * output:
         *  true/false
         */
    }


    /**
     * delete file or directory
     */
    @Test
    public void deleteFromHDFS() {
        try {
            System.out.println(fileSystem.delete(new Path(DFS_PATH), true));
            fileSystem.close();
        } catch (IOException e) {
            log.error("delete file or directory failed :{}", e.getMessage());
        }
        /**
         * output:
         *  true/false
         */
    }


    /**
     *
     * get all file or dir at path
     */
    @Test
    public void listFileFromHDFS(){
        FileStatus[] fs = new FileStatus[0];
        try {
            fs = fileSystem.listStatus(new Path("/hadoop"));
        } catch (IOException e) {
            log.error("get all file or dir error :{}",e.getMessage());
        }
        Path[] listPath = FileUtil.stat2Paths(fs);
        Stream.of(listPath).forEach(path -> System.out.println(path.getName()));
        /**
         * output：
         *   core-site.xml
         *   hdfs-site.xml
         * */
    }
}
