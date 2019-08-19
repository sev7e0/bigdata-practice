package com.tools.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RandomRowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

@Slf4j
public class HBaseFilter {

    private static Connection connection;
    static {
        Configuration configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum", "localhost");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        try {
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Table table = connection.getTable(TableName.valueOf(HBaseTestUtil.getTableName()));

        // rowId前缀过滤
        log.warn("PrefixFilter");
        Filter pf = new PrefixFilter(Bytes.toBytes("1"));
        Scan scan00 = new Scan().setFilter(pf);
        table.getScanner(scan00).forEach(res-> log.info(Bytes.toString(res.getValue(HBaseTestUtil.getFamilyName(), "data_stamp".getBytes()))));

        //随机百分比过滤
        log.warn("RandomRowFilter");
        Filter randomRowFilter = new RandomRowFilter(0.003f);
        Scan scan01 = new Scan().setFilter(randomRowFilter);
        table.getScanner(scan01).forEach(res-> res.listCells().forEach(cell -> log.info("{} : {} : {} : {} : {}",
                Bytes.toString(CellUtil.cloneRow(cell)),
                Bytes.toString(CellUtil.cloneFamily(cell)),
                Bytes.toString(CellUtil.cloneQualifier(cell)),
                Bytes.toString(CellUtil.cloneValue(cell)),
                cell.getTimestamp())));
    }
}
