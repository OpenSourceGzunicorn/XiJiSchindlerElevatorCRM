/*
 * Created on 2005-8-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.bean;

/**
 * Created on 2005-7-12
 * <p>
 * Title: 角色模块权限
 * </p>
 * <p>
 * Description: 角色模块权限Bean
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
import java.io.Serializable;
import org.apache.struts.action.ActionForm;

public class RoleNodeBean implements Serializable {

    private String roleID;

    private String roleName;

    private String moduleID;

    private String moduleName;

    private String nodeID;

    private String nodeName;

    private String writeFlag = "A"; //不可读标志

    private String nodeURL;
    /**
     * @return Returns the nodeURL.
     */
    public String getNodeURL() {
        return nodeURL;
    }
    /**
     * @param nodeURL The nodeURL to set.
     */
    public void setNodeURL(String nodeURL) {
        this.nodeURL = nodeURL;
    }

    
    /**
     * @return Returns the nodeName.
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName
     *            The nodeName to set.
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @return Returns the nodeID.
     */
    public String getNodeID() {
        return nodeID;
    }

    /**
     * @param nodeID
     *            The nodeID to set.
     */
    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    /**
     * @return Returns the writeFlag.
     */
    public String getWriteFlag() {
        return writeFlag;
    }

    /**
     * @param writeFlag
     *            The writeFlag to set.
     */
    public void setWriteFlag(String writeFlag) {
        this.writeFlag = writeFlag;
    }

    /**
     * 空构造方法
     *
     */
    public RoleNodeBean() {
        
    }
    /**
     * 构造方法
     * 
     * @param nodeID
     * @param nodeName
     * @param nodeURL
     */
    public RoleNodeBean(String nodeID, String nodeName, String nodeURL) {
        this.nodeID = nodeID;
        this.nodeName = nodeName;
        this.nodeURL = nodeURL;
    }

    /**
     * 构造方法
     * 
     * @param roleID
     * @param roleName
     * @param moduleID
     * @param moduleName
     */
    public RoleNodeBean(String roleID, String roleName, String moduleID,
            String moduleName) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.moduleID = moduleID;
        this.moduleName = moduleName;
    }
    /**
     * 构造方法
     * @param nodeID
     * @param writeFlag
     */
    public RoleNodeBean(String nodeID, String writeFlag) {
        this.nodeID = nodeID;
        this.writeFlag = writeFlag;
    }

    
    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
