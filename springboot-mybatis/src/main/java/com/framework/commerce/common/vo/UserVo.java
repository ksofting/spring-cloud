package com.framework.commerce.common.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserVo implements Serializable{
    private String name;
    
    private int age;
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name= name;
    }
    
    public int getAge(){
        return age;
    }
    
    public void setAge(int age){
        this.age= age;
    }
    
}
