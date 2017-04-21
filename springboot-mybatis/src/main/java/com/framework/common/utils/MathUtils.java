package com.framework.common.utils;

/**
 * 
 * 项目名称：k12py_dev <br>
 * 类名称：MathUtils <br>
 * 类描述：TODO(数学工具类) <br>
 * 创建人：何亚斌 <br>
 * 创建时间：2016年1月18日 上午10:10:32 <br>
 * 
 * @version V1.0
 */
public class MathUtils {
	private static final double EARTH_RADIUS = 6378137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 
	 * TODO(根据两点间经纬度坐标（double值），计算两点间距离，单位为米) 
	 * @author 何亚斌 2016年1月18日 上午10:11:06  
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	public static void main(String[] args){
		double d = getDistance(106.56590203857, 29.564136443476, 104.0679234633, 30.67994284542);
		
		System.out.println(d);
	}
}
