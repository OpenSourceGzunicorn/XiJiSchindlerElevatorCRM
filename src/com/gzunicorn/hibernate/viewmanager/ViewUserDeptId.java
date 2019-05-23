package com.gzunicorn.hibernate.viewmanager;

/**
 * ViewUserDeptId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class ViewUserDeptId extends AbstractViewUserDeptId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ViewUserDeptId() {
	}

	/** full constructor */
	public ViewUserDeptId(String userId, String deptId, String flag) {
		super(userId, deptId, flag);
	}

}
