package com.asiainfo.tag.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/8/17
 * Time: 下午2:41
 * Description: 数据同步工具类
 */
@Slf4j
@Component
public class SynchronizeUtils {

    @Value("${synchronize.urls}")
    private String urls;

    /**
     * 获取设置自动服务地址
     *
     * @return 服务地址集合
     */
    private List<String> getSynchronizeList() {
        String[] urlAry = urls.split(",");
        List<String> list = new ArrayList<>();
        for (String add : urlAry) {
            list.add(add + "/synchronizeData");
        }
        log.info("需要同步设置自动服务地址：" + list);
        return list;
    }

    /**
     * 同步设置日期
     *
     * @param date
     */
    public void asyncSetDate(String auto, String type, String date) {
        List<String> list = getSynchronizeList();
        Map<String, String> params = new HashMap<>(3);
        params.put("auto", auto);
        params.put("type", type);
        params.put("date", date);
        String result;
        for (String url : list) {
            result = HttpClientUtil.doGet(url, params);
            log.info("同步手动设置时间： 请求地址=" + url + ",参数=" + params + ",结果=" + result);
        }
    }
}
