/*
 * Created Fri Oct 21 13:28:59 CST 2005 by MyEclipse Hibernate Tool.
 */ 
package com.gzunicorn.hibernate.sysmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'GetNum' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Getnum
    extends AbstractGetnum
    implements Serializable
{
    /**
     * Simple constructor of Getnum instances.
     */
    public Getnum()
    {
    }

    /**
     * Constructor of Getnum instances given a composite primary key.
     * @param id
     */
    public Getnum(GetnumKey id)
    {
        super(id);
    }

    /* Add customized code below */

}
