package com.asiainfo.tag.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.tag.service.CustGroupService;
import com.asiainfo.tag.utils.FastJsonUtils;
import com.asiainfo.tag.utils.SettingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/27
 * Time: 下午5:17
 * Description: No Description
 */
@Slf4j
@Service
public class ServiceCustGroupProxy implements CustGroupService {

    @Autowired
    @Qualifier("custGroupServiceImpl")
    private CustGroupService custGroupService;

    @Override
    public String checkTagInGroup(String jsonParma) {
        String result = custGroupService.checkTagInGroup(jsonParma);
        if (SettingCache.IS_AUTOSWITCH) {
            log.warn("================checkTagInGroup 自动集群切换================");
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getString("retcode").equals("-3")) {
                if (SettingCache.TYPE.equals(SettingCache.PROD_TYPE)) {
                    SettingCache.TYPE = SettingCache.SERVICE_TYPE;
                    log.warn("================checkTagInGroup 切换前集群: 生产集群，切换后集群: 服务集群================");
                }else if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
                    SettingCache.TYPE = SettingCache.PROD_TYPE;
                    log.warn("================checkTagInGroup 切换前集群: 服务集群，切换后集群: 生产集群================");
                }
                result = custGroupService.checkTagInGroup(jsonParma);
            }
        } else {
            log.info("================checkTagInGroup 手动集群切换================");

        }
        log.info("================checkTagInGroup 方法结束================");
        JSONObject jsonObject = FastJsonUtils.getJsonObject(result);
        String retcode = jsonObject.getString("retcode");
        if ("-3".equals(retcode)) {
            JSONObject object = new JSONObject();
            object.put("errmsg", jsonObject.getString("errmsg"));
            object.put("retcode", "-2");
            result = FastJsonUtils.getBeanToJson(object);
        }
        return getResult(result);
    }

    @Override
    public String getUserTagGroupList(String jsonParma) {
        String result = custGroupService.getUserTagGroupList(jsonParma);
        if (SettingCache.IS_AUTOSWITCH) {
            log.warn("================getUserTagGroupList 自动集群切换================");
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getString("retcode").equals("-3")) {
                if (SettingCache.TYPE.equals(SettingCache.PROD_TYPE)) {
                    SettingCache.TYPE = SettingCache.SERVICE_TYPE;
                    log.warn("================getUserTagGroupList 切换前集群: 生产集群，切换后集群: 服务集群================");
                }else if(SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
                    SettingCache.TYPE = SettingCache.PROD_TYPE;
                    log.warn("================getUserTagGroupList 切换前集群: 服务集群，切换后集群: 生产集群================");
                }
                result = custGroupService.getUserTagGroupList(jsonParma);
            }
        } else {
            log.info("================getUserTagGroupList 手动集群切换================");
        }
        log.info("================getUserTagGroupList 方法结束================");
        return getResult(result);
    }

    private String getResult(String result) {
        JSONObject jsonObject = FastJsonUtils.getJsonObject(result);
        String retcode = jsonObject.getString("retcode");
        if ("-3".equals(retcode)) {
            JSONObject object = new JSONObject();
            object.put("retcode", "-2");
            object.put("errmsg", jsonObject.getString("errmsg"));
            result = FastJsonUtils.getBeanToJson(object);
        }
        log.info("查询的集群: " + SettingCache.TYPE);
        log.info("返回结果是：" + result);
        return result;
    }
}
