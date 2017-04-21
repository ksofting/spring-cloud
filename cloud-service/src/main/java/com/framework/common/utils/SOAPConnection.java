package com.framework.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * 
 *对指定webservice 发送soap请求
 * 
 * @author  zhangxiaohui
 * @version  [版本号, Jul 6, 2010]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SOAPConnection{
    private URL connUrl= null;
    
    /**
     * <默认构造函数>
     */
    public SOAPConnection(String url)
        throws Exception{
        connUrl= new URL(url); //SOAP服务的网址
    }
    
    /**
     * 获取请求连接
     * @return
     * @see [类、类#方法、类#成员]
     */
    public HttpURLConnection getRequestConnection(){
        try{
            HttpURLConnection httpConn= (HttpURLConnection)connUrl.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            httpConn.setUseCaches(false);
            httpConn.setDefaultUseCaches(false);
            return httpConn;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 指定地址发送
     * @param bdata
     * @param httpConn
     * @return
     * @throws Exception 
     * @see [类、类#方法、类#成员]
     */
    public String sendResp(byte[] data,Map<String,String> headMap)
        throws Exception{
        int code= 0;
        InputStream stream= null;
        OutputStream out= null;
        try{
            HttpURLConnection httpConn= getRequestConnection();
            httpConn.addRequestProperty("content-type","application/json;charset=UTF-8");
            if(MapUtils.hasSize(headMap)){
                Set<String> keySet= headMap.keySet();
                for(String key : keySet){
                    httpConn.addRequestProperty(key,headMap.get(key));
                }
            }
            httpConn.setConnectTimeout(20000);
            httpConn.setReadTimeout(15000);
            httpConn.connect(); //连接远程Url
            
            out= httpConn.getOutputStream(); //获取输出流对象
            out.write(data); //将要提交服务器的SOAP请求字符流写入输出流
            out.flush();
            
            code= httpConn.getResponseCode(); //用来获取服务器响应状态
            if(code!= HttpURLConnection.HTTP_OK){
                throw new ConnectException("connection code "+ code);
            }
            //服务器响应状态表示成功，这此处可以来获取服务器反馈的数据信息
            stream= httpConn.getInputStream();
            if(null== stream){
                return null;
            }
            byte[] respData= getHttpBody(httpConn.getContentLength(),stream);
            System.out.println(new String(respData));
            return StringUtils.toString(respData);
        }
        catch(Exception e){
            throw e;
        }
        finally{
            StreamUtils.close(out);
            StreamUtils.close(stream);
        }
    }
    
    protected byte[] getHttpBody(int length,InputStream in)
        throws IOException{
        byte[] data= null;
        if(length> 0){
            data= new byte[length];
            readFromInputStream(in,length,data);
        }
        else{
            data= readFromInputStream(in);
        }
        String str= new String(data).trim();
        return str.getBytes();
    }
    
    /**
     * 读取码流到EOF。
     *
     * @param inStream 输入码流
     * @param length 要读取的长度
     * @param data 存放码流
     * @throws IOException 
     * @throws IOException 
     * @throws IOException [参数说明]
     * @see [类、类#方法、类#成员]
     */
    public static void readFromInputStream(InputStream inStream,int length,byte[] data)
        throws IOException{
        DataInputStream in= new DataInputStream(inStream);
        //正常设置了Content-Length的
        if(length> 0){
            in.readFully(data,0,length);
        }
        else{
            throw new IllegalArgumentException("length must great than 0");
        }
    }
    
    public static byte[] readFromInputStream(InputStream inStream)
        throws IOException{
        DataInputStream in= new DataInputStream(inStream);
        //比最大限制的大小大一个
        byte[] temp= new byte[4* 1024];
        int n= in.read(temp);
        ByteArrayOutputStream bos= new ByteArrayOutputStream(100* 1024);
        while(n!= - 1){
            bos.write(temp,0,n);
            n= in.read(temp);
        }
        return bos.toByteArray();
    }
    
    public static void main(String[] args)
        throws Exception{
        SOAPConnection conn= new SOAPConnection("http://127.0.0.1:8080/feast/roll/prize");
        String json= "{actCode:\"c56ccaaeea6a4f118cce2b5d2d201c48\",drawnIP:\"128.232.103.202 \"}";
        String data= conn.sendResp(json.getBytes("UTF-8"),null);
        System.out.println(data);
    }
}
