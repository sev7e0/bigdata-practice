package com.tools.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class ReadWrite {

    private Configuration configuration = null;

    private Connection connection = null;

    public static void main(String[] args) throws IOException {
        ReadWrite readWrite = new ReadWrite();
        readWrite.init();
        readWrite.creatTable("table0814", "family0814");
        readWrite.insert("table0814", "11", "family0814", "time_str", "data0814");
        readWrite.scan("table0814", "family0814", "time_str");
    }

    /**
     * 初始化连接
     *
     * @throws IOException
     */
    private void init() throws IOException {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "localhost");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        connection = ConnectionFactory.createConnection(configuration);
    }

    /**
     * 创建表
     *
     * @param tableN  表名
     * @param familyN 列族
     * @throws IOException
     */
    private void creatTable(String tableN, String familyN) throws IOException {
        Admin admin = connection.getAdmin();
        TableName tableName = TableName.valueOf(tableN);
        if (admin.tableExists(tableName)) {
            // hbase 在删除表之前要先 disable
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }
        HTableDescriptor descriptor = new HTableDescriptor(tableName);
        descriptor.addFamily(new HColumnDescriptor(Bytes.toBytes(familyN)));
        admin.createTable(descriptor);
    }

    /**
     * 插入数据
     *
     * @param tableN     表名
     * @param rowId      row id
     * @param familyName 列族
     * @param qualifier  列
     * @param value      数据
     * @throws IOException
     */
    private void insert(String tableN, String rowId, String familyName, String qualifier, String value) throws IOException {
        TableName tableName = TableName.valueOf(tableN);
        Table table = connection.getTable(tableName);
        Put put = new Put(Bytes.toBytes(rowId));
        put.addColumn(familyName.getBytes(), qualifier.getBytes(), value.getBytes())
                .addColumn(familyName.getBytes(), qualifier.getBytes(), value.getBytes());
        table.put(put);
    }

    /**
     * @param tableN    表名
     * @param familyN   列族
     * @param qualifier 列
     * @throws IOException
     */
    private void scan(String tableN, String familyN, String qualifier) throws IOException {
        TableName tableName = TableName.valueOf(tableN);
        Table table = connection.getTable(tableName);
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        scanner.forEach(data -> System.out.println((Bytes.toString(data.getValue(familyN.getBytes(), qualifier.getBytes())))));
    }
}
