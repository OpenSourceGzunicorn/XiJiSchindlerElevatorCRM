/*
 * Created on 2005-8-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.dao;

import com.gzunicorn.common.dataimport.ImportInterface;
import com.gzunicorn.common.util.DebugUtil;
/**
 * importinterface　的真正实现的对象工厂
 * @author rr
 *
 */
public class ObjectAchieveFactory {
	public static String MssqlImp="com.gzunicorn.common.dao.mssql.DataOperation";
	
    private ObjectAchieveFactory() { 
    }


    
    public static synchronized DBInterface getDBInterface(String dbClass){
    	DBInterface dao = null;
        String className = "";
    try {
      className = dbClass;
      dao = (DBInterface)Class.forName(className).newInstance() ;
    } catch (ClassNotFoundException e) {
        DebugUtil.println("ImportObjectAchieve.getDAO() 没有相应 Class ! ClassNotFoundException className :" + className);
    }catch (InstantiationException e) {
        DebugUtil.println("ImportObjectAchieve.getDAO() 实例化DAO Class 失败! InstantiationException className :" + className);
    }catch (IllegalAccessException e) {
        DebugUtil.println("ImportObjectAchieve.getDAO() 实例化DAO Class 失败! IllegalAccessException className :" + className);
    }
    return dao;
   }
    /**
     * 返回一个对象实例
     * @param objClass
     * @return
     */
    public static Object getObject(String objClass){
    	Object dao = null;
        String className = "";
    try {
      className = objClass;
      dao = Class.forName(className).newInstance() ;
    } catch (ClassNotFoundException e) {
        DebugUtil.println("ImportObjectAchieve.getDAO() 没有相应 Class ! ClassNotFoundException className :" + className);
    }catch (InstantiationException e) {
        DebugUtil.println("ImportObjectAchieve.getDAO() 实例化DAO Class 失败! InstantiationException className :" + className);
    }catch (IllegalAccessException e) {
        DebugUtil.println("ImportObjectAchieve.getDAO() 实例化DAO Class 失败! IllegalAccessException className :" + className);
    }
    return dao;
   }
    
    /**
     * 取导入接口对象
     * @param importClass
     * @return
     */
    public static synchronized ImportInterface getImportInterface(String importClass){
    	ImportInterface dao = null;
        String className = "";
    try {
      className = importClass;
      dao = (ImportInterface)Class.forName(className).newInstance() ;
    } catch (ClassNotFoundException e) {
        DebugUtil.println("ImportObjectAchieve.getDAO() 没有相应 Class ! ClassNotFoundException className :" + className);
    }catch (InstantiationException e) {
        DebugUtil.println("ImportObjectAchieve.getDAO() 实例化DAO Class 失败! InstantiationException className :" + className);
    }catch (IllegalAccessException e) {
        DebugUtil.println("ImportObjectAchieve.getDAO() 实例化DAO Class 失败! IllegalAccessException className :" + className);
    }
    return dao;
   }
}
