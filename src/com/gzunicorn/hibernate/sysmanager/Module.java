/*
 * Created Fri Oct 21 13:29:41 CST 2005 by MyEclipse Hibernate Tool.
 */ 
package com.gzunicorn.hibernate.sysmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'module' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Module
    extends AbstractModule
    implements Serializable
{
    /**
     * Simple constructor of Module instances.
     */
    public Module()
    {
    }

    /**
     * Constructor of Module instances given a simple primary key.
     * @param moduleid
     */
    public Module(java.lang.String moduleid)
    {
        super(moduleid);
    }

    /* Add customized code below */

}
