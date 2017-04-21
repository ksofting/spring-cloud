package com.framework.commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.framework.commerce.common.mapper.UserDAO;
import com.framework.commerce.common.vo.UserVo;
import com.framework.common.mapper.model.User;
import com.framework.common.utils.CacheUtils;

@RestController
public class UserController{
    @Autowired
    UserDAO userDAO;
    
    @RequestMapping("/home/user")
    @ResponseBody
    public String user(){
        User user= userDAO.selectByPrimaryKey(1);
        UserVo userVo= userDAO.selectUserCount("李四");
        System.out.println(userVo.getName());
        return user.getName()+ "-----"+ user.getAge();
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
