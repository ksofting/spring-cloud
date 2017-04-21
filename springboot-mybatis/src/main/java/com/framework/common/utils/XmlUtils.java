package com.framework.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

public class XmlUtils {
	private static Logger logger = LogManager.getLogger(XmlUtils.class);
	
	static final String ATTR_CLASS = "clazz";
	static final String TMPFILEFOLDER = "tmpFileFolder";
	static final int BUFFER = 1024;
	
	public static void generateXml(Object obj, String xmlFileName) 
			throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException, SAXException {
		
		logger.debug("开始生成xml");
        
        
        OutputFormat format = new OutputFormat("    ",true);  
        format.setEncoding("UTF-8");//设置编码格式 
        
        File tmpFile = new File(xmlFileName);
    	tmpFile.deleteOnExit();
    	tmpFile.createNewFile();//导出时，创建一个xml文件
    	logger.info("生成临时文件：" + tmpFile.getAbsolutePath());//在控制台打印输出，路径tmpFile.getAbsolutePath()
    	
        FileOutputStream fo = new FileOutputStream(tmpFile);
        XMLWriter xmlWriter = new XMLWriter(fo, format);
        xmlWriter.startDocument();
		String className = obj.getClass().getName();
		Element root = DocumentHelper.createElement(obj.getClass().getSimpleName()).addAttribute(ATTR_CLASS, className);
		xmlWriter.writeOpen(root);
		Class<?> c = obj.getClass(); 
		Field[] fs = getAllField(c); 
		
		if (fs != null && fs.length > 0){
			for(int i=0;i<fs.length;i++){
				Field f = fs[i];
				Class<?> cls = f.getType();
				String name = f.getName();
				if("serialVersionUID".equals(name)){
					continue;
				}
				Element ele = DocumentHelper.createElement(name);
				ele.setParent(root);
				xmlWriter.writeOpen(ele);
				
				Type type = f.getGenericType();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				Method m = c.getMethod("get" + name);
				if(cls.isAssignableFrom(List.class)){
					List<?> li = (List<?>)m.invoke(obj);
					parseListObj(li, type, ele, xmlWriter);
				}else if(cls.isAssignableFrom(String.class)){
					String s = (String)m.invoke(obj);
					xmlWriter.write(s == null ? "" : s);
				}
				xmlWriter.writeClose(ele);
			}
		}
		 
        obj = null;
  
        xmlWriter.writeClose(root);   
        xmlWriter.endDocument();   
        xmlWriter.close();
        
        fo.flush();
        fo.close();
        
//        byte[] byteArr = byteOut.toByteArray();
//        return byteArr;
       
	}
	
	public static byte[] compressFiles(List<File> files) throws IOException{
		logger.debug("开始压缩");
		ByteArrayOutputStream zipByteOut = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(zipByteOut, new CRC32());  
        ZipOutputStream zos = new ZipOutputStream(cos);  
        for(File f: files){
        	ZipEntry entry = new ZipEntry(f.getName());  
	        zos.putNextEntry(entry);  
        	
	        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));  
	        int count;  
	        byte data[] = new byte[BUFFER];  
	        while ((count = bis.read(data, 0, BUFFER)) != -1) {  
	        	zos.write(data, 0, count);  
	        }  
	        bis.close();  
        }
        
        zos.closeEntry();  
        zos.flush();  
        zos.close();  
        
        byte[] zipByteArr = zipByteOut.toByteArray();
        zipByteOut.flush();
        zipByteOut.close();
        logger.debug("完毕");
        return zipByteArr;
	}
	
	public static Object parseXml(File file) throws DocumentException, ClassNotFoundException, 
		InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, 
		IllegalArgumentException, InvocationTargetException{
		ByteArrayInputStream in = null;
		try{
			
			SAXReader reader = new SAXReader();  
//			reader.setDefaultHandler(new ElementHandler(){  
//			    public void onEnd(ElementPath ep) {  
//			        Element e = ep.getCurrent(); //获得当前节点     
//			        e.detach(); //记得从内存中移去   
//			    }  
//			    public void onStart(ElementPath arg0) {  
//			    }  
//			});  
//			Document doc = reader.read(in);
			Document doc = reader.read(file);
	        Element root = doc.getRootElement();   
	        Object obj = null;
	        Attribute attr = root.attribute(ATTR_CLASS);
	        if (attr != null){
	        	String attrName = attr.getValue();
	        	Class<?> clazz = Class.forName(attrName);
	        	obj = clazz.newInstance();
	        	iterateElement(obj, root, null);
	        }
			return obj;
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					in = null;
				}
			}
		}
	}
	//生成一个模板文件件夹，也就是存放上传文件的路径地址
	public static String getTempFilePath() throws IOException{
		String p = XmlUtils.class.getResource("/").getPath();
		File f = new File(p);
		String sp = System.getProperty("file.separator");
		String pf = f.getParent() + sp + TMPFILEFOLDER ;
		File pfile = new File(pf);
		if(!pfile.isDirectory() || !pfile.exists()){//判断是否已在服务器有存放文件夹，如果没有就执行mkdir进行创建
			pfile.mkdir();//创建
		}
		return pfile.getAbsolutePath() + sp;//返回路径
	}
	
	private static void iterateElement(Object obj, Element ele, Object parentObj) throws ClassNotFoundException, 
	InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, 
	InvocationTargetException{
		for (Iterator<Element> i = ele.elementIterator(); i.hasNext(); ) {
    		Element el = (Element) i.next();
    		Attribute attr = el.attribute(ATTR_CLASS);
    		if(attr != null){
    			String attrName = attr.getValue();
    			Class<?> clazz;
				clazz = Class.forName(attrName);
				Object o = clazz.newInstance();
				String parentObjName = el.getParent().getName();
				String fn = parentObjName.substring(0, 1).toUpperCase() + parentObjName.substring(1);
				Method addm = obj.getClass().getMethod("add" + fn, clazz);
				
				Object pObj = obj;
				addm.invoke(parentObj, o);
					iterateElement(o, el, pObj);
    		}else{
    			String name = el.getName();
    			Field f = hasField(obj.getClass(), name);
    			if(f != null){
					Class<?> typeClass = f.getType();
					Object o = null;
					if(typeClass.isAssignableFrom(List.class)){
						o = new ArrayList<Object>();
					}else if(typeClass.isAssignableFrom(String.class)){
						o = el.getText();
					}
					String fn = name.substring(0, 1).toUpperCase() + name.substring(1);
					Method sm = obj.getClass().getMethod("set" + fn, typeClass);
					Method gm = obj.getClass().getMethod("get" + fn);
					Object fieldObj = gm.invoke(obj);
					if(fieldObj == null){
						sm.invoke(obj, o);
					}else{
					}
					Object pObj = obj;
					iterateElement(obj, el, pObj);
    			}
    		}
    	}
	}
	
	private static void parseListObj(List<?> list, Type tp, Element ele, XMLWriter xmlWriter) 
			throws NoSuchMethodException, SecurityException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException, IOException{
		if (list != null && !list.isEmpty()){
			for(Object obj: list){
				String className = obj.getClass().getName();
				
				Element el1 = DocumentHelper.createElement(obj.getClass().getSimpleName()).addAttribute(ATTR_CLASS, className);
				el1.setParent(ele);
				xmlWriter.writeOpen(el1);
				
				Class<?> c = obj.getClass(); 
				Field[] fs = getAllField(c); 
				
				if (fs != null && fs.length > 0){
					for(int i=0;i<fs.length;i++){
						Field f = fs[i];
						Class<?> cls = f.getType();
						String name = f.getName();
						if("serialVersionUID".equals(name)){
							continue;
						}
						Element el2 = DocumentHelper.createElement(name);
						el2.setParent(el1);
						xmlWriter.writeOpen(el2);
						
						Type type = f.getGenericType();
						name = name.substring(0, 1).toUpperCase() + name.substring(1);
						Method m = c.getMethod("get" + name);
						if(cls.isAssignableFrom(List.class)){
							List<?> li = (List<?>)m.invoke(obj);
							parseListObj(li, type, el2, xmlWriter);
						}else if(cls.isAssignableFrom(String.class)){
							String s = (String)m.invoke(obj);
							xmlWriter.write(s == null ? "" : s);
						}else if(cls.isAssignableFrom(String.class)){
						}
						xmlWriter.writeClose(el2);
					}
				}
				xmlWriter.writeClose(el1);
			}
		}else{
			ele.addText("");
		}
	}
	
	private static Field hasField(Class<?> clazz, String fieldName){
		if(fieldName == null){
			return null;
		}
		Field[] fs = getAllField(clazz);
		if(fs != null && fs.length > 0){
			for(int i=0;i<fs.length;i++){
				if(fieldName.equals(fs[i].getName())){
					return fs[i];
				}
			}
			
		}
		return null;
	}
	
	/**
	 * 获取类clazz的所有Field，包括其父类的Field，如果重名，以子类Field为准。
	 * @param clazz
	 * @return Field数组
	 */
	private static Field[] getAllField(Class<?> clazz){
		
		ArrayList<Field> fieldList = new ArrayList<Field>();
		Field[] dFields = clazz.getDeclaredFields();
		if (null != dFields && dFields.length > 0) {
			fieldList.addAll(Arrays.asList(dFields));
		}

		Class<?> superClass = clazz.getSuperclass();
		if (superClass != Object.class) {
			Field[] superFields = getAllField(superClass);
			if (null != superFields && superFields.length > 0) {
				for(Field field:superFields){
					if(!isContain(fieldList, field)){
						fieldList.add(field);
					}
				}
			}
		}
		Field[] result=new Field[fieldList.size()];
		fieldList.toArray(result);
		return result;
	}
	
	/**检测Field List中是否已经包含了目标field
	 * @param fieldList
	 * @param field 带检测field
	 * @return
	 */
	private static boolean isContain(ArrayList<Field> fieldList,Field field){
		for(Field temp:fieldList){
			if(temp.getName().equals(field.getName())){
				return true;
			}
		}
		return false;
	}
	
	//对文件进行解压
	@SuppressWarnings("unchecked")  
    public static List<File> unzip(File zipFile, String unzipFilePath) 
    		throws Exception  
    {  
		List<File> fileList = new ArrayList<File>();

		// 开始解压
		ZipEntry entry = null;
		String entryFilePath = null;
		File entryFile = null;
		int count = 0, bufferSize = BUFFER;
		byte[] buffer = new byte[bufferSize];
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		ZipFile zip = new ZipFile(zipFile);
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
		// 循环对压缩包里的每一个文件进行解压
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			// 构建压缩包中一个文件解压后保存的文件全路径
			entryFilePath = unzipFilePath + File.separator + entry.getName();

			// 创建解压文件
			entryFile = new File(entryFilePath);
			if (entryFile.exists()) {
				// 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
				SecurityManager securityManager = new SecurityManager();
				securityManager.checkDelete(entryFilePath);
				// 删除已存在的目标文件
				entryFile.delete();
			}

			// 写入文件
			bos = new BufferedOutputStream(new FileOutputStream(entryFile));
			bis = new BufferedInputStream(zip.getInputStream(entry));
			while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
				bos.write(buffer, 0, count);
			}
			bos.flush();
			bos.close();
			logger.info("unziped file:" + entryFile.getAbsolutePath());
			fileList.add(entryFile);
		}
		zip.close();
		return fileList;
    }  
}
