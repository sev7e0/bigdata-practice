package com.tools.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class ReadWrite {

    private static Configuration configuration = null;

    private static Connection connection = null;

    private void init() throws IOException {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","localhost");
        configuration.set("hbase.zookeeper.property.clientPort","2181");
        connection = ConnectionFactory.createConnection(configuration);
    }

    private void creatTable(String tableN) throws IOException {
        Admin admin = connection.getAdmin();
        TableName tableName = TableName.valueOf(tableN);
        if (admin.tableExists(tableName)){
            admin.deleteTable(tableName);
        }
        HTableDescriptor descriptor = new HTableDescriptor(tableName);
        descriptor.addFamily(new HColumnDescriptor(Bytes.toBytes("family0813")));
        admin.createTable(descriptor);
    }

    public static void main(String[] args) throws IOException {
        ReadWrite readWrite = new ReadWrite();
        readWrite.init();
        readWrite.creatTable("table0813");
    }
}
