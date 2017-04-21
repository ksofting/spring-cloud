package com.framework.common.utils;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * session处理
 * @author  lingchonghua
 * @version  [版本号, 2012-5-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SessionUtil{
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * 从session中获取信息
     * @param request
     * @param key
     * @return Object [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public static Object getAttribute(HttpServletRequest request,Serializable key){
        
        HttpSession session= request.getSession(false);
        if(null== session){
            return null;
        }
        return session.getAttribute(key.toString());
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * 从session存放登录信息
     * @param request
     * @param loginInfo [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void setAttribute(HttpServletRequest request,String key,Object value){
        
        HttpSession session= request.getSession(true);
        session.setAttribute(key,value);
    }
    
}
