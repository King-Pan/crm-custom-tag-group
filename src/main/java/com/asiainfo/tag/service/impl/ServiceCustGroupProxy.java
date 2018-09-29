package com.asiainfo.tag.service.impl;

import com.asiainfo.tag.service.CustGroupService;
import com.asiainfo.tag.utils.SettingCache;
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
//@Service
public class ServiceCustGroupProxy implements CustGroupService {

    @Autowired
    @Qualifier("custGroupServiceImpl")
    private CustGroupService custGroupService;

    @Override
    public String checkTagInGroup(String jsonParma) {
        SettingCache.TYPE = SettingCache.SERVICE_TYPE;
        return custGroupService.checkTagInGroup(jsonParma);
    }

    @Override
    public String getUserTagGroupList(String jsonParma) {
        SettingCache.TYPE = SettingCache.SERVICE_TYPE;
        return custGroupService.getUserTagGroupList(jsonParma);
    }
}
