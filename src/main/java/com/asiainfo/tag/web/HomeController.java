package com.asiainfo.tag.web;


import static com.asiainfo.tag.utils.SettingCache.*;


import com.asiainfo.tag.common.ServerResponse;
import com.asiainfo.tag.utils.SettingCache;
import com.asiainfo.tag.utils.SynchronizeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/21
 * Time: 下午4:20
 * Description: No Description
 */
@Slf4j
@RestController
public class HomeController {

    @Autowired
    private SynchronizeUtils synchronizeUtils;


    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @RequestMapping("/query")
    public ModelAndView query() {
        return new ModelAndView("query");
    }



    @RequestMapping("/change")
    public Object changeInfo(@RequestParam("model") String model,
                             @RequestParam("type") String type,
                             @RequestParam("date") String date) {
        log.info("=================================>设置参数 begin <=================================");
        ServerResponse serverResponse;
        try {

            log.error("设置参数为: 模式->{},集群类型->{},日期->{}", model, type, date);
            synchronizeUtils.asyncSetDate(model, type, date);
            serverResponse = ServerResponse.createBySuccessMessage("配置信息设置成功");
        } catch (Exception e) {
            log.error("配置信息设置失败: \n" + e.getMessage(), e);
            serverResponse = ServerResponse.createByErrorMessage("配置信息设置失败: \n" + e.getMessage());
        }
        log.info("=================================>设置参数 end <=================================");
        return serverResponse;
    }


    @RequestMapping("/synchronizeData")
    public Object synchronizeData(@RequestParam("auto") String auto,
                                  @RequestParam("type") String type,
                                  @RequestParam("date") String date) {
        ServerResponse serverResponse;
        try {
            log.info("=================================>同步参数 begin <=================================");
            log.error("数据同步参数为: 自动模式->{},集群类型->{},日期->{}", auto, type, date);
            if (StringUtils.isNoneBlank(auto)) {
                if (!AUTO.equalsIgnoreCase(auto)) {
                    IS_AUTOSWITCH = false;
                    if (StringUtils.isNoneBlank(type)) {
                        TYPE = type;
                    } else {
                        throw new RuntimeException("集群类型不能为空");
                    }
                    if (StringUtils.isNoneBlank(date)) {
                        SWITCH_DATE = date;
                    } else {
                        throw new RuntimeException("日期不能为空");
                    }
                } else {
                    SWITCH_DATE = "";
                    TYPE = DEFAULT_TYPE;
                    IS_AUTOSWITCH = true;
                }
            } else {
                throw new RuntimeException("model不能为空");
            }
            serverResponse = ServerResponse.createBySuccessMessage("配置信息设置成功");
        } catch (Exception e) {
            log.error("配置信息设置失败: \n" + e.getMessage(), e);
            serverResponse = ServerResponse.createByErrorMessage("配置信息设置失败: \n" + e.getMessage());
        }
        log.info("=================================>同步参数 end <=================================");
        return serverResponse;
    }

    @RequestMapping("/getData")
    public Object getData() {
        log.info("=================================>获取参数 begin <=================================");
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("model", SettingCache.IS_AUTOSWITCH);
        resultMap.put("type", SettingCache.TYPE);
        resultMap.put("date", SettingCache.SWITCH_DATE);
        log.info("=================================>获取参数 end <=================================");
        return resultMap;
    }
}
