package com.asiainfo;

import com.asiainfo.tag.utils.ApplicationStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author king-pan
 */
@SpringBootApplication
@EnableSwagger2
public class CmcPersonApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CmcPersonApplication .class);
        springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);
    }
}
