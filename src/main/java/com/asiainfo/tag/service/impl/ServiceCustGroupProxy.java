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
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getString("retcode").equals("-3")) {
            if (SettingCache.IS_AUTOSWITCH) {
                if (SettingCache.TYPE.equals(SettingCache.PROD_TYPE)) {
                    SettingCache.TYPE = SettingCache.SERVICE_TYPE;
                    log.error("================checkTagInGroup 切换前集群: 生产集群，切换后集群: 服务集群================");
                } else if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
                    SettingCache.TYPE = SettingCache.PROD_TYPE;
                    log.error("================checkTagInGroup 切换前集群: 服务集群，切换后集群: 生产集群================");
                }
                result = custGroupService.checkTagInGroup(jsonParma);
                result = getResult(result);
            } else {
                log.error("================checkTagInGroup 手动集群切换，出错不自动切换================");
                log.error("================checkTagInGroup: " + jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    @Override
    public String getUserTagGroupList(String jsonParma) {
        String result = custGroupService.getUserTagGroupList(jsonParma);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getString("retcode").equals("-3")) {
            if (SettingCache.IS_AUTOSWITCH) {
                log.error(jsonObject.getString("errmsg"));
                if (SettingCache.TYPE.equals(SettingCache.PROD_TYPE)) {
                    SettingCache.TYPE = SettingCache.SERVICE_TYPE;
                    log.error("================getUserTagGroupList 切换前集群: 生产集群，切换后集群: 服务集群================");
                } else if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
                    SettingCache.TYPE = SettingCache.PROD_TYPE;
                    log.error("================getUserTagGroupList 切换前集群: 服务集群，切换后集群: 生产集群================");
                }
                result = custGroupService.getUserTagGroupList(jsonParma);
                result = getResult(result);
            } else {
                log.error("================getUserTagGroupList 手动集群切换，出错不自动切换================");
                log.error("================getUserTagGroupList: " + jsonObject.getString("errmsg"));
            }
        }
        return result;
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
        return result;
    }
}
