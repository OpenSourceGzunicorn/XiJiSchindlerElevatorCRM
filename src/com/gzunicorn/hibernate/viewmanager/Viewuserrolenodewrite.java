/*
 * Created Wed Oct 19 18:04:34 CST 2005 by MyEclipse Hibernate Tool.
 */ 
package com.gzunicorn.hibernate.viewmanager;

import java.io.Serializable;

/**
 * A class that represents a row in the 'ViewUserRoleNodeWrite' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Viewuserrolenodewrite
    extends AbstractViewuserrolenodewrite
    implements Serializable
{
    /**
     * Simple constructor of Viewuserrolenodewrite instances.
     */
    public Viewuserrolenodewrite()
    {
    }

/* Add customized code below */
    
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Viewuserrolenodewrite)) return false;
        	
        	final Viewuserrolenodewrite other = (Viewuserrolenodewrite)o;
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
