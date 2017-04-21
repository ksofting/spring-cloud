package com.framework.commerce.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.framework.commerce.common.vo.UserVo;
import com.framework.commerce.service.UserService;

@RestController
public class UserController{
    @Resource
    private UserService userService;
    
    @RequestMapping("/home/user")
    @ResponseBody
    public String user(){
        UserVo user= userService.readUserInfo();
        return user.getName()+ "-----"+ user.getAge();
    }
}
