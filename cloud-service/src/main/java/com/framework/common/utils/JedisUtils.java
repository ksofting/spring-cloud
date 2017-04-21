package com.framework.common.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.SafeEncoder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Jedis Cache 工具类
 * 
 * @author  zhangxiaohui
 * @version  [版本号, 2015年11月24日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class JedisUtils{
    
    private static Logger logger= LoggerFactory.getLogger(JedisUtils.class);
    
    private static JedisPool jedisPool= SpringUtils.getBean(JedisPool.class);
    
    public static final String KEY_PREFIX= "sys:session:";
    
    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static String get(int index,String key){
        String value= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                value= jedis.get(key);
                value= StringUtils.isNotBlank(value)&& ! "nil".equalsIgnoreCase(value) ? value : null;
                logger.debug("get {} = {}",key,value);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("get {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return value;
    }
    
    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static byte[] get(int index,byte[] key){
        byte[] value= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                value= jedis.get(key);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("get {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return value;
    }
    
    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(int index,byte[] key,Class<T> c){
        T t= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                byte[] value= jedis.get(key);
                t= (T)SerializableUtils.deSerialize(value);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("get",e);
            returnBrokenResource(jedis);
        }
        return t;
    }
    
    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(int index,String key,Class<T> c){
        T t= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                byte[] value= jedis.get(SafeEncoder.encode(key));
                t= (T)SerializableUtils.deSerialize(value);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("get",e);
            returnBrokenResource(jedis);
        }
        return t;
    }
    
    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static Object getObject(int index,String key){
        Object value= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(getBytesKey(key))){
                value= toObject(jedis.get(getBytesKey(key)));
                logger.debug("getObject {} = {}",key,value);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("getObject {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return value;
    }
    
    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String set(int index,String key,String value,int cacheSeconds){
        String result= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.set(key,value);
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            logger.debug("set {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("set {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    public static String set(int index,byte[] key,byte[] value,int cacheSeconds){
        String result= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.set(key,value);
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    public static String setBean(int index,String key,Object value,int cacheSeconds){
        String result= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.set(SafeEncoder.encode(key),SerializableUtils.serialize(value));
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    public static String zSetAdd(int index,String key,double score,String member){
        String result= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            jedis.zadd(key,score,member);
            logger.debug("set {} = {} = {}",key,score,member);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("set {} = {} = {}",key,score,member);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    public static Set<String> zRange(int index,String key,long start,long end){
        Jedis jedis= null;
        boolean isclose= false;
        try{
            jedis= getResource(index);
            logger.debug("zrange {} = {} = {}",key,start,end);
            return jedis.zrange(key,start,end);
        }
        catch(Exception e){
            logger.error("zrange {} = {} = {}",key,start,end);
            returnBrokenResource(jedis);
            isclose= true;
        }
        finally{
            if(! isclose)
                returnResource(jedis);
        }
        return null;
    }
    
    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String setObject(int index,String key,Object value,int cacheSeconds){
        String result= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.set(getBytesKey(key),toBytes(value));
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            logger.debug("setObject {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("setObject {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 获取List缓存
     * @param key 键
     * @return 值
     */
    public static List<String> getList(int index,String key){
        List<String> value= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                value= jedis.lrange(key,0,- 1);
                logger.debug("getList {} = {}",key,value);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("getList {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return value;
    }
    
    /**
     * 获取List缓存
     * @param key 键
     * @return 值
     */
    public static List<Object> getObjectList(int index,String key){
        List<Object> value= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(getBytesKey(key))){
                List<byte[]> list= jedis.lrange(getBytesKey(key),0,- 1);
                value= Lists.newArrayList();
                for(byte[] bs : list){
                    value.add(toObject(bs));
                }
                logger.debug("getObjectList {} = {}",key,value);
                returnResource(jedis);
            }
        }
        catch(Exception e){
            logger.error("getObjectList {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return value;
    }
    
    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setList(int index,String key,Collection<String> value,int cacheSeconds){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                jedis.del(key);
            }
            result= jedis.rpush(key,value.toArray(new String[value.size()]));
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            logger.debug("setList {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("setList {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long rpush(int index,String key,String...value){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.rpush(key,value);
            logger.debug("rpush {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("rpush {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setObjectList(int index,String key,List<Object> value,int cacheSeconds){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(getBytesKey(key))){
                jedis.del(key);
            }
            List<byte[]> list= Lists.newArrayList();
            for(Object o : value){
                list.add(toBytes(o));
            }
            result= jedis.rpush(getBytesKey(key),(byte[][])list.toArray());
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            logger.debug("setObjectList {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("setObjectList {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 向List缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long listAdd(int index,String key,String...value){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.rpush(key,value);
            logger.debug("listAdd {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("listAdd {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 向List缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long listObjectAdd(int index,String key,Object...value){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            List<byte[]> list= Lists.newArrayList();
            for(Object o : value){
                list.add(toBytes(o));
            }
            result= jedis.rpush(getBytesKey(key),(byte[][])list.toArray());
            logger.debug("listObjectAdd {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("listObjectAdd {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static Set<String> getSet(int index,String key){
        Set<String> value= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                value= jedis.smembers(key);
                logger.debug("getSet {} = {}",key,value);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("getSet {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return value;
    }
    
    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public static Set<Object> getObjectSet(int index,String key){
        Set<Object> value= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(getBytesKey(key))){
                value= Sets.newHashSet();
                Set<byte[]> set= jedis.smembers(getBytesKey(key));
                for(byte[] bs : set){
                    value.add(toObject(bs));
                }
                logger.debug("getObjectSet {} = {}",key,value);
                returnResource(jedis);
            }
        }
        catch(Exception e){
            logger.error("getObjectSet {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return value;
    }
    
    /**
     * 设置Set缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setSet(int index,String key,Set<String> value,int cacheSeconds){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                jedis.del(key);
            }
            result= jedis.sadd(key,(String[])value.toArray());
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            logger.debug("setSet {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("setSet {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 设置Set缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setObjectSet(int index,String key,Set<Object> value,int cacheSeconds){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(getBytesKey(key))){
                jedis.del(key);
            }
            Set<byte[]> set= Sets.newHashSet();
            for(Object o : value){
                set.add(toBytes(o));
            }
            result= jedis.sadd(getBytesKey(key),(byte[][])set.toArray());
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            logger.debug("setObjectSet {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("setObjectSet {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 向Set缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long setSetAdd(int index,String key,String...value){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.sadd(key,value);
            logger.debug("setSetAdd {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.warn("setSetAdd {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 向Set缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long setSetObjectAdd(int index,String key,Object...value){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            Set<byte[]> set= Sets.newHashSet();
            for(Object o : value){
                set.add(toBytes(o));
            }
            result= jedis.rpush(getBytesKey(key),(byte[][])set.toArray());
            logger.debug("setSetObjectAdd {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("setSetObjectAdd {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    /**
     * 获取Map缓存
     * @param key 键
     * @return 值
     */
    public static Map<String,String> getMap(int index,String key){
        Map<String,String> value= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                value= jedis.hgetAll(key);
                logger.debug("getMap {} = {}",key,value);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("getMap {} = {}",key,value,e);
            returnBrokenResource(jedis);
        }
        return value;
    }
    
    /**
     * 获取Map缓存
     * @param key 键
     * @return 值
     */
    public static Map<String,Object> getObjectMap(int index,String key){
        Map<String,Object> value= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(getBytesKey(key))){
                value= Maps.newHashMap();
                Map<byte[],byte[]> map= jedis.hgetAll(getBytesKey(key));
                for(Map.Entry<byte[],byte[]> e : map.entrySet()){
                    value.put(StringUtils.toString(e.getKey()),toObject(e.getValue()));
                }
                logger.debug("getObjectMap {} = {}",key,value);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("getObjectMap {} = {}",key,value,e);
        }
        return value;
    }
    
    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String setMap(int index,String key,Map<String,String> value,int cacheSeconds){
        String result= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                jedis.del(key);
            }
            result= jedis.hmset(key,value);
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            logger.debug("setMap {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("setMap {} = {}",key,value,e);
        }
        return result;
    }
    
    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String setObjectMap(int index,String key,Map<String,Object> value,int cacheSeconds){
        String result= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(getBytesKey(key))){
                jedis.del(key);
            }
            Map<byte[],byte[]> map= Maps.newHashMap();
            for(Map.Entry<String,Object> e : value.entrySet()){
                map.put(getBytesKey(e.getKey()),toBytes(e.getValue()));
            }
            result= jedis.hmset(getBytesKey(key),(Map<byte[],byte[]>)map);
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            logger.debug("setObjectMap {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("setObjectMap {} = {}",key,value,e);
        }
        return result;
    }
    
    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static String mapPut(int index,String key,Map<String,String> value){
        String result= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.hmset(key,value);
            logger.debug("mapPut {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("mapPut {} = {}",key,value,e);
        }
        return result;
    }
    
    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public static String mapObjectPut(int index,String key,Map<String,Object> value){
        String result= null;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            Map<byte[],byte[]> map= Maps.newHashMap();
            for(Map.Entry<String,Object> e : value.entrySet()){
                map.put(getBytesKey(e.getKey()),toBytes(e.getValue()));
            }
            result= jedis.hmset(getBytesKey(key),(Map<byte[],byte[]>)map);
            logger.debug("mapObjectPut {} = {}",key,value);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("mapObjectPut {} = {}",key,value,e);
        }
        return result;
    }
    
    /**
     * 移除Map缓存中的值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long mapRemove(int index,String key,String mapKey){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.hdel(key,mapKey);
            logger.debug("mapRemove {}  {}",key,mapKey);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("mapRemove {}  {}",key,mapKey,e);
        }
        return result;
    }
    
    /**
     * 移除Map缓存中的值
     * @param key 键
     * @param value 值
     * @return
     */
    public static long mapObjectRemove(int index,String key,String mapKey){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.hdel(getBytesKey(key),getBytesKey(mapKey));
            logger.debug("mapObjectRemove {}  {}",key,mapKey);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("mapObjectRemove {}  {}",key,mapKey,e);
        }
        return result;
    }
    
    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param value 值
     * @return
     */
    public static boolean mapExists(int index,String key,String mapKey){
        boolean result= false;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.hexists(key,mapKey);
            logger.debug("mapExists {}  {}",key,mapKey);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("mapExists {}  {}",key,mapKey,e);
        }
        return result;
    }
    
    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param value 值
     * @return
     */
    public static boolean mapObjectExists(int index,String key,String mapKey){
        boolean result= false;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.hexists(getBytesKey(key),getBytesKey(mapKey));
            logger.debug("mapObjectExists {}  {}",key,mapKey);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("mapObjectExists {}  {}",key,mapKey,e);
        }
        return result;
    }
    
    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    public static long del(int index,String key){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(key)){
                result= jedis.del(key);
                logger.debug("del {}",key);
            }
            else{
                logger.debug("del {} not exists",key);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("del {}",key,e);
        }
        return result;
    }
    
    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    public static long delObject(int index,String key){
        long result= 0;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            if(jedis.exists(getBytesKey(key))){
                result= jedis.del(getBytesKey(key));
                logger.debug("delObject {}",key);
            }
            else{
                logger.debug("delObject {} not exists",key);
            }
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("delObject {}",key,e);
        }
        return result;
    }
    
    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    public static boolean exists(int index,String key){
        boolean result= false;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.exists(key);
            logger.debug("exists {}",key);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("exists {}",key,e);
        }
        return result;
    }
    
    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    public static boolean hexists(int index,String key,String field){
        boolean result= false;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.hexists(key,field);
            logger.debug("hexists {}={}",key,field);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("hexists {}={}",key,field,e);
        }
        return result;
    }
    
    public static void hdel(int index,String key,String...fields){
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            jedis.hdel(key,fields);
            logger.debug("hdel {}={}",key,fields);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("hdel {}={}",key,fields,e);
        }
    }
    
    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    public static boolean existsObject(int index,String key){
        boolean result= false;
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            result= jedis.exists(getBytesKey(key));
            logger.debug("existsObject {}",key);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
            logger.error("existsObject {}",key,e);
        }
        return result;
    }
    
    /**
     * 获取资源
     * @return
     * @throws JedisException
     */
    public static Jedis getResource(int index)
        throws JedisException{
        Jedis jedis= null;
        try{
            jedis= jedisPool.getResource();
            if(index!= 0){
                jedis.select(index);
            }
        }
        catch(JedisException e){
            logger.warn("getResource.",e);
            returnBrokenResource(jedis);
            throw e;
        }
        return jedis;
    }
    
    /**
     * 归还资源
     * @param jedis
     * @param isBroken
     */
    public static void returnBrokenResource(Jedis jedis){
        if(ObjectUtils.isNotNull(jedis)){
            jedisPool.returnBrokenResource(jedis);
        }
    }
    
    /**
     * 释放资源
     * @param jedis
     * @param isBroken
     */
    public static void returnResource(Jedis jedis){
        if(ObjectUtils.isNotNull(jedis)){
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 获取byte[]类型Key
     * @param key
     * @return
     */
    public static byte[] getBytesKey(Object object){
        if(object instanceof String){
            return StringUtils.getBytes((String)object);
        }
        else{
            return ObjectUtils.serialize(object);
        }
    }
    
    public static Set<String> getKeySet(int index,String key){
        Jedis jedis= null;
        boolean isclose= false;
        try{
            jedis= getResource(index);
            return jedis.keys(key);
        }
        catch(Exception e){
            isclose= true;
            returnBrokenResource(jedis);
            logger.error("getKeySet {}",key,e);
            return null;
        }
        finally{
            if(! isclose)
                returnResource(jedis);
        }
        
    }
    
    /**
     * Object转换byte[]类型
     * @param key
     * @return
     */
    public static byte[] toBytes(Object object){
        return ObjectUtils.serialize(object);
    }
    
    public static void flushDB(int index){
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            jedis.flushDB();
            returnResource(jedis);
        }
        catch(JedisException e){
            logger.error("getResource.",e);
            returnBrokenResource(jedis);
            throw e;
        }
    }
    
    /**
     * byte[]型转换Object
     * @param key
     * @return
     */
    public static Object toObject(byte[] bytes){
        return ObjectUtils.unserialize(bytes);
    }
    
    /**
     * 生成id
     * @param args
     * @return
     */
    public static String generateId(String...args){
        StringBuffer buff= new StringBuffer();
        for(int i= 0;i< args.length;i++ ){
            if(i!= 0){
                buff.append(":");
            }
            buff.append(args[i]);
        }
        return buff.toString();
    }
    
    /**
     * hset
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static void hset(int index,String key,String field,String value){
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            jedis.hset(key,field,value);
            logger.debug("hset {} = {} = {}",key,field,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("hset {} = {} = {}",key,field,value,e);
            returnBrokenResource(jedis);
        }
    }
    
    public static void hset(int index,String key,String field,String value,int cacheSeconds){
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            jedis.hset(key,field,value);
            if(cacheSeconds!= 0){
                jedis.expire(key,cacheSeconds);
            }
            logger.debug("hset {} = {} = {}",key,field,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("hset {} = {} = {}",key,field,value,e);
            returnBrokenResource(jedis);
        }
    }
    
    public static void hset(int index,byte[] key,byte[] field,byte[] value){
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            jedis.hset(key,field,value);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("hset",e);
            returnBrokenResource(jedis);
        }
    }
    
    public static void hsetBean(int index,String key,String field,Object value){
        Jedis jedis= null;
        try{
            jedis= getResource(index);
            jedis.hset(SafeEncoder.encode(key),SafeEncoder.encode(field),SerializableUtils.serialize(value));
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("hset",e);
            returnBrokenResource(jedis);
        }
    }
    
    public static String hget(int index,String key,String field){
        Jedis jedis= null;
        String result= null;
        try{
            jedis= getResource(index);
            result= jedis.hget(key,field);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    public static byte[] hget(int index,byte[] key,byte[] field){
        Jedis jedis= null;
        byte[] result= null;
        try{
            jedis= getResource(index);
            result= jedis.hget(key,field);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T hgetBean(int index,String key,String field,Class<T> c){
        Jedis jedis= null;
        T intance= null;
        try{
            jedis= getResource(index);
            byte[] result= jedis.hget(SafeEncoder.encode(key),SafeEncoder.encode(field));
            intance= (T)SerializableUtils.deSerialize(result);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
        }
        return intance;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T hgetBean(int index,byte[] key,byte[] field,Class<T> c){
        Jedis jedis= null;
        T intance= null;
        try{
            jedis= getResource(index);
            byte[] result= jedis.hget(key,field);
            intance= (T)SerializableUtils.deSerialize(result);
            returnResource(jedis);
        }
        catch(Exception e){
            returnBrokenResource(jedis);
        }
        return intance;
    }
    
    public static Map<String,String> hgetAll(int index,String key){
        Jedis jedis= null;
        Map<String,String> result= null;
        try{
            jedis= getResource(index);
            result= jedis.hgetAll(key);
            logger.debug("hgetAll {}  ",key);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("hgetAll {}  ",key,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    public static Map<byte[],byte[]> hgetAll(int index,byte[] key){
        Jedis jedis= null;
        Map<byte[],byte[]> result= null;
        try{
            jedis= getResource(index);
            result= jedis.hgetAll(key);
            logger.debug("hgetAll {}  ",key);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("hgetAll {} ",key,e);
            returnBrokenResource(jedis);
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Map<String,T> hgetAllBean(int index,String key,Class<T> c){
        Jedis jedis= null;
        Map<String,T> returnMap= null;
        try{
            jedis= getResource(index);
            Map<byte[],byte[]> result= jedis.hgetAll(SafeEncoder.encode(key));
            if(MapUtils.hasSize(result)){
                returnMap= new HashMap<String,T>();
                Iterator<Entry<byte[],byte[]>> iter= result.entrySet().iterator();
                Entry<byte[],byte[]> entry;
                while(iter.hasNext()){
                    entry= iter.next();
                    returnMap.put(SafeEncoder.encode(entry.getKey()),(T)SerializableUtils.deSerialize(entry.getValue()));
                    iter.remove();
                }
            }
            logger.debug("hgetAll {}  ",key);
            returnResource(jedis);
        }
        catch(Exception e){
            logger.error("hgetAll {} ",key,e);
            returnBrokenResource(jedis);
        }
        return returnMap;
    }
}
