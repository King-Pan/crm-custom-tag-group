package com.asiainfo.tag.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author king-pan
 * @date 2018/11/17
 * @Description ${DESCRIPTION}
 */
@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ac = event.getApplicationContext();
        log.warn("=============================项目初始化工作  begin =============================");
        try {
            Connection connection = HbaseConnection.getInstance().getConnection();
            if(connection!=null && connection.getAdmin()!=null){
                log.warn("初始化生产集群连接成功");
            }else{
                log.error("初始化生产集群连接失败");
            }
        }catch (Exception e){
            log.error("初始化生产集群连接失败",e);
        }

        try {
            Connection connection = HbaseServiceConnection.getInstance().getConnection();
            if(connection!=null && connection.getAdmin()!=null){
                log.warn("初始化襄阳服务集群连接成功");
            }else{
                log.error("初始化襄阳服务集群连接失败");
            }
        }catch (Exception e){
            log.error("初始化襄阳服务集群连接失败",e);
        }
        log.warn("=============================项目初始化工作  end  =============================");
    }
}
