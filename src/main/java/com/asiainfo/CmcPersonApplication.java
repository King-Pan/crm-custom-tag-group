package com.asiainfo;

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
        SpringApplication.run(CmcPersonApplication.class, args);
    }
}
