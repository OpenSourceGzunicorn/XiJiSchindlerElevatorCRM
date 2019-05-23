package com.gzunicorn.bean;

import java.io.Serializable;

/* **
 * 
 * Created on 2005-11-21
 * <p>Title: </p>
 * <p>Description: 用户登录Session Bean,保存有用户的SessionID</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:广州友联</p>
 * @author wyj
 * @version	1.0
 */
public class UserInfoBean implements Serializable {

	
	private String sessionID;//会话ID

	private String userID;//用户ID

	private String userName;//用户名

	private String roleName; //角色

	private String storageName;//所属部门
	
	private String comName; //所属分部

	private	String loginDate;//登录日期
	
	private String loginTime;////登录时间
	
	private String ipAddress;//IP地址
	
	
	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
		
	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

}
