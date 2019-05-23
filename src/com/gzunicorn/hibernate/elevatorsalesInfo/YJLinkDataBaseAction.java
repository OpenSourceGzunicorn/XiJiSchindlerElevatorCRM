package com.gzunicorn.hibernate.elevatorsalesInfo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * 连接遥监系统中间表数据库
 * 
 * @author lijun
 * @date 2015-04-10
 * 
 */
public class YJLinkDataBaseAction {
	
	public static Session getSession() {
		Session session = null;

		String url = "/com/gzunicorn/hibernate/elevatorsalesInfo/hibernate.cfg.xml";
		Configuration config = null;
		try {
			
			config = new Configuration().configure(url);
			SessionFactory sessionFactory = config.buildSessionFactory();
			session = sessionFactory.openSession();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return session;
	}

}
