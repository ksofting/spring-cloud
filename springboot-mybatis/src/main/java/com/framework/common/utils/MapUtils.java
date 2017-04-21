package com.framework.common.utils;

import java.util.Map;

public class MapUtils extends org.apache.commons.collections.MapUtils{
    @SuppressWarnings("rawtypes")
    public static boolean hasSize(Map map){
        if(ObjectUtils.isNotNull(map)){
            return map.size()> 0;
        }
        return false;
    }
    
    @SuppressWarnings("rawtypes")
    public static void clear(Map map){
        if(null== map){
            return;
        }
        map.clear();
        map= null;
    }
}
