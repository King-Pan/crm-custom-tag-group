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
        if (SettingCache.TYPE.equals(SettingCache.DEFAULT_TYPE)) {
            connection = HbaseConnection.getInstance().getConnection();
            log.info("======================>获取【生产集群】 end<======================");
        } else if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
            connection = HbaseServiceConnection.getInstance().getConnection();
            log.info("======================>获取【服务集群】 end<======================");
        } else {
            log.error("设置集群参数有误：{}-不存在", SettingCache.TYPE);
        }
        return connection;
    }

    private synchronized static HBaseAdmin getHBaseAdmin() {
        HBaseAdmin hBaseAdmin = null;
        if (SettingCache.TYPE.equals(SettingCache.DEFAULT_TYPE)) {
            hBaseAdmin = HbaseConnection.getInstance().gethBaseAdmin();
            log.info("======================>获取【生产集群】 hBaseAdmin end<======================");
        } else if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
            hBaseAdmin = HbaseServiceConnection.getInstance().gethBaseAdmin();
            log.info("======================>获取【服务集群】 hBaseAdmin end<======================");
        } else {
            log.error("设置集群参数有误：{}-不存在", SettingCache.TYPE);
        }
        return hBaseAdmin;
    }


    public static Boolean tableExists(String tableName) {
        boolean isExists;
        try {
            HBaseAdmin admin = getHBaseAdmin();
            isExists = admin.tableExists(tableName);
            log.info("tableExists isExists-->" + isExists);
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("判断表是否存在出错： \n" + e.getMessage(), e);
            throw new RuntimeException("查询表是否存在出错: [" + tableName + "]");
        }
        return isExists;
    }



    public static List<String> scanQueryList(String startRowKey,
                                             String endRowKey,
                                             String tableName) {
        List<String> result = new ArrayList<>(10);
        ResultScanner rs = null;
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
            rs = query.getScanner(scan);
            log.debug("================scanQueryList 封装数据 begin ================");
            for (Result res : rs) {
                if(log.isDebugEnabled()){
                    log.debug("================scanQueryList 数据->[" + Bytes.toString(res.getRow()) + "]");
                }
                result.add(Bytes.toString(res.getRow()));
            }
            log.debug("================scanQueryList 封装数据 end ================");
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("[scanQueryList]方法出错: " + e.getMessage(), e);
            throw new RuntimeException("查询数据列表【scanQueryList】出错", e);
        }
        finally {
            if(rs!=null){
                rs.close();
            }
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
}
