package com.gzunicorn.hibernate.viewmanager;

/**
 * AbstractViewClassUserId entity provides the base persistence definition of
 * the ViewClassUserId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractViewClassUserId implements java.io.Serializable {

	// Fields

	private String classId;
	private String className;
	private String userId;
	private String userName;
	private String typeId;

	// Constructors

	/** default constructor */
	public AbstractViewClassUserId() {
	}

	/** minimal constructor */
	public AbstractViewClassUserId(String classId, String userId,
			String userName, String typeId) {
		this.classId = classId;
		this.userId = userId;
		this.userName = userName;
		this.typeId = typeId;
	}

	/** full constructor */
	public AbstractViewClassUserId(String classId, String className,
			String userId, String userName, String typeId) {
		this.classId = classId;
		this.className = className;
		this.userId = userId;
		this.userName = userName;
		this.typeId = typeId;
	}

	// Property accessors

	public String getClassId() {
		return this.classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractViewClassUserId))
			return false;
		AbstractViewClassUserId castOther = (AbstractViewClassUserId) other;

		return ((this.getClassId() == castOther.getClassId()) || (this
				.getClassId() != null
				&& castOther.getClassId() != null && this.getClassId().equals(
				castOther.getClassId())))
				&& ((this.getClassName() == castOther.getClassName()) || (this
						.getClassName() != null
						&& castOther.getClassName() != null && this
						.getClassName().equals(castOther.getClassName())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null
						&& castOther.getUserId() != null && this.getUserId()
						.equals(castOther.getUserId())))
				&& ((this.getUserName() == castOther.getUserName()) || (this
						.getUserName() != null
						&& castOther.getUserName() != null && this
						.getUserName().equals(castOther.getUserName())))
				&& ((this.getTypeId() == castOther.getTypeId()) || (this
						.getTypeId() != null
						&& castOther.getTypeId() != null && this.getTypeId()
						.equals(castOther.getTypeId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getClassId() == null ? 0 : this.getClassId().hashCode());
		result = 37 * result
				+ (getClassName() == null ? 0 : this.getClassName().hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getUserName() == null ? 0 : this.getUserName().hashCode());
		result = 37 * result
				+ (getTypeId() == null ? 0 : this.getTypeId().hashCode());
		return result;
	}

}