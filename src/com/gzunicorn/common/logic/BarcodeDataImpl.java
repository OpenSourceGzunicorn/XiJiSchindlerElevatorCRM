/*
 * Created on 2005-9-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.logic;

import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BarcodeDataImpl {
	Log log = LogFactory.getLog(BarcodeDataImpl.class);
	
	
	/**
	 * 
	 * @return String
	 */
	public int getBalanceMaxTimes(String shopid,String balancemonth) {
		Session session = null;
		int  result = 0;

		try {
			session = HibernateUtil.getSession();
			Query query;
			query = session
					.createQuery("select max(a.balancetime) from Balancemaster a where a.shopid = '" + shopid + "' and balancemonth = '" + balancemonth + "'");
			
			java.util.List list = query.list();
			if(list != null && list.get(0) != null)
			{
				result = ((Integer)list.get(0)).intValue();
			}

		} catch (DataStoreException dex) {
			log.error(dex.getMessage());
			DebugUtil.print(dex,
					"HibernateUtil?1?7?1?7Hibernate l?1?7?1?7 ?1?7?1?7?");
		} catch (HibernateException hex) {
			log.error(hex.getMessage());
			DebugUtil.print(hex,
					"HibernateUtil?1?7?1?7Hibernate Session ?1?7?1?9?");
		} finally {
			try {
				session.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil?1?7?1?7Hibernate Session ");
			}
		}
		return result;

	}

}
