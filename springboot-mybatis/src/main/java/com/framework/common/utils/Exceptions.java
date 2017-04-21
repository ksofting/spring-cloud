package com.framework.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 关于异常的工具类.
 * 
 * @author  zhangxiaohui
 * @version  [版本号, 2015年12月9日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Exceptions
{
    
    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static RuntimeException unchecked(Exception e)
    {
        if (e instanceof RuntimeException)
        {
            return (RuntimeException)e;
        }
        else
        {
            return new RuntimeException(e);
        }
    }
    
    /**
     * 将ErrorStack转化为String.
     */
    public static String getStackTraceAsString(Throwable e)
    {
        if (ObjectUtils.isNull(e))
        {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
    
    /**
     * 判断异常是否由某些底层的异常引起.
     */
    @SuppressWarnings("unchecked")
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses)
    {
        Throwable cause = ex.getCause();
        while (cause != null)
        {
            for (Class<? extends Exception> causeClass : causeExceptionClasses)
            {
                if (causeClass.isInstance(cause))
                {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }
    
    /**
     * 在request中获取异常类
     * @param request
     * @return 
     */
    public static Throwable getThrowable(HttpServletRequest request)
    {
        Throwable ex = null;
        if (ObjectUtils.isNotNull(request.getAttribute("exception")))
        {
            ex = (Throwable)request.getAttribute("exception");
        }
        else if (ObjectUtils.isNotNull(request.getAttribute("javax.servlet.error.exception")))
        {
            ex = (Throwable)request.getAttribute("javax.servlet.error.exception");
        }
        return ex;
    }
    
    
    public static String getErrorInfoFromException(Exception e) {
        try {
               StringWriter sw = new StringWriter();
               PrintWriter pw = new PrintWriter(sw);
               e.printStackTrace(pw);
               String exceptionInfo = sw.toString();
               pw.close();
               sw.close();
               return exceptionInfo;
           } catch (Exception e2) {
               return e.getMessage();
           }
    }
    
}
