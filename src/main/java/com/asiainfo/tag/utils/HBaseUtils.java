package com.asiainfo.tag.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/8/1
 * Time: 上午9:53
 * Description: Hbase工具类
 */
@Slf4j
public class HBaseUtils {

    private synchronized static Connection getConnection() {
        Connection connection = null;
        log.info("======================>获取Hbase连接  开始<======================");
        if (SettingCache.TYPE.equals(SettingCache.DEFAULT_TYPE)) {
            log.info("======================>生产集群<======================");
            connection = HbaseConnection.getInstance().getConnection();
            log.info("======================>生产集群<======================");
        } else if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
            log.info("======================>服务集群<======================");
            connection = HbaseServiceConnection.getInstance().getConnection();
            log.info("======================>服务集群<======================");
        } else {
            log.error("设置集群参数有误：{}-不存在", SettingCache.TYPE);
        }
        log.info("======================>获取Hbase连接  结束<======================");
        return connection;
    }

    private synchronized static HBaseAdmin getHBaseAdmin() {
        HBaseAdmin hBaseAdmin = null;
        log.info("======================>获取HBaseAdmin  开始<======================");
        if (SettingCache.TYPE.equals(SettingCache.DEFAULT_TYPE)) {
            hBaseAdmin = HbaseConnection.getInstance().gethBaseAdmin();
            log.info("======================>生产集群 hBaseAdmin<======================");
        } else if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
            hBaseAdmin = HbaseServiceConnection.getInstance().gethBaseAdmin();
            log.info("======================>服务集群 hBaseAdmin<======================");
        } else {
            log.error("设置集群参数有误：{}-不存在", SettingCache.TYPE);
        }
        log.info("======================>获取HBaseAdmin  结束<======================");
        return hBaseAdmin;
    }


    public static Boolean tableExists(String tableName) {
        boolean isExists;
        try {
            log.debug("=========================tableExists begin=========================");
            log.info("=========================tableExists tableName-->" + tableName);
            HBaseAdmin admin = getHBaseAdmin();
            isExists = admin.tableExists(tableName);
            log.info("=========================tableExists isExists-->" + isExists);
            log.debug("=========================tableExists end=========================");
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("判断表是否存在出错： \n" + e.getMessage(), e);
            throw new RuntimeException("查询表是否存在出错: [" + tableName + "]");
        }
        return isExists;
    }


    public static ResultScanner scanQuery(String startRowKey,
                                          String endRowKey,
                                          String tableName) throws Exception {
        log.debug("================scan query=================");
        log.debug("startRowKey=[" + startRowKey + "]");
        log.debug("endRowKey=[" + endRowKey + "]");
        Table query = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(startRowKey));
        scan.setStopRow(Bytes.toBytes(endRowKey));
        ResultScanner rs = query.getScanner(scan);
        return rs;
    }


    public static List<String> scanQueryList(String startRowKey,
                                             String endRowKey,
                                             String tableName) {
        List<String> result = new ArrayList<>(10);

        try {
            log.debug("================scan List=================");
            log.debug("startRowKey=[" + startRowKey + "]");
            log.debug("endRowKey=[" + endRowKey + "]");
            log.debug("================scanQueryList 获取客户群信息getTable  begin ================");
            Table query = getConnection().getTable(TableName.valueOf(tableName));
            log.debug("================scanQueryList 获取客户群信息getTable  end ================");
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRowKey));
            scan.setStopRow(Bytes.toBytes(endRowKey));
            ResultScanner rs = query.getScanner(scan);
            log.debug("================scanQueryList 封装数据 begin ================");
            for (Result res : rs) {
                log.debug("================scanQueryList 数据->[" + Bytes.toString(res.getRow()) + "]");
                result.add(Bytes.toString(res.getRow()));
            }
            log.debug("================scanQueryList 封装数据 end ================");
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("[scanQueryList]方法出错: " + e.getMessage(), e);
            throw new RuntimeException("查询数据列表【scanQueryList】出错");
        }
        return result;
    }


    public static Connection getConnection(Connection connection, String model) {
        if (connection == null) {
            throw new RuntimeException(model + "连接为null");
        } else if (connection.isClosed()) {
            throw new RuntimeException(model + "连接已关闭");
        } else {
            try {
                connection.getAdmin();
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(model + "获取连接失败");
            }
        }
        return connection;
    }


    public static ResultScanner getSimpleRowByScan(String tableName, String rowKey) throws Exception {
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        //Scan所有数据
        Scan scan = new Scan();
        scan.setStartRow(rowKey.getBytes());
        log.debug("startRow-->" + rowKey);
        String stopRowKey = rowKey + "g";
        scan.setStopRow(stopRowKey.getBytes());
        log.debug("stopRow-->" + stopRowKey);
        return table.getScanner(scan);
    }
}
