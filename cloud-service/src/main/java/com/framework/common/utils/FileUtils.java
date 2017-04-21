package com.framework.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.servlet.ServletContext;

/**
 * 
 * 文件处理通用类
 * 
 * @author zhangxiaohui
 * @version [版本号, 2015年12月8日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FileUtils extends org.apache.commons.io.FileUtils{
    /**
    * 文件服务器根路径
    */
    public static String FILE_PATH_ROOT;
    
    /**
     * 当前文件下标
     */
    public static Long FILE_NAME_INDEX;
    
    public static DecimalFormat df;
    
    /** 临时文件处理路径 */
    public static String TEMP_FILE_PATH;
    
    static{
        ServletContext context= SpringUtils.getBean(ServletContext.class);
        FILE_PATH_ROOT= context.getRealPath("upload")+ "/";
        FILE_NAME_INDEX= new Long("001001001001");
        df= new DecimalFormat("#0.000000000000");
        TEMP_FILE_PATH= context.getRealPath("temp")+ "/";
    }
    
    /**
     * 
    * @Title: saveTempFile 
    * @Description: 保存临时文件在应用服务器，处理完内容即删除
    * @param @param in
    * @param @param fileName
    * @param @return
    * @param @throws IOException    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static File saveTempFile(InputStream in,String fileName)
        throws IOException{
        fileName= System.currentTimeMillis()+ RandomUtils.generateMixString(4)+ "_"+ fileName;
        File tempFilePath= new File(TEMP_FILE_PATH);
        if(! tempFilePath.exists()){
            tempFilePath.mkdirs();
        }
        File tempFile= new File(TEMP_FILE_PATH,fileName);
        copyInputStreamToFile(in,tempFile);
        return tempFile;
    }
}
