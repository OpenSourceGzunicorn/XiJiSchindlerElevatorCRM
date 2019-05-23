/*
 * Created Fri Oct 21 13:27:12 CST 2005 by MyEclipse Hibernate Tool.
 */ 
package com.gzunicorn.hibernate.viewmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'ViewUserRoleNode' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Viewuserrolenode
    extends AbstractViewuserrolenode
    implements Serializable
{
    /**
     * Simple constructor of Viewuserrolenode instances.
     */
    public Viewuserrolenode()
    {
    }

/* Add customized code below */
    
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Viewuserrolenode)) return false;
        	
        	final Viewuserrolenode other = (Viewuserrolenode)o;
        	if(this.getUserid().equals(other.getUserid())) return false;
        	if(this.getNodeid().equals(other.getNodeid())) return false;
        	
        	return true;
    }
    
    public int hashCode(){
        int result;
        result = this.getUserid()==null?0:this.getUserid().hashCode();
        result = 29 * result + this.getNodeid()==null?0:this.getNodeid().hashCode();
        return result;
    }

}
