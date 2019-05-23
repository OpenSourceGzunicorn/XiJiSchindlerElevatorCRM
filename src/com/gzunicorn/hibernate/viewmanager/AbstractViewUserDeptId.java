package com.gzunicorn.hibernate.viewmanager;

/**
 * AbstractViewUserDeptId entity provides the base persistence definition of the
 * ViewUserDeptId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractViewUserDeptId implements java.io.Serializable {

	// Fields

	private String userId;
	private String deptId;
	private String flag;

	// Constructors

	/** default constructor */
	public AbstractViewUserDeptId() {
	}

	/** full constructor */
	public AbstractViewUserDeptId(String userId, String deptId, String flag) {
		this.userId = userId;
		this.deptId = deptId;
		this.flag = flag;
	}

	// Property accessors

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractViewUserDeptId))
			return false;
		AbstractViewUserDeptId castOther = (AbstractViewUserDeptId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(
				castOther.getUserId())))
				&& ((this.getDeptId() == castOther.getDeptId()) || (this
						.getDeptId() != null
						&& castOther.getDeptId() != null && this.getDeptId()
						.equals(castOther.getDeptId())))
				&& ((this.getFlag() == castOther.getFlag()) || (this.getFlag() != null
						&& castOther.getFlag() != null && this.getFlag()
						.equals(castOther.getFlag())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getDeptId() == null ? 0 : this.getDeptId().hashCode());
		result = 37 * result
				+ (getFlag() == null ? 0 : this.getFlag().hashCode());
		return result;
	}

}