package com.framework.commerce.common.mapper;

import com.framework.commerce.common.vo.UserVo;
import com.framework.common.mapper.UserMapper;

public interface UserDAO extends UserMapper{
    public UserVo selectUserCount(String userName);
}
