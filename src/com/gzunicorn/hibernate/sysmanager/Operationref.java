/*
 * Created Wed Nov 30 17:35:14 CST 2005 by MyEclipse Hibernate Tool.
 */ 
package com.gzunicorn.hibernate.sysmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'OperationRef' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Operationref
    extends AbstractOperationref
    implements Serializable
{
    /**
     * Simple constructor of Operationref instances.
     */
    public Operationref()
    {
    }

    /**
     * Constructor of Operationref instances given a composite primary key.
     * @param id
     */
    public Operationref(OperationrefKey id)
    {
        super(id);
    }

    /* Add customized code below */

}
