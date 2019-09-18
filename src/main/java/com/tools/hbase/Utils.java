package com.tools.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.RegionLocator;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;

/**
 * HBase Utils Class
 */
public class Utils {

    /**
     * HBase Bulk Load method replace command line
     *
     * @param configuration hadoop config
     * @param hFilePath     HFile path
     * @param table         table name
     */
    public static void doBulkLoad(Configuration configuration, String hFilePath, String table) {
        try {
            FileSystem fileSystem = FileSystem.newInstance(configuration);
            // add HBase config to Configuration object
            HBaseConfiguration.addHbaseResources(configuration);
            LoadIncrementalHFiles loadIncrementalHFiles = new LoadIncrementalHFiles(configuration);

            // create HBase connection
            try (Connection connection = ConnectionFactory.createConnection(configuration)) {
                Table connectionTable = connection.getTable(TableName.valueOf(table));
                RegionLocator regionLocator = connection.getRegionLocator(connectionTable.getName());
                // new client api for HBase 1.0.0+
                loadIncrementalHFiles.doBulkLoad(new Path(hFilePath), connection.getAdmin(), connectionTable, regionLocator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
