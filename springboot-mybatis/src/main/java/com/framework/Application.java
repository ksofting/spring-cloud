package com.framework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan(basePackages= {
    "com.framework.common.mapper","com.framework.commerce.common.mapper"
})
@SpringBootApplication
@EnableDiscoveryClient
public class Application{
    
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
    
}
