package com.gzunicorn.bo;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;


import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.util.ArrayConfig;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;


public class XmlBO {
	/**
	 * 配置功能角色的XML
	 * @param list   结果集 
	 * @throws IOException
	 */
	public String ToRoleXml(List list){
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			sb.append("<?xml version='1.0' encoding='gbk'?>");
			sb.append("<dataset>");
			int len=list.size();
			for(int i=0;i<len;i++){
				HashMap mp=(HashMap)list.get(i);
				sb.append("<row>");
				sb.append("<roleid>").append((String)mp.get("roleid")).append("</roleid>");
				sb.append("<rolename>").append((String)mp.get("rolename")).append("</rolename>");
				sb.append("</row>");
			}
			sb.append("</dataset>");
		}
		return sb.toString();
	}
	/**
	 * 配置数据角色的XML
	 * @param list   结果集 
	 * @throws IOException
	 */
	public String ToDataRoleXml(List list){
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			sb.append("<?xml version='1.0' encoding='gbk'?>");
			sb.append("<dataset>");
			int len=list.size();
			for(int i=0;i<len;i++){
				HashMap mp=(HashMap)list.get(i);
				sb.append("<row>");
				sb.append("<dataroleid>").append((String)mp.get("dataroleid")).append("</dataroleid>");
				sb.append("<datarolename>").append((String)mp.get("datarolename")).append("</datarolename>");
				sb.append("</row>");
			}
			sb.append("</dataset>");
		}
		return sb.toString();
	}
	/**
	 * 配置地区XML
	 * @param list   结果集 
	 * @throws IOException
	 */
	public String ToSaleAreaXml(List list,int count){
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			sb.append("<?xml version='1.0' encoding='gbk'?>");
			sb.append("<dataset>");
			sb.append("<totalCount>").append(count).append("</totalCount>");
			int len=list.size();
			for(int i=0;i<len;i++){
				HashMap mp=(HashMap)list.get(i);
				sb.append("<row>");
				sb.append("<areaid>").append((String)mp.get("areaid")).append("</areaid>");
				sb.append("<areaname>").append((String)mp.get("areaname")).append("</areaname>");
				sb.append("<enabledflag>").append((String)mp.get("enabledflag")).append("</enabledflag>");
				sb.append("<rem1>").append((String)mp.get("rem1")).append("</rem1>");
				sb.append("</row>");
			}
			sb.append("</dataset>");
		}
		return sb.toString();
	}
	/**
	 * 配置用户基础数据的XML
	 * @param list   结果集 
	 * @throws IOException
	 */
	public String ToLoginUserXml(List list,int count){
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			sb.append("<?xml version='1.0' encoding='gbk'?>");
			sb.append("<dataset>");
			int len=list.size();
			for(int i=0;i<len;i++){
				HashMap mp=(HashMap)list.get(i);
				sb.append("<row>");
				sb.append("<userid>").append((String)mp.get("userid")).append("</userid>");
				sb.append("<username>").append((String)mp.get("username")).append("</username>");
				sb.append("<roleid>").append((String)mp.get("roleid")).append("</roleid>");
				sb.append("<dataroleid>").append((String)mp.get("dataroleid")).append("</dataroleid>");
				sb.append("<enabledflag>").append((String)mp.get("enabledflag")).append("</enabledflag>");
				sb.append("<loginflag>").append((String)mp.get("loginflag")).append("</loginflag>");
				sb.append("</row>");
			}
			sb.append("</dataset>");
		}
		return sb.toString();
	}
	/**
	 * 配置用户基础数据的XML
	 * @param list   结果集 
	 * @throws IOException
	 */
	public String ToUpdateLoginUserXml(List list){
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			sb.append("<?xml version='1.0' encoding='gbk'?>");
			sb.append("<dataset>");
			int len=list.size();
			for(int i=0;i<len;i++){
				HashMap mp=(HashMap)list.get(i);
				sb.append("<row>");
				sb.append("<useridUp>").append((String)mp.get("userid")).append("</useridUp>");
				sb.append("<usernameUp>").append((String)mp.get("username")).append("</usernameUp>");
				sb.append("<passwdUp>").append((String)mp.get("passwd")).append("</passwdUp>");
				sb.append("<roleidUp>").append((String)mp.get("roleid")).append("</roleidUp>");
				sb.append("<dataroleidUp>").append((String)mp.get("dataroleid")).append("</dataroleidUp>");
				sb.append("<enabledflagUp>").append((String)mp.get("enabledflag")).append("</enabledflagUp>");
				sb.append("<loginflagUp>").append((String)mp.get("loginflag")).append("</loginflagUp>");
				sb.append("<rem1Up>").append((String)mp.get("rem1")).append("</rem1Up>");
				sb.append("</row>");
			}
			sb.append("</dataset>");
		}
		return sb.toString();
	}
	/**
	 * 配置待办工作列表的XML
	 * @param list   结果集 
	 * @throws IOException
	 */
	public String ToMyTaskOAXml(List list){
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			sb.append("<?xml version='1.0' encoding='gbk'?>");
			sb.append("<dataset>");
			int len=list.size();
			for(int i=0;i<len;i++){
				HashMap mp=(HashMap)list.get(i);
				sb.append("<row>");
				sb.append("<taskid>").append((String)mp.get("taskid")).append("</taskid>");
				sb.append("<taskname>").append((String)mp.get("taskname")).append("</taskname>");
				sb.append("<taskurl>").append((String)mp.get("taskurl")).append("</taskurl>");
				sb.append("<createdate>").append((String)mp.get("createdate")).append("</createdate>");
				sb.append("<des>").append((String)mp.get("des")).append("</des>");
				sb.append("<flowname>").append((String)mp.get("flowname")).append("</flowname>");
				sb.append("<tasktype>").append((String)mp.get("tasktype")).append("</tasktype>");
				sb.append("<hiddentasktype>").append((String)mp.get("hiddentasktype")).append("</hiddentasktype>");
				sb.append("<taskprocs>").append((String)mp.get("taskprocs")).append("</taskprocs>");
				sb.append("<taskprocurl>").append((String)mp.get("taskprocurl")).append("</taskprocurl>");
				sb.append("<hiddentasktarget>").append((String)mp.get("hiddentasktarget")).append("</hiddentasktarget>");
				sb.append("</row>");
			}
			sb.append("</dataset>");
		}
		return sb.toString();
	}
}