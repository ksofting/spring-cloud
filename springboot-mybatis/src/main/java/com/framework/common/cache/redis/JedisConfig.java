package com.framework.common.cache.redis;

public class JedisConfig{
    private String host;
    
    private int port;
    
    private int timeout;
    
    private String password;
    
    private int database;
    
    public String getHost(){
        return host;
    }
    
    public void setHost(String host){
        this.host= host;
    }
    
    public int getPort(){
        return port;
    }
    
    public void setPort(int port){
        this.port= port;
    }
    
    public int getTimeout(){
        return timeout;
    }
    
    public void setTimeout(int timeout){
        this.timeout= timeout;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password= password;
    }
    
    public int getDatabase(){
        return database;
    }
    
    public void setDatabase(int database){
        this.database= database;
    }
    
}
