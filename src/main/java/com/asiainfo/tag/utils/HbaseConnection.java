package com.asiainfo.tag.utils;

import com.asiainfo.tag.model.HbaseInfo;
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
 * Time: 上午10:07
 * Description: No Description
 */
public class HbaseConnection {
    private Configuration config;
    private Connection connection;

    private HBaseAdmin hBaseAdmin;


    public static HbaseConnection getInstance() {
        return HbaseConnectionHeper.INSTANCE;
    }

    private static class HbaseConnectionHeper {
        private static final HbaseConnection INSTANCE = new HbaseConnection();
    }


    private HbaseConnection() {
        config = HBaseConfiguration.create();
        //zookeeper代理地址


        HbaseInfo hbaseInfo = SpringUtil.getBean(HbaseInfo.class);

        // zookeeper主机名称，多个主机名称以,号分隔开
        config.set("hbase.zookeeper.quorum", hbaseInfo.getQuorum());
        // 客户端端口号
        config.set("hbase.zookeeper.property.clientPort", hbaseInfo.getClientPort());
        config.set("fs.defaultFS", "hdfs://hbcm");
        //HBASE 每次重连叠加时间: 第一次重连等待50ms 第二次重连等待100ms 第三次重连等待150ms
        config.set("hbase.client.pause", hbaseInfo.getPause());
        //HBASE 重连次数 默认为31次
        config.set("hbase.client.retries.number", hbaseInfo.getNumber());
        //HBASE 一次rpc调用的超时时间
        config.set("hbase.rpc.timeout", hbaseInfo.getRpcTimeout());
//        config.set("zookeeper.recovery.retry","5");
//        config.set("zookeeper.recovery.retry.intervalmill","100");
        config.set("hbase.client.scanner.timeout.period", "10000");


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
        return HBaseUtils.getConnection(connection, "生产集群");
    }


    public HBaseAdmin gethBaseAdmin() {
        return hBaseAdmin;
    }

}
