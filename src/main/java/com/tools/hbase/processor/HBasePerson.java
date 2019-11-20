package com.tools.hbase.processor;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

public class HBasePerson {

    public static void main(String[] args) {

        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "spark01:2181,spark02:2181,spark03:2181");
        configuration.set("hbase.table.sanity.checks", "false");

        try (Connection connection = ConnectionFactory.createConnection(configuration)) {
            TableName tableName = TableName.valueOf("person");
            if (!connection.getAdmin().tableExists(tableName)) {
                HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
                HColumnDescriptor info = new HColumnDescriptor("info");
                hTableDescriptor.addFamily(info);
                Path path = new Path("hdfs://spark01:9000/bigdata-practice-0.jar");
                hTableDescriptor.addCoprocessor(HBaseProcessor.class.getCanonicalName(), path, HBaseProcessor.PRIORITY_USER, null);
                connection.getAdmin().createTable(hTableDescriptor);
            }
            Put put = new Put("0002".getBytes());
            put.addColumn("info".getBytes(), "name".getBytes(), "lishengnan".getBytes());
            put.addColumn("info".getBytes(), "age".getBytes(), "18".getBytes());
            try (Table person = connection.getTable(tableName)) {
                person.put(put);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
