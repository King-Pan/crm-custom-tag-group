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
        if (SettingCache.TYPE.equals(SettingCache.PROD_TYPE)) {
            connection = HbaseConnection.getInstance().getConnection();
        } else if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
            connection = HbaseServiceConnection.getInstance().getConnection();
        } else {
            log.error("设置集群参数有误：{}-不存在", SettingCache.TYPE);
        }
        return connection;
    }

    public static Boolean tableExists(String tableName) {
        boolean isExists;
        try {
            Admin admin = getConnection().getAdmin();
            isExists = admin.tableExists(TableName.valueOf(tableName));
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
            Table query = getConnection().getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRowKey));
            scan.setStopRow(Bytes.toBytes(endRowKey));
            rs = query.getScanner(scan);
            for (Result res : rs) {
                result.add(Bytes.toString(res.getRow()));
            }
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("[scanQueryList]方法出错: " + e.getMessage(), e);
            throw new RuntimeException("查询数据列表【scanQueryList】出错", e);
        } finally {
            if (rs != null) {
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
