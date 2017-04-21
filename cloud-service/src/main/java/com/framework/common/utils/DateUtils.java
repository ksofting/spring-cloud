package com.framework.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期时间工具类
 * 
 * @author zxh
 * @version 2015-01-19
 */
public class DateUtils{
    private static final SimpleDateFormat datetimeFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static final SimpleDateFormat datetimeFormat2= new SimpleDateFormat("yyyyMMddHHmmss");
    
    private static final SimpleDateFormat datetimeFormat3= new SimpleDateFormat("yyyyMMddHHmmss:SSS");
    
    private static final SimpleDateFormat datetimeFormat4= new SimpleDateFormat("yyyyMMddHHmm");
    
    private static final SimpleDateFormat datetimeFormat5= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    private static final SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
    
    private static final SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm:ss");
    
    private static final SimpleDateFormat dateFormat2= new SimpleDateFormat("yyyyMMdd");
    
    private static final SimpleDateFormat chineseDateFormat= new SimpleDateFormat("yyyy年MM月dd日");
    
    /**
     * 获得当前日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     * 
     * @return
     */
    public static synchronized String currentDatetime(){
        return datetimeFormat.format(now());
    }
    
    /**
     * 获得当前日期时间
     * <p>
     * 日期时间格式yyyyMMddHHmmss
     * 
     * @return
     */
    public static synchronized String currentDatetime2(){
        return datetimeFormat2.format(now());
    }
    
    public static synchronized String currentDatetime4(){
        return datetimeFormat4.format(now());
    }
    
    /**
     * 格式化日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     * 
     * @return
     */
    public static synchronized String formatDatetime(Date date){
        return datetimeFormat.format(date);
    }
    
    public static synchronized String formatDatetime4(Date date){
        return datetimeFormat4.format(date);
    }
    
    /**
     * 添加指定时间
     * addDatetime4
     * @author zxh 
     * @param datetime
     * @param field
     * @param num
     * @return
     * @throws ParseException 
     * @since JDK 1.7
     */
    public static synchronized String addDatetime4(String datetime,int field,int num)
        throws ParseException{
        Calendar c= Calendar.getInstance();
        c.setTime(parseDatetime4(datetime));
        c.add(field,num);
        return formatDatetime4(c.getTime());
    }
    
    /**
     * 格式化日期时间
     * <p>
     * 日期时间格式yyyyMMddHHmmss
     * 
     * @return
     */
    public static synchronized String formatDatetime2(Date date){
        return datetimeFormat2.format(date);
    }
    
    /**
     * 格式化日期时间
     * 
     * @param date
     * @param pattern
     *            格式化模式，详见{@link SimpleDateFormat}构造器
     *            <code>SimpleDateFormat(String pattern)</code>
     * @return
     */
    public static synchronized String formatDatetime(Date date,String pattern){
        SimpleDateFormat customFormat= (SimpleDateFormat)datetimeFormat.clone();
        customFormat.applyPattern(pattern);
        return customFormat.format(date);
    }
    
    /**
     * 格式化日期时间
     * 
     * @param pattern
     *            格式化模式，详见{@link SimpleDateFormat}构造器
     *            <code>SimpleDateFormat(String pattern)</code>
     * @return
     */
    public static synchronized String formatDatetime(String pattern){
        SimpleDateFormat customFormat= (SimpleDateFormat)datetimeFormat.clone();
        customFormat.applyPattern(pattern);
        return customFormat.format(now());
    }
    
    /**
     * 获得当前日期
     * <p>
     * 日期格式yyyy-MM-dd
     * 
     * @return
     */
    public static synchronized String currentDate(){
        return dateFormat.format(now());
    }
    
    /**
     * 
    * @author xiaolisheng
    * @Title: currentDate2 
    * @Description: 获得当前日期 ， 日期格式yyyyMMdd
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static synchronized String currentDate2(){
        return dateFormat2.format(now());
    }
    
    /**
     * 格式化日期
     * <p>
     * 日期格式yyyy-MM-dd
     * 
     * @return
     */
    public static synchronized String formatDate(Date date){
        return dateFormat.format(date);
    }
    
    /**
     * 获得当前时间
     * <p>
     * 时间格式HH:mm:ss
     * 
     * @return
     */
    public static synchronized String currentTime(){
        return timeFormat.format(now());
    }
    
    /**
     * 格式化时间
     * <p>
     * 时间格式HH:mm:ss
     * 
     * @return
     */
    public static synchronized String formatTime(Date date){
        return timeFormat.format(date);
    }
    
    /**
     * 获得当前时间的<code>java.util.Date</code>对象
     * 
     * @return
     */
    public static Date now(){
        return new Date();
    }
    
    public static Calendar calendar(){
        Calendar cal= GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }
    
    /**
     * 获得当前时间的毫秒数
     * <p>
     * 详见{@link System#currentTimeMillis()}
     * 
     * @return
     */
    public static long millis(){
        return System.currentTimeMillis();
    }
    
    /**
     * 
     * 获得当前Chinese月份
     * 
     * @return
     */
    public static int month(){
        return calendar().get(Calendar.MONTH)+ 1;
    }
    
    /**
     * 获得月份中的第几天
     * 
     * @return
     */
    public static int dayOfMonth(){
        return calendar().get(Calendar.DAY_OF_MONTH);
    }
    
    public static int hourOfDay(){
        return calendar().get(Calendar.HOUR_OF_DAY);
    }
    
    /**
     * 今天是星期的第几天
     * 
     * @return
     */
    public static int dayOfWeek(){
        return calendar().get(Calendar.DAY_OF_WEEK);
    }
    
    /**
     * 今天是年中的第几天
     * 
     * @return
     */
    public static int dayOfYear(){
        return calendar().get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * 判断原日期是否在目标日期之前
     * 
     * @param src
     * @param dst
     * @return
     */
    public static boolean isBefore(Date src,Date dst){
        return src.before(dst);
    }
    
    /**
     * 判断原日期是否在目标日期之后
     * 
     * @param src
     * @param dst
     * @return
     */
    public static boolean isAfter(Date src,Date dst){
        return src.after(dst);
    }
    
    /**
     * 判断两日期是否相同
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isEqual(Date date1,Date date2){
        return date1.compareTo(date2)== 0;
    }
    
    /**
     * 判断某个日期是否在某个日期范围
     * 
     * @param beginDate
     *            日期范围开始
     * @param endDate
     *            日期范围结束
     * @param src
     *            需要判断的日期
     * @return
     */
    public static boolean between(Date beginDate,Date endDate,Date src){
        return beginDate.before(src)&& endDate.after(src);
    }
    
    /**
     * 获得当前月的最后一天
     * <p>
     * HH:mm:ss为0，毫秒为999
     * 
     * @return
     */
    public static Date lastDayOfMonth(){
        Calendar cal= calendar();
        cal.set(Calendar.DAY_OF_MONTH,0); // M月置零
        cal.set(Calendar.HOUR_OF_DAY,0);// H置零
        cal.set(Calendar.MINUTE,0);// m置零
        cal.set(Calendar.SECOND,0);// s置零
        cal.set(Calendar.MILLISECOND,0);// S置零
        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+ 1);// 月份+1
        cal.set(Calendar.MILLISECOND,- 1);// 毫秒-1
        return cal.getTime();
    }
    
    /**
     * 获得当前月的第一天
     * <p>
     * HH:mm:ss SS为零
     * 
     * @return
     */
    public static Date firstDayOfMonth(){
        Calendar cal= calendar();
        cal.set(Calendar.DAY_OF_MONTH,1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY,0);// H置零
        cal.set(Calendar.MINUTE,0);// m置零
        cal.set(Calendar.SECOND,0);// s置零
        cal.set(Calendar.MILLISECOND,0);// S置零
        return cal.getTime();
    }
    
    private static Date weekDay(int week){
        Calendar cal= calendar();
        cal.set(Calendar.DAY_OF_WEEK,week);
        return cal.getTime();
    }
    
    /**
     * 获得周五日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     * 
     * @return
     */
    public static Date friday(){
        return weekDay(Calendar.FRIDAY);
    }
    
    /**
     * 获得周六日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     * 
     * @return
     */
    public static Date saturday(){
        return weekDay(Calendar.SATURDAY);
    }
    
    /**
     * 获得周日日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     * 
     * @return
     */
    public static Date sunday(){
        return weekDay(Calendar.SUNDAY);
    }
    
    /**
     * 将字符串日期时间转换成java.util.Date类型
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     * 
     * @param datetime
     * @return
     */
    public static synchronized Date parseDatetime(String datetime)
        throws ParseException{
        return datetimeFormat.parse(datetime);
    }
    
    public static synchronized Date parseDatetime3(String datetime)
        throws ParseException{
        return datetimeFormat3.parse(datetime);
    }
    
    public static synchronized Date parseDatetime4(String datetime)
        throws ParseException{
        return datetimeFormat4.parse(datetime);
    }
    
    /**
     * 将字符串日期时间转换成java.util.Date类型
     * <p>
     * 日期时间格式yyyyMMddHHmmss
     * 
     * @param datetime
     * @return
     */
    public static synchronized Date parseDatetime2(String datetime)
        throws ParseException{
        return datetimeFormat2.parse(datetime);
    }
    
    /**
     * 将字符串日期转换成java.util.Date类型
     * <p>
     * 日期时间格式yyyy-MM-dd
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static synchronized Date parseDate(String date)
        throws ParseException{
        return dateFormat.parse(date);
    }
    
    /**
     * 将字符串日期转换成java.util.Date类型
     * <p>
     * 时间格式 HH:mm:ss
     * 
     * @param time
     * @return
     * @throws ParseException
     */
    public static synchronized Date parseTime(String time)
        throws ParseException{
        return timeFormat.parse(time);
    }
    
    /**
     * 根据自定义pattern将字符串日期转换成java.util.Date类型
     * 
     * @param datetime
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static synchronized Date parseDatetime(String datetime,String pattern)
        throws ParseException{
        SimpleDateFormat format= (SimpleDateFormat)datetimeFormat.clone();
        format.applyPattern(pattern);
        return format.parse(datetime);
    }
    
    /**
     * 获取当前时间的上月或下月
     * 
     * @param n
     * @return
     * @throws ParseException
     */
    public static synchronized Date getNMonth(int n){
        Calendar c= Calendar.getInstance();
        c.add(Calendar.MONTH,n);
        return c.getTime();
    }
    
    public static Date getNDay(int n){
        Calendar c= Calendar.getInstance();
        c.add(Calendar.DATE,n);
        return c.getTime();
    }
    
    public static void main(String args[])
        throws ParseException{
        System.out.println(addDatetime4("201612261019",Calendar.MINUTE,- 1));
    }
    
    /**
     * 获取过去的分钟
     * @param date
     * @return
     */
    public static long pastMinutes(Date date){
        long t= new Date().getTime()- date.getTime();
        return t/ (60* 1000);
    }
    
    public static int getCurrYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    
    /**
     * 
    * @author xiaolisheng
    * @Title: formatDateByPattern 
    * @Description: 根据指定日期和格式返回字符串
    * @param @param date
    * @param @param pattern
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static synchronized String formatDateByPattern(Date date,String pattern){
        SimpleDateFormat simp= new SimpleDateFormat(pattern);
        return simp.format(date);
    }
    
    /**
     * 获得当前日期
     * <p>
     * 日期格式yyyy-MM-dd
     * 
     * @return
     */
    public static synchronized String currentChineseDate(){
        return chineseDateFormat.format(now());
    }
    
    public static synchronized String parseDatetime3To5(String datetime)
        throws Exception{
        if(StringUtils.isNotEmpty(datetime)){
            return datetimeFormat5.format(datetimeFormat3.parse(datetime));
        }
        return "";
    }
    
    public static synchronized String parseDatetime2To1(String datetime)
        throws Exception{
        if(StringUtils.isNotEmpty(datetime)){
            return datetimeFormat.format(datetimeFormat2.parse(datetime));
        }
        return "";
    }
    
    public static synchronized String parseTimes(String datetime)
        throws Exception{
        if(StringUtils.isNotEmpty(datetime)){
            return dateFormat.format(dateFormat2.parse(datetime));
        }
        return "";
    }
    
    public static synchronized Date parseTimesToDate(String datetime)
        throws Exception{
        if(StringUtils.isNotEmpty(datetime)){
            return dateFormat2.parse(datetime);
        }
        return null;
    }
    
    public static synchronized String formatTime(String datetime){
        Date date= new Date();
        date.setTime(Long.valueOf(datetime));
        return datetimeFormat.format(date);
    }
}
