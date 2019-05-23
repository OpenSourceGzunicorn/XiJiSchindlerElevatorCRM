/*
 * Created Mon Oct 24 13:29:01 CST 2005 by MyEclipse Hibernate Tool.
 */ 
package com.gzunicorn.hibernate.sysmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'role' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Role
    extends AbstractRole
    implements Serializable
{
    /**
     * Simple constructor of Role instances.
     */
    public Role()
    {
    }

    /**
     * Constructor of Role instances given a simple primary key.
     * @param roleid
     */
    public Role(java.lang.String roleid)
    {
        super(roleid);
    }

    /* Add customized code below */

}
