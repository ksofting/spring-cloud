package com.framework.commerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.commerce.common.mapper.UserDAO;
import com.framework.commerce.common.vo.UserVo;

@Service
public class UserService{
    @Autowired
    UserDAO userDAO;
    
    public UserVo getUser(String name){
        UserVo userVo= userDAO.selectUserCount(name);
        return userVo;
    }
}
