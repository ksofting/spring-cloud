package com.framework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages= {
    "com.framework.common.mapper","com.framework.commerce.common.mapper"
})
@SpringBootApplication
public class Application{
    
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
    
}
