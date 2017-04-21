package com.framework.commerce.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.framework.commerce.common.vo.UserVo;
import com.framework.commerce.service.UserService;
import com.framework.common.utils.CacheUtils;

@RestController
public class UserController{
    @Resource
    private UserService userService;
    
    @RequestMapping(value= "/home/user",method= RequestMethod.GET)
    public UserVo user(){
        UserVo user= userService.getUser("李四");
        return user;
    }
    
    @RequestMapping("/home/ehcache")
    @ResponseBody
    public String getEhCache(){
        Object obj= CacheUtils.get("test");
        if(null== obj){
            CacheUtils.put("test","test");
            obj= CacheUtils.get("test");
        }
        return String.valueOf(obj);
    }
}
