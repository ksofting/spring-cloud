package com.framework.common.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 
 *封装各种生成唯一性ID算法的工具类.
 * 
 * @author  zhangxiaohui
 * @version  [版本号, 2015年12月9日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class IdGen{
    public static final int FIX_NUMBER9999= 9999;
    
    public static final int FIX_NUMBER999= 999;
    
    public static final int FIX_NUMBER99= 99;
    
    public static final int FIX_NUMBER2= 2;
    
    public static final int FIX_NUMBER3= 3;
    
    public static final int FIX_NUMBER4= 4;
    
    private static SecureRandom random= new SecureRandom();
    
    private static Random rd= new Random();
    
    private static IntSequence rowKeyID= new IntSequence(0,FIX_NUMBER9999);
    
    private static final SimpleDateFormat datetimeFormat= new SimpleDateFormat("yyMMddHHmmss");
    
    public static int getRandomInt(int num){
        return rd.nextInt(num);
    }
    
    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public synchronized static String generateKeyId(){
        return datetimeFormat.format(new Date())+ StringUtils.format(rowKeyID.getNextValue(),4);
    }
    
    /**
     * 使用SecureRandom随机生成Long. 
     */
    public static long randomLong(){
        return Math.abs(random.nextLong());
    }
    
    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     */
    public static String randomBase62(int length){
        byte[] randomBytes= new byte[length];
        random.nextBytes(randomBytes);
        return Encodes.encodeBase62(randomBytes);
    }
    
    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
