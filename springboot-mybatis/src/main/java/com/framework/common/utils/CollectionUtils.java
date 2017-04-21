package com.framework.common.utils;

import java.util.Collection;

/**
 * 
 * Collection工具类
 * 
 * @author  zhangxiaohui
 * @version  [版本号, 2016年1月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils{
    /**
     * 
     * @author: zhangxiaohui
     * @date  : 2016年1月28日
     * @param list
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean hasSize(Collection list){
        if(ObjectUtils.isNotNull(list)){
            return ! list.isEmpty();
        }
        return false;
    }
}
