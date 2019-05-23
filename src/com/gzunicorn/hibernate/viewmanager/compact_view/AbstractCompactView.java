package com.gzunicorn.hibernate.viewmanager.compact_view;

/**
 * AbstractCompactView entity provides the base persistence definition of the
 * CompactView entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCompactView implements java.io.Serializable {

	// Fields

	private CompactViewId id;

	// Constructors

	/** default constructor */
	public AbstractCompactView() {
	}

	/** full constructor */
	public AbstractCompactView(CompactViewId id) {
		this.id = id;
	}

	// Property accessors

	public CompactViewId getId() {
		return this.id;
	}

	public void setId(CompactViewId id) {
		this.id = id;
	}

}