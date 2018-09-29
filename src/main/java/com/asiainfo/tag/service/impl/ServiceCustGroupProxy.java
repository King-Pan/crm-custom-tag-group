package com.asiainfo.tag.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.tag.service.CustGroupService;
import com.asiainfo.tag.utils.SettingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

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
            SettingCache.TYPE = SettingCache.SERVICE_TYPE;
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getString("retcode").equals("-3")) {
                if (SettingCache.TYPE.equals(SettingCache.PROD_TYPE)) {
                    SettingCache.TYPE = SettingCache.SERVICE_TYPE;
                    log.warn("================checkTagInGroup 切换前集群: 生产集群，切换后集群: 服务集群================");
                }
                if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
                    SettingCache.TYPE = SettingCache.PROD_TYPE;
                    log.warn("================checkTagInGroup 切换前集群: 服务集群，切换后集群: 生产集群================");
                }
                result = custGroupService.checkTagInGroup(jsonParma);
            }
        } else {
            log.info("================checkTagInGroup 手动集群切换================");

        }
        log.info("================checkTagInGroup 方法结束================");
        return result;
    }

    @Override
    public String getUserTagGroupList(String jsonParma) {
        String result = custGroupService.getUserTagGroupList(jsonParma);
        if (SettingCache.IS_AUTOSWITCH) {
            log.warn("================getUserTagGroupList 自动集群切换================");
            SettingCache.TYPE = SettingCache.SERVICE_TYPE;
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getString("retcode").equals("-3")) {
                if (SettingCache.TYPE.equals(SettingCache.PROD_TYPE)) {
                    SettingCache.TYPE = SettingCache.SERVICE_TYPE;
                    log.warn("================getUserTagGroupList 切换前集群: 生产集群，切换后集群: 服务集群================");
                }
                if (SettingCache.TYPE.equals(SettingCache.SERVICE_TYPE)) {
                    SettingCache.TYPE = SettingCache.PROD_TYPE;
                    log.warn("================getUserTagGroupList 切换前集群: 服务集群，切换后集群: 生产集群================");
                }
                result = custGroupService.getUserTagGroupList(jsonParma);
            }
        } else {
            log.info("================getUserTagGroupList 手动集群切换================");
        }
        log.info("================getUserTagGroupList 方法结束================");
        return result;
    }
}
