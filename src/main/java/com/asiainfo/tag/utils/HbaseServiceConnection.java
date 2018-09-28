package com.asiainfo.tag.utils;

import com.asiainfo.tag.model.HbaseInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/8/16
 * Time: 上午10:03
 * Description: No Description
 */
public class HbaseServiceConnection {

    private Configuration config;
    private Connection connection;

    private HBaseAdmin hBaseAdmin;


    public static HbaseServiceConnection getInstance() {
        return HbaseServiceConnection.HbaseServiceConnectionHeper.INSTANCE;
    }

    private static class HbaseServiceConnectionHeper {
        private static final HbaseServiceConnection INSTANCE = new HbaseServiceConnection();
    }


    private HbaseServiceConnection() {
        config = HBaseConfiguration.create();

        HbaseInfo hbaseInfo = SpringUtil.getBean(HbaseInfo.class);
        // zookeeper主机名称，多个主机名称以,号分隔开
        String hosts = "HBBDC-FW-DN-17,HBBDC-FW-DN-27,HBBDC-FW-DN-37";

        // 客户端端口号
        String port = "2181";
        //klist -kte ocdc01.keytab
        //krb.conf   放在/etc/目录下
        System.setProperty("java.security.krb5.conf", hbaseInfo.getKrb5());

        config.set("hadoop.security.authentication", "kerberos");
        config.set("hbase.security.authentication", "kerberos");
        config.set("kerberos.principal", hbaseInfo.getPrincipal());
        config.set("hbase.master.kerberos.principal", hbaseInfo.getPrincipal());
        config.set("hbase.regionserver.kerberos.principal", hbaseInfo.getPrincipal());
        config.set("keytab.file", hbaseInfo.getKeytabFile());
        config.set("hbase.zookeeper.quorum", hosts);
        config.set("hbase.zookeeper.property.clientPort", port);
        config.set("zookeeper.recovery.retry", "3");
        //HBASE 每次重连叠加时间: 第一次重连等待50ms 第二次重连等待100ms 第三次重连等待150ms
        config.set("hbase.client.pause", hbaseInfo.getPause());
        //HBASE 重连次数 默认为31次
        config.set("hbase.client.retries.number", hbaseInfo.getNumber());
        //HBASE 一次rpc调用的超时时间
        config.set("hbase.rpc.timeout", hbaseInfo.getRpcTimeout());
        config.addResource("hbase-resource/core-site.xml");
        config.addResource("hbase-resource/hbase-site.xml");
        config.addResource("hbase-resource/hdfs-site.xml");

        try {
            UserGroupInformation.setConfiguration(config);
            UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(hbaseInfo.getPrincipal(),
                    hbaseInfo.getKeytabFile());
            UserGroupInformation.setLoginUser(ugi);
            connection = ConnectionFactory.createConnection(config);
            hBaseAdmin = new HBaseAdmin(config);
            System.out.println("hBaseAdmin-->" + hBaseAdmin);
            System.out.println("====================================");
        } catch (IOException e) {
            System.out.println("====================================初始化服务集群失败:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return config;
    }


    public Connection getConnection() {
        return HBaseUtils.getConnection(connection, "服务集群");
    }


    public HBaseAdmin gethBaseAdmin() {
        return hBaseAdmin;
    }
}
