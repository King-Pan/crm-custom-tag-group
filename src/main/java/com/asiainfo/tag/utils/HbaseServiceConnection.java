package com.asiainfo.tag.utils;

import com.asiainfo.tag.model.HbaseInfo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.error("======================>初始化【服务集群】  开始<======================");
        config = HBaseConfiguration.create();

        HbaseInfo hbaseInfo = SpringUtil.getBean(HbaseInfo.class);
        // zookeeper主机名称，多个主机名称以,号分隔开
        String hosts = hbaseInfo.getServiceQuorum();

        // 客户端端口号
        String port = hbaseInfo.getServiceClientPort();
        //klist -kte ocdc01.keytab
        //krb.conf   放在/etc/目录下
        System.setProperty("java.security.krb5.conf", hbaseInfo.getKrb5());

        config.set("hadoop.security.authentication", "kerberos");
        config.set("hbase.security.authentication", "kerberos");
        config.set("kerberos.principal", hbaseInfo.getPrincipal());
        //config.set("hbase.master.kerberos.principal", hbaseInfo.getPrincipal());
        //config.set("hbase.regionserver.kerberos.principal", hbaseInfo.getPrincipal());
        config.set("keytab.file", hbaseInfo.getKeytabFile());
        config.set("hbase.zookeeper.quorum", hosts);
        config.set("hbase.zookeeper.property.clientPort", port);
        config.addResource("hbase-resource/core-site.xml");
        config.addResource("hbase-resource/hbase-site.xml");
        config.addResource("hbase-resource/hdfs-site.xml");
        log.error("======================>初始化【服务集群】  参数: hbase.zookeeper.quorum->{}", hosts);
        log.error("======================>初始化【服务集群】  参数: hbase.zookeeper.property.clientPort->{}", port);
        log.error("======================>初始化【服务集群】  参数: java.security.krb5.conf->{}", hbaseInfo.getKrb5());
        log.error("======================>初始化【服务集群】  参数: kerberos.principal->{}", hbaseInfo.getPrincipal());
        log.error("======================>初始化【服务集群】  参数: keytab.file->{}", hbaseInfo.getKeytabFile());
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
            log.error("======================>初始化【服务集群】  异常->" + e.getMessage(), e);
        }
        log.error("======================>初始化【服务集群】  结束<======================");
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
