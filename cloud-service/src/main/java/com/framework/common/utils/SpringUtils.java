package com.framework.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy(false)
public final class SpringUtils implements ApplicationContextAware{
    
    private static ApplicationContext appContext;
    
    /**
     * 当继承了ApplicationContextAware类之后，那么程序在调用  
     * getBean(String)的时候会自动调用该方法，不用自己操作  
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException{
        appContext= applicationContext;
    }
    
    public static ApplicationContext getApplicationContext(){
        return appContext;
    }
    
    /**
     * 获取对象
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws org.springframework.beans.BeansException
     *
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name)
        throws BeansException{
        return (T)appContext.getBean(name);
    }
    
    /**
     * 获取类型为requiredType的对象
     *
     * @param clz
     * @return
     * @throws org.springframework.beans.BeansException
     *
     */
    public static <T> T getBean(Class<T> clz)
        throws BeansException{
        T result= (T)appContext.getBean(clz);
        return result;
    }
    
    /**
     * 如果appContext包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name){
        return appContext.containsBean(name);
    }
    
    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *
     */
    public static boolean isSingleton(String name)
        throws NoSuchBeanDefinitionException{
        return appContext.isSingleton(name);
    }
    
    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *
     */
    public static Class<?> getType(String name)
        throws NoSuchBeanDefinitionException{
        return appContext.getType(name);
    }
    
    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *
     */
    public static String[] getAliases(String name)
        throws NoSuchBeanDefinitionException{
        return appContext.getAliases(name);
    }
    
}
