package com.asiainfo.service;


import com.asiainfo.tag.service.CustGroupService;
import com.asiainfo.tag.utils.SettingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * @author king-pan
 */

@Slf4j
@Component
@WebService(serviceName = "custcroupcheck", targetNamespace = "http://service.asiainfo.com", endpointInterface = "com.asiainfo.service.CustGroupCheck")
public class CustGroupCheckImpl implements CustGroupCheck {


    @Autowired
    @Qualifier("serviceCustGroupProxy")
    private CustGroupService custGroupService;

    @Override
    public String checkExistsNo(String jsonParam) {
        log.warn("查询某号码是否在客户群内参数: [" + jsonParam + "]");
        long startTime = System.currentTimeMillis();
        String result = custGroupService.checkTagInGroup(jsonParam);
        long endTime = System.currentTimeMillis();
        log.warn("查询某号码是否在客户群内,返回结果: " + result);
        log.warn("集群信息:" + SettingCache.TYPE);
        log.warn("查询某号码是否在客户群内共耗时: " + (endTime - startTime));
        return result;
    }

    @Override
    public String getCustGroupList(String jsonParam) {
        log.warn("查询某号码所有客户群-->参数: [" + jsonParam + "]");
        long startTime = System.currentTimeMillis();
        String result = custGroupService.getUserTagGroupList(jsonParam);
        long endTime = System.currentTimeMillis();
        log.warn("查询某号码所有客户群 返回结果:{}", result);
        log.warn("集群信息:" + SettingCache.TYPE);
        log.warn("查询某号码所有客户群共耗时: " + (endTime - startTime));
        return result;
    }
}
