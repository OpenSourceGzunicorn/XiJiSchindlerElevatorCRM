package com.gzunicorn.hibernate.viewmanager;

/**
 * ViewClassUserId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class ViewClassUserId extends AbstractViewClassUserId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ViewClassUserId() {
	}

	/** minimal constructor */
	public ViewClassUserId(String classId, String userId, String userName,
			String typeId) {
		super(classId, userId, userName, typeId);
	}

	/** full constructor */
	public ViewClassUserId(String classId, String className, String userId,
			String userName, String typeId) {
		super(classId, className, userId, userName, typeId);
	}

}
