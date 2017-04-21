package com.framework.common.utils;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 定时器任务接口
 * 
 * @author  zhangxiaohui
 * @version  [版本号, 2016年2月23日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class SafeTimerTask extends TimerTask{
    private Logger log= LoggerFactory.getLogger(getClass());
    
    private String name;
    
    public SafeTimerTask(){
        this(null);
    }
    
    public SafeTimerTask(String name){
        this.name= name;
    }
    
    public void run(){
        try{
            if(null!= name){
                log.info("Execute timer-task: "+ name);
            }
            execute();
        }
        catch(Throwable e){
            log.error("TimerTask ["+ name+ "] run failed.",e);
        }
    }
    
    protected abstract void execute();
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name= name;
    }
    
}
