/*
 * Created Tue Jan 22 17:53:40 CST 2008 by MyEclipse Hibernate Tool.
 */ 
package com.gzunicorn.hibernate.sysmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'WsRoleConfig' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Wsroleconfig
    extends AbstractWsroleconfig
    implements Serializable
{
    /**
     * Simple constructor of Wsroleconfig instances.
     */
    public Wsroleconfig()
    {
    }

    /**
     * Constructor of Wsroleconfig instances given a composite primary key.
     * @param id
     */
    public Wsroleconfig(WsroleconfigKey id)
    {
        super(id);
    }

    /* Add customized code below */

}
