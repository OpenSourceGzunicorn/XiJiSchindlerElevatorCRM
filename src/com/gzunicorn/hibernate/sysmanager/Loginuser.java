/*
 * Created Sat Nov 05 17:04:28 CST 2005 by MyEclipse Hibernate Tool.
 */ 
package com.gzunicorn.hibernate.sysmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'LoginUser' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Loginuser
    extends AbstractLoginuser
    implements Serializable
{
    /**
     * Simple constructor of Loginuser instances.
     */
    public Loginuser()
    {
    }

    /**l
     * Constructor of Loginuser instances given a simple primary key.
     * @param userid
     */
    public Loginuser(java.lang.String userid)
    {
        super(userid);
    }

    /* Add customized code below */

}
