package com.framework.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.framework.common.mapper.model.User;
import com.framework.common.mapper.model.UserCriteria;

public interface UserMapper{
    int countByExample(UserCriteria example);
    
    int deleteByExample(UserCriteria example);
    
    int deleteByPrimaryKey(Integer id);
    
    int insert(User record);
    
    int insertSelective(User record);
    
    List<User> selectByExample(UserCriteria example);
    
    User selectByPrimaryKey(Integer id);
    
    int updateByExampleSelective(@Param("record")
    User record,@Param("example")
    UserCriteria example);
    
    int updateByExample(@Param("record")
    User record,@Param("example")
    UserCriteria example);
    
    int updateByPrimaryKeySelective(User record);
    
    int updateByPrimaryKey(User record);
}
