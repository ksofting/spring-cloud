package com.framework.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

/**
 * 
 * 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
 * 
 * @author  zhangxiaohui
 * @version  [版本号, 2015年11月24日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils{
    
    /**
     * 注解到对象复制，只复制能匹配上的方法。
     * @param annotation
     * @param object
     */
    public static void annotationToObject(Object annotation,Object object){
        if(ObjectUtils.isNotNull(annotation)){
            Class<?> annotationClass= annotation.getClass();
            Class<?> objectClass= object.getClass();
            for(Method m : objectClass.getMethods()){
                if(StringUtils.startsWith(m.getName(),"set")){
                    try{
                        String s= StringUtils.uncapitalize(StringUtils.substring(m.getName(),3));
                        Object obj= annotationClass.getMethod(s).invoke(annotation);
                        if(ObjectUtils.isNotNull(obj)){
                            if(ObjectUtils.isNull(object)){
                                object= objectClass.newInstance();
                            }
                            m.invoke(object,obj);
                        }
                    }
                    catch(Exception e){
                        // 忽略所有设置失败方法
                    }
                }
            }
        }
    }
    
    /**
     * 序列化对象
     * @param object
     * @return
     */
    public static byte[] serialize(Object object){
        ObjectOutputStream oos= null;
        ByteArrayOutputStream baos= null;
        try{
            if(ObjectUtils.isNotNull(object)){
                baos= new ByteArrayOutputStream();
                oos= new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 反序列化对象
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes){
        ByteArrayInputStream bais= null;
        try{
            if(ArrayUtils.hasSize(bytes)){
                bais= new ByteArrayInputStream(bytes);
                ObjectInputStream ois= new ObjectInputStream(bais);
                return ois.readObject();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 判断对象是否为空
     * @author: zhangxiaohui
     * @date  : 2016年1月28日
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj){
        return obj== null ? true : false;
    }
    
    public static boolean isNotNull(Object obj){
        return ! isNull(obj);
    }
    
    public static boolean equals(final Object object1,final Object object2){
        if(isNull(object1)){
            return false;
        }
        if(object1 instanceof Number|| object1 instanceof String){
            return object1.equals(object2);
        }
        return object1== object2;
    }
    
    public static boolean notEquals(final Object object1,final Object object2){
        return ! equals(object1,object2);
    }
    
    public static String dataToJson(Object obj)
    {
        return JsonUtils.toJson(obj);
    }
}
