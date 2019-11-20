package com.tools.hbase.processor;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;

import java.io.IOException;
import java.util.List;

/**
 * HBase协处理器实践
 * HBase提供四种协处理器：
 *
 *  RegionObserver 针对get put delete等操作的
 *  RegionServerObserver 针对RegionServer的
 *  WALObserver 针对日志的如滚动、删除
 *  MasterObserver 针对表结构的创建、修改、删除
 *
 *  EndpointObserver 作用是将用户层的逻辑下推到数据层执行，将大量处理结果放在HBase中执行，需要手动调用
 *
 */
public class HBaseProcessor extends BaseRegionObserver {

    @Override
    public void prePut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "spark01:2181,spark02:2181,spark03:2181");
        try (Connection connection = ConnectionFactory.createConnection(configuration)) {
            TableName person_back = TableName.valueOf("person_back");
            //表不存在时创建表
            if (!connection.getAdmin().tableExists(person_back)){
                HColumnDescriptor columnDescriptor = new HColumnDescriptor("info");
                HTableDescriptor hTableDescriptor = new HTableDescriptor(person_back);
                hTableDescriptor.addFamily(columnDescriptor);
                connection.getAdmin().createTable(hTableDescriptor);
            }
            List<Cell> cells = put.get("info".getBytes(), "name".getBytes());
            if (cells.isEmpty()) {
                return;
            }
            Cell cell = cells.get(0);
            Put put1 = new Put(put.getRow());
            put1.add(cell);
            try (Table person = connection.getTable(person_back)) {
                person.put(put1);
            }
        }
    }
}
