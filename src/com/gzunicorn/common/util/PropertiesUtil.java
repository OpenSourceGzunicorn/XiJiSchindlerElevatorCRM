package com.gzunicorn.common.util;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;

/*
 * Created on 2005-7-12
 * <p>Title:读取Properties属性文件程序	</p>
 * <p>Description:	</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 */

public class PropertiesUtil {

    private final static String FILENAME = SysConfig.CONFIG_FILENAME;
    private String FILEPATH = "";  //properties文件保存的绝对位置

    private Properties properties = null;
    private static PropertiesUtil instance = null;

    private PropertiesUtil(){
      try{
        properties = new Properties();
        this.FILEPATH = this.getClass().getClassLoader().getResource(FILENAME).getFile();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(FILENAME);
        //InputStream in = new FileInputStream(FILENAME);
        properties.load(in);
        in.close();
      }catch(IOException e){
        System.err.println("load Config.properties error!");
        e.printStackTrace();
      }
    }

    private static void init(){
      if(instance == null){
        instance = new PropertiesUtil();
      }
    }
    /**
     * 得到参数
     * @param name String 参数名称
     * @return String
     */
    public static String getProperty(String name){
      init();
      return instance.properties.getProperty(name);
    }

    /**
     * 将配置信息写入properties文件
     * @param name String
     * @param value String
     * @param description String 当前写入配置属性的描述信息
     */
    public static void setProperty(String name,String value,String description){
      if(name == null || name.trim().equals("")){
        DebugUtil.println("PropertiesUtil.java setProperty() 参数为空!");
        return;
      }
      init();
      try{
        instance.properties.put(name, value);
        instance.properties.store(new FileOutputStream(instance.FILEPATH), description);
      }catch(IOException ioe){
        DebugUtil.println("PropertiesUtil.java setProperty() 保存properties配置文件失败!");
        ioe.printStackTrace();
      }
    }
    /**
     * 获得properties文件的绝对路径
     * @return String
     */
    public static String getFileAbsolutePath(){
      return instance.FILEPATH;
    }
  /*
    public static void main(String args[]){
      PropertiesUtil.setProperty("hq","11","user");
    }
  */
}
