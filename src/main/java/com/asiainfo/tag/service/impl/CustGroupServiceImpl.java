package com.asiainfo.tag.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.tag.service.CustGroupService;
import com.asiainfo.tag.utils.DateUtils;
import com.asiainfo.tag.utils.HBaseUtils;
import com.asiainfo.tag.utils.MD5RowKeyGenerator;
import com.asiainfo.tag.utils.SettingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/21
 * Time: 下午2:52
 * Description: No Description
 */
@Slf4j
@Service
public class CustGroupServiceImpl implements CustGroupService {

    private static final String TABLE_NAME_PREFIX = "COC_CUSTOMER_GROUP_";

    MD5RowKeyGenerator md5RowKeyGenerator = new MD5RowKeyGenerator();

    @Override
    public String checkTagInGroup(String jsonParma) {
        log.info("--------------查询号码是否在某个客户群组中 checkTagInGroup begin--------------");
        log.info("checkTagInGroup params:" + jsonParma);
        JSONObject js = JSONObject.parseObject(jsonParma);
        String telno = js.getString("telnum");
        String groupid = js.getString("custgroupid");
        String tableName = getTableName();
        Map<String, String> rt = new HashMap<>(20);
        try {
            if (HBaseUtils.tableExists(tableName)) {
                String rowKey = md5RowKeyGenerator.generatePrefix(telno) + telno + "|" + groupid;
                String groupId = String.format("%08d", Long.valueOf(groupid.substring(6, groupid.length())) + 1);
                String enKey = md5RowKeyGenerator.generatePrefix(telno) + telno + "|" + groupid.substring(0, 6) + groupId;
                log.info("查询客户群是否存在startRow:" + rowKey);
                log.info("查询客户群是否存在endRow:" + enKey);
                if (HBaseUtils.scanQueryList(rowKey, enKey, tableName).size() > 0) {
                    rt.put("retcode", "0");
                    rt.put("errmsg", "数据存在");
                    rt.put("telstatus", "0");
                } else {
                    rt.put("retcode", "0");
                    rt.put("errmsg", "数据不存在");
                    rt.put("telstatus", "-1");
                }
            } else {
                rt.put("retcode", "-2");
                rt.put("errmsg", "表" + tableName + "未生成");
            }
        } catch (Exception e) {
            log.error("Hbase查询出错：" + e.getMessage(), e);
            rt.put("retcode", "-2");
            log.error("查询集群:{}", SettingCache.TYPE);
            rt.put("errmsg", "Hbase查询出错：" + e.getMessage());
        }

        String resultInfo = JSONObject.toJSONString(rt);
        log.info("电话号码: " + telno);
        log.info("客户群组ID: " + groupid);
        log.info("查询表名: " + tableName);
        log.info("查询的集群: " + SettingCache.TYPE);
        log.info("返回结果是：" + resultInfo);
        log.info("--------------查询号码是否在某个客户群组中 checkTagInGroup end--------------");
        return resultInfo;
    }

    @Override
    public String getUserTagGroupList(String jsonParma) {
        log.info("---------查询客户群组信息 getUserTagGroupList begin----------");
        log.info("getUserTagGroupList 查询参数:" + jsonParma);
        JSONObject js = JSONObject.parseObject(jsonParma);
        String telno = js.getString("telnum");
        String tableName = getTableName();
        Map<String, Object> rt = new HashMap<>(20);
        try {
            if (HBaseUtils.tableExists(tableName)) {
                //13477343118 - 开始行： oc513477343118
                String rowKey = md5RowKeyGenerator.generatePrefix(telno) + telno;
                String endKey = md5RowKeyGenerator.generatePrefix(telno) + String.valueOf(Long.valueOf(telno) + 1);
                log.info("查询客户信息: rowKey=" + rowKey + ",endKey=" + rowKey);
                List<String> queryList = HBaseUtils.scanQueryList(rowKey, endKey, tableName);
                List<Map<String, String>> tmp = new ArrayList<>(20);
                for (String record : queryList) {
                    Map<String, String> map = new HashMap<>(20);
                    String groupId = record.replaceAll(rowKey + "\\|", "");
                    groupId = groupId.substring(0, groupId.length() - 16);
                    map.put("id", groupId);
                    tmp.add(map);
                }
                rt.put("retcode", "0");
                rt.put("errmsg", "");
                rt.put("custgrouplist", tmp);
            } else {
                rt.put("retcode", "-2");
                rt.put("errmsg", "表" + tableName + "未生成");
            }
        } catch (Exception e) {
            log.error("Hbase查询出错：" + e.getMessage(), e);
            rt.put("retcode", "-3");
            log.error("查询集群:{}", SettingCache.TYPE);
            rt.put("errmsg", "Hbase查询出错：" + e.getMessage());
        }

        String resultInfo = JSONObject.toJSONString(rt);
        log.info("电话号码: " + telno);
        log.info("查询表名: " + tableName);
        log.info("查询的集群: " + SettingCache.TYPE);
        log.info("返回结果是：" + resultInfo);
        log.info("---------查询客户群组信息 getUserTagGroupList end----------");
        return resultInfo;
    }


    private String getTableName() {
        String tableName;
        if (SettingCache.IS_AUTOSWITCH) {
            tableName = TABLE_NAME_PREFIX + DateUtils.getBeforeNDay(-1);
        } else {
            if (StringUtils.isNotBlank(SettingCache.SWITCH_DATE)) {
                tableName = TABLE_NAME_PREFIX + SettingCache.SWITCH_DATE;
            } else {
                throw new RuntimeException("设置的手动切换日期为null");
            }
        }
        return tableName;
    }
}
