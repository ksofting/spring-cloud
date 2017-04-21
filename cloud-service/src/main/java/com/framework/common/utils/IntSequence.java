package com.framework.common.utils;

/**
 * 序列数
 * @author zhangxiaohui
 * @date   2016年3月30日
 */
public class IntSequence{
    private int minValue;
    
    private int maxValue;
    
    private int currValue;
    
    public IntSequence(int min,int max){
        int inMinValue= min;
        int inMaxValue= max;
        if(inMinValue> inMaxValue){
            int tmp= inMinValue;
            inMinValue= inMaxValue;
            inMaxValue= tmp;
        }
        this.minValue= inMinValue;
        this.maxValue= inMaxValue;
        this.currValue= inMaxValue;
    }
    
    public synchronized int getNextValue(){
        if(currValue== maxValue){
            currValue= minValue;
            return currValue;
        }
        return ++ currValue;
    }
    
    public synchronized void setCurrValue(int currValue){
        if((currValue< minValue)|| (currValue> maxValue)){
            return;
        }
        this.currValue= currValue;
    }
    
    public synchronized int getMinValue(){
        return this.minValue;
    }
    
    public synchronized int getMaxValue(){
        return this.maxValue;
    }
    
}
