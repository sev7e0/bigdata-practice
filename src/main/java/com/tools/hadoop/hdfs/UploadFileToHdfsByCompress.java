package com.tools.hadoop.hdfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class UploadFileToHdfsByCompress {

    private static final String DFS_PATH = "/hadoop/yarn-site.xml";
    private static final String LOCAL_PATH = "src/main/java/com/tools/hadoop/config/yarn-site.xml";
    private static final String url = "hdfs://spark02:9000";

    @Test
    public void uploadByCompress() {
        Configuration configuration = new Configuration();
        BZip2Codec codec = new BZip2Codec();
        codec.setConf(configuration);
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(LOCAL_PATH));
            FileSystem fileSystem = FileSystem.get(URI.create(url), configuration, "hadoopadmin");
            FSDataOutputStream outputStream = fileSystem.create(new Path(DFS_PATH));

            CompressionOutputStream codecOutputStream = codec.createOutputStream(outputStream);
            IOUtils.copyBytes(inputStream, codecOutputStream, configuration);
            log.info("upload success, local path: {}, hdfs path: {}", LOCAL_PATH, DFS_PATH);
        } catch (InterruptedException | IOException e) {
            log.error("upload error:{}", e.getMessage());
        }

    }
}
