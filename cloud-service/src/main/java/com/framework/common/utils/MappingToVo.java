package com.framework.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

public class MappingToVo {
	public static  <T> T mappingToVO(T source,Class<? extends T> clazz){
		try{
			T extend = clazz.getConstructor().newInstance();
			BeanUtils.copyProperties(source, extend);
		 
			return extend;
		}catch(Throwable t){
			t.printStackTrace();
			return source;
		}
	}
	public static <T> List<T> mappingToVO(List<T> data,Class<? extends T> clazz){
        try{
            List<T> list =new ArrayList<T>();
            T extend =null;
            for(T t : data){
                extend= clazz.getConstructor().newInstance();
                BeanUtils.copyProperties(t, extend);
                list.add(extend);
            }
            return list;
        }catch(Throwable t){
            t.printStackTrace();
            return data;
        }
    }
	
	public static  <T> T mappingAToB(Object A, Class<T> B){
        try{
            T extend = B.getConstructor().newInstance();
            BeanUtils.copyProperties(A, extend);
            return extend;
        }catch(Throwable t){
            t.printStackTrace();
            return null;
        }
    }
}
