/*
 * Created on 2005-8-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.taglib.roleshow;

/**
 * Created on 2005-7-12
 * <p>Title:	角色模块权限设置</p>
 * <p>Description:	角色模块树异常类</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 */
public class RoleShowException extends Exception
{

    public RoleShowException()
    {
    }

    public RoleShowException(String msg)
    {
        super(msg);
    }

    public RoleShowException(Throwable cause)
    {
        super(cause);
    }

    public RoleShowException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}