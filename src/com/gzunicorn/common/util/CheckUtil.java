/*
 * Created on 2005-8-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.util;

import java.text.ParseException;
import java.util.ArrayList;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.ValidatorUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created on 2005-7-12
 * <p>
 * Title: 具体业务数据通用校验方法
 * </p>
 * <p>
 * Description: 提供单态设计模式
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * <p>
 * Company:友联科技
 * </p>
 * 
 * @author wyj
 * @version 1.0
 */
public class CheckUtil {

    private static String errorMsg = "";// 返回校验信息字符串

    private static CheckUtil checkUtil = new CheckUtil();

    private CheckUtil() {
    }

    public static CheckUtil getInstance() {
        return checkUtil;
    }
    
    /**
     * 校验日期是否是条码出入库日期
     * @param barcodeDate	条码出入库日期
     * @return String 成功返回空字符串 ""
     */
    public static String isBarcodeDate(String barcodeDate) {
        
        boolean returnValue = true;
       
        synchronized (errorMsg) {
        if(returnValue){
            //先判断日期的通用格式
            if(!ValidatorUtil.isDate(barcodeDate)){
                setErrorMsg(barcodeDate + "不合法的日期格式！");
                returnValue = false;
            }
        }
        
        if(returnValue){
            try{
                String strDate =  
                    CommonUtil.dateToStr(CommonUtil.strToDate(barcodeDate, ""), "");
                //判断日期范围
                if(Integer.parseInt(strDate.substring(0,4))<2005 || Integer.parseInt(strDate.substring(0,4))>2050){
                    setErrorMsg(barcodeDate + "日期年份范围不合法！");
                    returnValue = false;
                }    
            }catch(ParseException e){
                setErrorMsg(barcodeDate + "为不合法的日期格式！");
                returnValue = false;
            }
        }
        
        if (returnValue) {
            setErrorMsg("");
        } else {
            DebugUtil.println(getErrorMsg());
        }
        return getErrorMsg();
        }// synchronized
    }


    /**
     * 单机代码合法校验
     * 
     * @param procID
     *            单机代码
     * @param procType
     *            单机类型，%表示所有类型
     * @return String 成功返回空字符串
     */
    public static String isProcInfo(String procID, String procType) {

        boolean returnValue = true;
        Session session = null;
        ArrayList list = null;

        synchronized (errorMsg) {

            // 校验长度
            if (returnValue) {
                if (procID == null || procID.length() == 0 ) {
                    setErrorMsg(procID + "机型代码不能为空！");
                    returnValue = false;
                }
            }

            if (returnValue) {
                try {
                    // 判断店铺编码在店铺表中是否存
                    session = HibernateUtil.getSession();
                    Query query = session
                    .createQuery("FROM Procinfo as a WHERE a.procid=:procid AND a.proctype like :proctype");
                    query.setString("procid", procID);
                    query.setString("proctype", procType);
                    list = (ArrayList) query.list();
                    if (list == null || list.size() == 0) {
                        setErrorMsg(procID + "机型代码不存在！");
                        returnValue = false;
                    }

                } catch (DataStoreException dex) {
                    DebugUtil.print(dex, "HibernateUtil：Hibernate 连接 出错！");
                    setErrorMsg("HibernateUtil：Hibernate 连接 出错！");
                    returnValue = false;
                } catch (HibernateException hex) {
                    DebugUtil.print(hex,
                            "HibernateUtil：Hibernate Session 打开出错！");
                    setErrorMsg("HibernateUtil：Hibernate Session 打开出错！");
                    returnValue = false;
                } finally {
                    try {
                        session.close();
                    } catch (HibernateException hex) {
                        DebugUtil.print(hex,
                                "HibernateUtil：Hibernate Session 关闭出错！");
                    }
                }
            }
            if (returnValue) {
                setErrorMsg("");
            } else {
                DebugUtil.println(getErrorMsg());
            }
            return getErrorMsg();
        }// synchronized
        
    }

    /**
     * 数量合法校验
     * 
     * @param barcode
     * @return String 成功返回空字符串
     */
    public static String isQTY(String qty) {

        boolean returnValue = true;
        // errorMsg资源同步，防止多个线程同时访问errorMsg造成影响
        synchronized (errorMsg) {

            if (returnValue) {
                if (!ValidatorUtil.isAllDigits(qty)) {
                    setErrorMsg(qty + "是不合法的数量类型！");
                    returnValue = false;
                }
            }

            if (returnValue) {
                // 校验成功，errorMsg为""
                setErrorMsg("");
            } else {
                DebugUtil.println(getErrorMsg());
            }
            return getErrorMsg();
        }// synchronized
    }
    
    /**
     * 检测userID是否有权限管shopID店铺
     * @param userID	用户代码
     * @param shopID	店铺代码
     * @return String 成功返回空字符串 ""
     */
    public static String isUserShopRef(String userID, String shopID) {
        
        boolean returnValue = true;
        Session session = null;
        ArrayList list = null;
        
        synchronized (errorMsg) {
        
        
        if (returnValue) {
            try {
                // 判断店铺之间的所属关系
                session = HibernateUtil.getSession();
                Query query = session
                        .createQuery("FROM Viewuserrefshop as a WHERE a.id.shopid=:shopid AND a.id.userid = :userid");
                query.setString("shopid", shopID);
                query.setString("userid", userID);

                list = (ArrayList) query.list();
                if (list == null || list.isEmpty() || list.size() == 0) {
                    setErrorMsg("当前用户没有操作" + shopID + "店铺的权限");
                    returnValue = false;
                }

            } catch (DataStoreException dex) {
                DebugUtil.print(dex, "HibernateUtil：Hibernate 连接 出错！");
                setErrorMsg("HibernateUtil：Hibernate 连接 出错！");
                returnValue = false;
            } catch (HibernateException hex) {
                DebugUtil.print(hex,
                        "HibernateUtil：Hibernate Session 打开出错！");
                setErrorMsg("HibernateUtil：Hibernate Session 打开出错！");
                returnValue = false;
            } finally {
                try {
                    session.close();
                } catch (HibernateException hex) {
                    DebugUtil.print(hex,
                            "HibernateUtil：Hibernate Session 关闭出错！");
                }
            }
        }
        
        if (returnValue) {
            setErrorMsg("");
        } else {
            DebugUtil.println(getErrorMsg());
        }
        return getErrorMsg();
        }// synchronized
    }
    /**
     * 校验仓库编号是否存在
     * @param storeID	仓库编号
     * @return String 成功返回空字符串 ""
     */
    public static String isStoreID(String storeID) {
        
        boolean returnValue = true;
        Session session = null;
        ArrayList list = null;
       
        synchronized (errorMsg) {
        	 if (returnValue) {
                if (storeID == null || storeID.length() == 0 ) {
                    setErrorMsg(storeID + "仓库代码不能为空！");
                    returnValue = false;
                }
            }

            if (returnValue) {
                try {
                    // 判断店铺编码在店铺表中是否存
                    session = HibernateUtil.getSession();
                    Query query = session
                    .createQuery("FROM Store as a WHERE a.storeid=:storeid");
                    query.setString("storeid", storeID);

                    list = (ArrayList) query.list();
                    if (list == null || list.size() == 0) {
                        setErrorMsg(storeID + "仓库代码不存在！");
                        returnValue = false;
                    }

                } catch (DataStoreException dex) {
                    DebugUtil.print(dex, "HibernateUtil：Hibernate 连接 出错！");
                    setErrorMsg("HibernateUtil：Hibernate 连接 出错！");
                    returnValue = false;
                } catch (HibernateException hex) {
                    DebugUtil.print(hex,
                            "HibernateUtil：Hibernate Session 打开出错！");
                    setErrorMsg("HibernateUtil：Hibernate Session 打开出错！");
                    returnValue = false;
                } finally {
                    try {
                        session.close();
                    } catch (HibernateException hex) {
                        DebugUtil.print(hex,
                                "HibernateUtil：Hibernate Session 关闭出错！");
                    }
                }
            }
            if (returnValue) {
                setErrorMsg("");
            } else {
                DebugUtil.println(getErrorMsg());
            }
            return getErrorMsg();
        }// synchronized
    }

    
    /**
     * @return Returns the errorMsg.
     */
    public static String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg
     *            The errorMsg to set.
     */
    public static void setErrorMsg(String errorMsg) {
        CheckUtil.errorMsg = errorMsg;
    }
}
