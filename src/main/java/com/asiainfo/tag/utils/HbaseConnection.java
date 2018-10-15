package com.asiainfo.tag.utils;

import com.asiainfo.tag.model.HbaseInfo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.error("======================>初始化【生产集群】  开始<======================");
        config = HBaseConfiguration.create();
        //zookeeper代理地址


        HbaseInfo hbaseInfo = SpringUtil.getBean(HbaseInfo.class);

        // zookeeper主机名称，多个主机名称以,号分隔开
        config.set("hbase.zookeeper.quorum", hbaseInfo.getQuorum());
        // 客户端端口号
        config.set("hbase.zookeeper.property.clientPort", hbaseInfo.getClientPort());
        //config.set("hbase.client.ipc.pool.type", "RoundRobin");
        config.set("hbase.client.ipc.pool.size", hbaseInfo.getPoolSize());
        log.error("======================>初始化【生产集群】  参数: hbase.zookeeper.quorum->{}", hbaseInfo.getQuorum());
        log.error("======================>初始化【生产集群】  参数: hbase.zookeeper.property.clientPort->{}", hbaseInfo.getClientPort());
        try {
            connection = ConnectionFactory.createConnection(config);
            hBaseAdmin = new HBaseAdmin(connection);
        } catch (IOException e) {
            log.error("======================>初始化【生产集群】  异常->" + e.getMessage(), e);
        }
        log.error("======================>初始化【生产集群】  结束<======================");
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
