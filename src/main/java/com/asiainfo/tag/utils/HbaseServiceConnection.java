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
        System.setProperty("java.security.krb5.conf", "/root/crm-person-api/krb5_fw.conf");
        String principal = "ocdc-hbbdcfw@HBBDCFW.COM";
        String keytabPath = "/root/crm-person-api/ocdc01.keytab";

        config.set("hadoop.security.authentication", "kerberos");
        config.set("hbase.security.authentication", "kerberos");
        config.set("kerberos.principal", principal);
        config.set("hbase.master.kerberos.principal", "hbase/_HOST@HBBDCFW.COM");
        config.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@HBBDCFW.COM");
        config.set("keytab.file", keytabPath);
        config.set("hbase.zookeeper.quorum", hosts);
        config.set("hbase.zookeeper.property.clientPort", port);
        config.set("zookeeper.recovery.retry","3");
        config.addResource("hbase-resource/core-site.xml");
        config.addResource("hbase-resource/hbase-site.xml");
        config.addResource("hbase-resource/hdfs-site.xml");

        try {
            UserGroupInformation.setConfiguration(config);
            UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal,
                    keytabPath);
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
