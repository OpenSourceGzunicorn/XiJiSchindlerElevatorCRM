/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Wed Mar 08 11:11:28 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.gzunicorn.hibernate.sysmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the OperationRef table. 
 * You can customize the behavior of this class by editing the class, {@link Operationref()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractOperationref 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The simple primary key value. */
    private OperationrefKey id;

    /**
     * Simple constructor of AbstractOperationref instances.
     */
    public AbstractOperationref()
    {
    }

    /**
     * Constructor of AbstractOperationref instances given a composite primary key.
     * @param id
     */
    public AbstractOperationref(OperationrefKey id)
    {
        this.setId(id);
    }

    /**
     * Return the composite id instance that identifies this object.
     * @return OperationrefKey
     */
    public OperationrefKey getId()
    {
        return this.id;
    }

    /**
     * Set the composite id instance that identifies this object.
     * @param id
     */
    public void setId(OperationrefKey id)
    {
        this.hashValue = 0;
        this.id = id;
    }

    /**
     * Implementation of the equals comparison on the basis of equality of the primary key values.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof Operationref))
            return false;
        Operationref that = (Operationref) rhs;
        if (this.getId() == null || that.getId() == null)
            return false;
        return (this.getId().equals(that.getId()));
    }

    /**
     * Implementation of the hashCode method conforming to the Bloch pattern with
     * the exception of array properties (these are very unlikely primary key types).
     * @return int
     */
    public int hashCode()
    {
        if (this.hashValue == 0)
        {
            int result = 17;
            if (this.getId() == null)
            {
                result = super.hashCode();
            }
            else
            {
                result = this.getId().hashCode();
            }
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
