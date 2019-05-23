/*
 * Created Fri Oct 21 16:34:40 CST 2005 by MyEclipse Hibernate Tool.
 */ 
package com.gzunicorn.hibernate.sysmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'company' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Company
    extends AbstractCompany
    implements Serializable
{
    /**
     * Simple constructor of Company instances.
     */
    public Company()
    {
    }

    /**
     * Constructor of Company instances given a simple primary key.
     * @param companyid
     */
    public Company(java.lang.String companyid)
    {
        super(companyid);
    }

    /* Add customized code below */

}
