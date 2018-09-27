package com.asiainfo.tag.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/27
 * Time: 下午2:22
 * Description: Hbase 相关配置参数
 */
@Data
@Component
public class HbaseInfo {

    /**
     * 生产集群HOST
     */
    @Value("${hbase.zookeeper.quorum}")
    private String quorum;

    @Value("${service.hbase.zookeeper.quorum}")
    private String serviceQuorum;
    /**
     * 生产集群PORT
     */
    @Value("${hbase.zookeeper.property.clientPort}")
    private String clientPort;

    @Value("${service.hbase.zookeeper.property.clientPort}")
    private String serviceClientPort;
    /**
     * HBASE 每次重连叠加时间: 第一次重连等待50ms 第二次重连等待100ms 第三次重连等待150ms
     */
    @Value("${hbase.client.pause}")
    private String pause;
    /**
     * HBASE 重连次数 默认为31次
     */
    @Value("${hbase.client.retries.number}")
    private String number;
    /**
     * HBASE 一次rpc调用的超时时间
     */
    @Value("${hbase.rpc.timeout}")
    private String rpcTimeout;
    /**
     * hbase keytab_config相关配置文件路径
     */
    @Value("${java.security.krb5.conf}")
    private String krb5;
    /**
     * hbase keytab相关配置文件路径
     */
    @Value("${keytab.file}")
    private String keytabFile;

    @Value("${kerberos.principal}")
    private String principal;

}
