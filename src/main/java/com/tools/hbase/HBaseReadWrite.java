package com.tools.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

@Slf4j
public class HBaseReadWrite {

    private Connection connection = null;

    public static void main(String[] args) throws IOException {
        HBaseReadWrite readWrite = new HBaseReadWrite();
        readWrite.init();
        readWrite.creatTable();
        for (int i = 0; i < 10000; i++) {
            long stamp = System.currentTimeMillis();
            String data = "data_"+stamp;
            System.out.println("insert data :"+data+"");
            readWrite.insert(HBaseTestUtil.getTableName(null), String.valueOf(stamp), HBaseTestUtil.getFamilyName(null), "data_stamp".getBytes(), data);
        }

        readWrite.scan(HBaseTestUtil.getTableName(null), HBaseTestUtil.getFamilyName(null), "data_stamp");
    }

    /**
     * 初始化连接
     *
     * @throws IOException
     */
    private void init() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "spark01");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        connection = ConnectionFactory.createConnection(configuration);
    }

    /**
     * 创建表
     *
     * @throws IOException
     */
    private void creatTable() throws IOException {
        Admin admin = connection.getAdmin();
        TableName tableName = TableName.valueOf(HBaseTestUtil.getTableName(null));
        if (admin.tableExists(tableName)) {
            // hbase 在删除表之前要先 disable
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }
        HTableDescriptor descriptor = new HTableDescriptor(tableName);
        descriptor.addFamily(new HColumnDescriptor(HBaseTestUtil.getFamilyName(null)));
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
    private void insert(byte[] tableN, String rowId, byte[] familyName, byte[] qualifier, String value) throws IOException {
        TableName tableName = TableName.valueOf(tableN);
        Table table = connection.getTable(tableName);
        Put put = new Put(rowId.getBytes());
        put.addColumn(familyName, qualifier, value.getBytes());
        table.put(put);
    }

    /**
     * @param tableN    表名
     * @param familyN   列族
     * @param qualifier 列
     * @throws IOException
     */
    private void scan(byte[] tableN, byte[] familyN, String qualifier) throws IOException {
        TableName tableName = TableName.valueOf(tableN);
        Table table = connection.getTable(tableName);
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        scanner.forEach(data -> System.out.println((Bytes.toString(data.getValue(familyN, qualifier.getBytes())))));
    }
}
