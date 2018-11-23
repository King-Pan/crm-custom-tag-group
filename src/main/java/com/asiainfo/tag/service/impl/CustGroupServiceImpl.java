package com.asiainfo.tag.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.tag.service.CustGroupService;
import com.asiainfo.tag.utils.DateUtils;
import com.asiainfo.tag.utils.HBaseUtils;
import com.asiainfo.tag.utils.MD5RowKeyGenerator;
import com.asiainfo.tag.utils.SettingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
        JSONObject js = JSONObject.parseObject(jsonParma);
        String telno = js.getString("telnum");
        String groupid = js.getString("custgroupid");
        String tableName = getTableName();
        Map<String, String> rt = new HashMap<>(20);
        log.info("===============checkTagInGroup  本次查询的表为: " + tableName);
        try {
            if (HBaseUtils.tableExists(tableName)) {
                String rowKey = md5RowKeyGenerator.generatePrefix(telno) + telno + "|" + groupid;
                String groupId = String.format("%08d", Long.valueOf(groupid.substring(6, groupid.length())) + 1);
                StringBuilder sb = new StringBuilder();
                sb.append(md5RowKeyGenerator.generatePrefix(telno)).append(telno).append("|");
                sb.append(groupid.substring(0, 6)).append(groupId);
                String enKey = sb.toString();
                if(log.isDebugEnabled()){
                    log.debug("查询客户群是否存在startRow:{},endRow:{}",rowKey,enKey);
                }
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
                rt.put("retcode", "-3");
                rt.put("errmsg", "表" + tableName + "未生成");
            }
        } catch (Exception e) {
            log.error("Hbase查询出错：" + e.getMessage(), e);
            rt.put("retcode", "-3");
            log.error("查询集群:{}", SettingCache.TYPE);
            rt.put("errmsg", "Hbase查询出错：" + e.getMessage());
        }

        String resultInfo = JSONObject.toJSONString(rt);
        return resultInfo;
    }

    @Override
    public String getUserTagGroupList(String jsonParma) {
        JSONObject js = JSONObject.parseObject(jsonParma);
        String telno = js.getString("telnum");
        String tableName = getTableName();
        Map<String, Object> rt = new HashMap<>(20);
        log.warn("===============getUserTagGroupList 本次查询的表为: " + tableName);
        try {
            if (HBaseUtils.tableExists(tableName)) {
                //13477343118 - 开始行： oc513477343118
                StringBuilder rowKeySb = new StringBuilder(md5RowKeyGenerator.generatePrefix(telno).toString());
                rowKeySb.append(telno);
                String rowKey = rowKeySb.toString();
                StringBuilder endKeySb = new StringBuilder(md5RowKeyGenerator.generatePrefix(telno).toString());
                endKeySb.append( String.valueOf(Long.valueOf(telno) + 1));
                String endKey = endKeySb.toString();
                if(log.isDebugEnabled()){
                    log.debug("查询客户信息: rowKey={},endKey={}",rowKey ,rowKey);
                }
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
                rt.put("retcode", "-3");
                rt.put("errmsg", "表" + tableName + "未生成");
            }
        } catch (Exception e) {
            log.error("Hbase查询出错：" + e.getMessage(), e);
            rt.put("retcode", "-3");
            log.error("查询集群:{}", SettingCache.TYPE);
            rt.put("errmsg", "Hbase查询出错：" + e.getMessage());
        }

        String resultInfo = JSONObject.toJSONString(rt);
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
