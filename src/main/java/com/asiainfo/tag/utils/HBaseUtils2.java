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
public class HBaseUtils2 {

    private synchronized static Connection getConnection() {
        Connection connection = null;
        log.info("======================>获取Hbase连接  开始<======================");
        if (SettingCache.IS_AUTOSWITCH) {
            log.info("======================>自动切换集群实现双活<======================");
            try {
                connection = HbaseConnection.getInstance().getConnection();
                log.info("======================>生产集群<======================");
            } catch (Exception e) {
                log.error("生成集群连接失败", e);
                try {
                    connection = HbaseServiceConnection.getInstance().getConnection();
                    log.info("======================>服务集群<======================");
                } catch (Exception e1) {
                    log.error("服务集群连接失败", e1);
                    log.error("======================>获取集群失败:" + e1.getMessage() + "<======================");
                }
            }
            log.info("======================>自动切换集群实现双活<======================");
        } else {
            log.info("======================>手动切换集群实现双活<======================");
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
            log.info("======================>手动切换集群实现双活<======================");
        }
        log.info("======================>获取Hbase连接  结束<======================");
        return connection;
    }

    private synchronized static HBaseAdmin getHBaseAdmin() {
        HBaseAdmin hBaseAdmin = null;
        log.info("======================>获取HBaseAdmin  开始<======================");
        if (SettingCache.IS_AUTOSWITCH) {
            log.info("======================>自动切换集群实现双活<======================");
            try {
                hBaseAdmin = HbaseConnection.getInstance().gethBaseAdmin();
            } catch (Exception e) {
                log.error("生成集群hBaseAdmin获取失败", e);
                try {
                    hBaseAdmin = HbaseServiceConnection.getInstance().gethBaseAdmin();
                } catch (Exception e1) {
                    log.error("服务集群hBaseAdmin获取失败", e1);
                    log.error("======================>获取hBaseAdmin失败");
                }
            }
            log.info("======================>自动切换集群实现双活<======================");
        } else {
            log.info("======================>手动切换集群实现双活<======================");
            if (SettingCache.TYPE.equals(SettingCache.DEFAULT_TYPE)) {
                hBaseAdmin = HbaseConnection.getInstance().gethBaseAdmin();
                log.info("======================>生产集群 hBaseAdmin<======================");
            } else if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
                hBaseAdmin = HbaseConnection.getInstance().gethBaseAdmin();
                log.info("======================>服务集群 hBaseAdmin<======================");
            } else {
                log.error("设置集群参数有误：{}-不存在", SettingCache.TYPE);
            }
            log.info("======================>手动切换集群实现双活<======================");
        }
        log.info("======================>获取HBaseAdmin  结束<======================");
        return hBaseAdmin;
    }

    /**
     * 根据主键rowKey查询一行数据
     *
     * @param tableName 表名
     * @param rowKey    主键ID
     * @return Result
     */
    public static Result getSimpleRow(String tableName, String rowKey) throws Exception {
        log.info("准备获取Table： " + tableName);
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        log.info("获取Table： " + tableName);
        log.info("准备获取rowKey: " + rowKey);
        Get get = new Get(rowKey.getBytes());
        log.info("获取rowKey: " + rowKey);

        log.info("准备获取Result: ");
        Result result = table.get(get);
        log.info("获取Result: " + result);
        return result;
    }


    public static Boolean tableExists(String tableName) {
        boolean isExists = false;
        try {
            log.info("=========================tableExists begin=========================");
            log.info("=========================tableExists tableName-->" + tableName);
            HBaseAdmin admin = getHBaseAdmin();
            log.info("=========================tableExists admin-->" + admin);
            isExists = admin.tableExists(tableName);
            log.info("=========================tableExists isExists-->" + isExists);
            log.info("=========================tableExists end=========================");
        } catch (IOException e) {
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
        scan.setStartRow(Bytes.toBytes(tableName));
        scan.setStopRow(Bytes.toBytes(endRowKey));
        ResultScanner rs = query.getScanner(scan);
        return rs;
    }


    public static List<String> scanQueryList(String startRowKey,
                                             String endRowKey,
                                             String tableName) {
        List<String> result = new ArrayList<>(10);

        try {
            log.debug("================scan query=================");
            log.debug("startRowKey=[" + startRowKey + "]");
            log.debug("endRowKey=[" + endRowKey + "]");
            Table query = getConnection().getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(tableName));
            scan.setStopRow(Bytes.toBytes(endRowKey));
            ResultScanner rs = query.getScanner(scan);
            Iterator<Result> it = rs.iterator();
            while (it.hasNext()) {
                result.add(Bytes.toString(it.next().getRow()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("[scanQueryList]方法出错: " + e.getMessage(), e);
            throw new RuntimeException("查询数据列表【scanQueryList】出错");
        }
        return result;
    }

    /**
     * 获取最新的表名
     *
     * @param prefix
     * @return String
     */
    public static String getTableName(String prefix) throws Exception {
        String tableName;
        int n = -1;
        while (true) {
            tableName = prefix + DateUtils.getBeforeNDay(n);
            if (tableExists(tableName)) {
                log.info("查询的表名为-->" + tableName);
                break;
            }
            if (n <= -5) {
                log.error("未找到数据表");
                throw new RuntimeException("未找到数据表");
            }
            n--;
        }
        return tableName;
    }


    public static Connection getConnection(Connection connection, String model) {
        if (connection == null) {
            throw new RuntimeException(model + "连接为null");
        } else if (connection.isClosed()) {
            throw new RuntimeException(model + "连接已关闭");
        } else {
            try {
                connection.getAdmin();
            } catch (IOException e) {
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
        log.info("startRow-->" + rowKey);
        String stopRowKey = rowKey + "g";
        scan.setStopRow(stopRowKey.getBytes());
        log.info("stopRow-->" + stopRowKey);
        return table.getScanner(scan);
    }
}
