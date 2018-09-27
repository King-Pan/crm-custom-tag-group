package com.asiainfo.tag.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/8/15
 * Time: 上午10:06
 * Description: No Description
 */
public class HbaseProxyConnection {
    private Configuration config;
    private Connection connection;

    private HBaseAdmin hBaseAdmin;


    public static HbaseProxyConnection getInstance() {
        return HbaseProxyConnection.HbaseProxyConnectionHeper.INSTANCE;
    }

    private static class HbaseProxyConnectionHeper {
        private static final HbaseProxyConnection INSTANCE = new HbaseProxyConnection();
    }


    private HbaseProxyConnection() {
        config = HBaseConfiguration.create();
        //zookeeper代理地址

        // zookeeper主机名称，多个主机名称以,号分隔开
        //
        String hosts = "10.29.30.161:2181,10.29.30.162:2181";
        // 客户端端口号
        String port = "2181";
        config.set("hbase.zookeeper.quorum", hosts);
        config.set("hbase.zookeeper.property.clientPort", port);
        try {
            connection = ConnectionFactory.createConnection(config);
            hBaseAdmin = new HBaseAdmin(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return config;
    }


    public Connection getConnection() {
        return HBaseUtils.getConnection(connection,"BDX集群");
    }


    public HBaseAdmin gethBaseAdmin() {
        return hBaseAdmin;
    }
}
