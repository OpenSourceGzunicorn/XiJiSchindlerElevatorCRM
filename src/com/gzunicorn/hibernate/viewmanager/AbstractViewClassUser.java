package com.gzunicorn.hibernate.viewmanager;

/**
 * AbstractViewClassUser entity provides the base persistence definition of the
 * ViewClassUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractViewClassUser implements java.io.Serializable {

	// Fields

	private ViewClassUserId id;

	// Constructors

	/** default constructor */
	public AbstractViewClassUser() {
	}

	/** full constructor */
	public AbstractViewClassUser(ViewClassUserId id) {
		this.id = id;
	}

	// Property accessors

	public ViewClassUserId getId() {
		return this.id;
	}

	public void setId(ViewClassUserId id) {
		this.id = id;
	}

}