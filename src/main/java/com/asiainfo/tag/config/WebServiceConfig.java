package com.asiainfo.tag.config;

import com.asiainfo.tag.webservice.CustGroupCheck;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/21
 * Time: 下午1:00
 * Description: No Description
 */
@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private CustGroupCheck custGroupCheck;


    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, custGroupCheck);
        endpoint.publish("/custgroupcheck");
        return endpoint;
    }
}
