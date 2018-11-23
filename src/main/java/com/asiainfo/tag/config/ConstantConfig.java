package com.asiainfo.tag.config;

import com.asiainfo.tag.utils.SettingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/10/11
 * Time: 上午10:53
 * Description: No Description
 */
@Slf4j
@Configuration
public class ConstantConfig {

    @Autowired(required = false)
    public void setDefaultType(@Value("${default.type}") String type) {
        SettingCache.TYPE = type;
        log.warn("初始化项目:   初始化集群 --> {}" ,type);
        SettingCache.DEFAULT_TYPE = type;
        log.warn("初始化项目:   初始化默认集群 --> {}" ,type);
    }
}
