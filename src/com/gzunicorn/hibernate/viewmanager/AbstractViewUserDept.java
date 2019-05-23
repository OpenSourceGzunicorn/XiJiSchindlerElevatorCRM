package com.gzunicorn.hibernate.viewmanager;

/**
 * AbstractViewUserDept entity provides the base persistence definition of the
 * ViewUserDept entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractViewUserDept implements java.io.Serializable {

	// Fields

	private ViewUserDeptId id;

	// Constructors

	/** default constructor */
	public AbstractViewUserDept() {
	}

	/** full constructor */
	public AbstractViewUserDept(ViewUserDeptId id) {
		this.id = id;
	}

	// Property accessors

	public ViewUserDeptId getId() {
		return this.id;
	}

	public void setId(ViewUserDeptId id) {
		this.id = id;
	}

}