package com.gzunicorn.hibernate.sysmanager.pulldown;

/**
 * AbstractPulldownId entity provides the base persistence definition of the
 * PulldownId entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractPulldownId implements java.io.Serializable {

	// Fields

	private String pullid;
	private String typeflag;

	// Constructors

	/** default constructor */
	public AbstractPulldownId() {
	}

	/** full constructor */
	public AbstractPulldownId(String pullid, String typeflag) {
		this.pullid = pullid;
		this.typeflag = typeflag;
	}

	// Property accessors

	public String getPullid() {
		return this.pullid;
	}

	public void setPullid(String pullid) {
		this.pullid = pullid;
	}

	public String getTypeflag() {
		return this.typeflag;
	}

	public void setTypeflag(String typeflag) {
		this.typeflag = typeflag;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractPulldownId))
			return false;
		AbstractPulldownId castOther = (AbstractPulldownId) other;

		return ((this.getPullid() == castOther.getPullid()) || (this
				.getPullid() != null
				&& castOther.getPullid() != null && this.getPullid().equals(
				castOther.getPullid())))
				&& ((this.getTypeflag() == castOther.getTypeflag()) || (this
						.getTypeflag() != null
						&& castOther.getTypeflag() != null && this
						.getTypeflag().equals(castOther.getTypeflag())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getPullid() == null ? 0 : this.getPullid().hashCode());
		result = 37 * result
				+ (getTypeflag() == null ? 0 : this.getTypeflag().hashCode());
		return result;
	}

}