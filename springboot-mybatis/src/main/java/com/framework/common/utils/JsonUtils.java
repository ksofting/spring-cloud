package com.framework.common.utils;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils{
    
    private static ObjectMapper mapper= new ObjectMapper();
    
    public static String serial(Object obj){
        try{
            return mapper.writeValueAsString(obj);
        }
        catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static String toJson(Object obj){
        try{
            return mapper.writeValueAsString(obj);
        }
        catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
    
    static public <T> T toObject(Class<T> clazz,String v){
        try{
            return mapper.readValue(v,clazz);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    static public <T> T toObject(Class<T> clazz,String v,String dateFormat){
        try{
            mapper.setDateFormat(new SimpleDateFormat(dateFormat));
            return mapper.readValue(v,clazz);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
}
