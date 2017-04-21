package com.framework.commerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.framework.commerce.common.vo.UserVo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UserService{
    @Autowired
    RestTemplate restTemplate;
    
    final String SERVICE_NAME= "cloud-service";
    
    @HystrixCommand(fallbackMethod= "fallbackSearchAll")
    public UserVo readUserInfo(){
        ResponseEntity<UserVo> response= restTemplate.getForEntity("http://"+ SERVICE_NAME+ "/home/user",UserVo.class);
        return response.getBody();
    }
    
    protected UserVo fallbackSearchAll(){
        UserVo user= new UserVo();
        user.setName("王五");
        user.setAge(22);
        return user;
    }
}
