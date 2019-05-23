
package com.gzunicorn.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.cfg.*;

import com.gzunicorn.common.util.SysConfig;

/**
 * Created on 2005-7-12
 * <p>Title: Hibernate的Session公共调用</p>
 * <p>Description:	</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 */

public class HibernateUtil {

    private static Log log = LogFactory.getLog(HibernateUtil.class);
    private static Configuration configuration;
    private	static SessionFactory sessionFactory;
    
    //初始化Hibernate,创建SessionFactory实例
    static{
        try{
            configuration = new Configuration().configure(SysConfig.HIBERNATE_CFG_FILENAME);
            sessionFactory = configuration.buildSessionFactory();
        }catch(Throwable ex){
          log.error(ex.getMessage());
          throw new ExceptionInInitializerError(ex);
        }
     }
    
    
    /**
     * 获得Hibernate的SessionFactory工厂
     * @return
     */
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
    
    /**
     * 获得Hibernate的Configuration配置
     * @return
     */
    public static Configuration getConfiguration(){
        return configuration;
    }
    
    /**
     * 根据当前配置重新生成Hibernate的SessionFactory工厂
     * @throws DataStoreException
     */
    public static void rebuildSessionFactory() 
    	throws DataStoreException{
        synchronized(sessionFactory){
            try{
                sessionFactory = getConfiguration().buildSessionFactory();
            }catch(Exception ex){
                log.error(ex.getMessage());
                throw new DataStoreException(ex);
            }
        }
    }
    
    /**
     * 根据新配置重新生成Hibernate的SessionFactory工厂
     * @param cfg
     * @throws DataStoreException
     */
     public static void rebuildSessionFactory(Configuration cfg)
     	throws DataStoreException{
         synchronized(sessionFactory){
             try{
                 sessionFactory = cfg.buildSessionFactory();
                 configuration = cfg;
             }catch(Exception ex){
                 log.error(ex.getMessage());
                 throw new DataStoreException(ex);
             }
         }
     }
     
     /**
      * 获取一个Hibernate的Session
      * @return
      * @throws DataStoreException
      */
     public static Session getSession() throws DataStoreException{
         try{
             return sessionFactory.openSession();
         }catch(Exception ex){
             log.error(ex.getMessage());
             throw new DataStoreException(ex);
         }
     }
     
     /**
      * 关闭sessionFactory工厂
      * 注：不要随便调用
      */
     public static void close(){
         try{
             sessionFactory.close();
         }catch(Exception ex){
             log.error(ex.getMessage());
         }
     }

     /**
      * 关闭连接Session
      * @param session
      * @throws DataStoreException
      */
     public static void closeSession(Session session) 
     	throws DataStoreException{
         try{
             session.close();
         }catch(Exception ex){
             log.error(ex.getMessage());
             throw new DataStoreException(ex);
         }
     }

}
