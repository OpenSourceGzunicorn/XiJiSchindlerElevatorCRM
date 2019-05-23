/*
 * Created Sun Aug 21 18:44:54 CST 2005 by MyEclipse Hibernate Tool.
 */
package com.gzunicorn.hibernate.sysmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'RoleNode' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Rolenode
    extends AbstractRolenode
    implements Serializable
{
    /**
     * Simple constructor of Rolenode instances.
     */
    public Rolenode()
    {
    }

    /* Add customized code below */
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Rolenode)) return false;
        	
        	final Rolenode other = (Rolenode)o;
        	if(this.getRoleid().equals(other.getRoleid())) return false;
        	if(this.getNodeid().equals(other.getNodeid())) return false;
        	
        	return true;
    }
    
    public int hashCode(){
        int result;
        result = this.getRoleid()==null?0:this.getRoleid().hashCode();
        result = 29 * result + this.getNodeid()==null?0:this.getNodeid().hashCode();
        return result;
    }
}
