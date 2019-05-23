package com.gzunicorn.hibernate.basedata.handoverelevatorcheckitem;

/**
 * AbstractHandoverElevatorCheckItemId entity provides the base persistence
 * definition of the HandoverElevatorCheckItemId entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractHandoverElevatorCheckItemId implements
		java.io.Serializable {

	// Fields

	private String examType;
	private String checkItem;
	private String issueCoding;

	// Constructors

	/** default constructor */
	public AbstractHandoverElevatorCheckItemId() {
	}

	/** full constructor */
	public AbstractHandoverElevatorCheckItemId(String examType,
			String checkItem, String issueCoding) {
		this.examType = examType;
		this.checkItem = checkItem;
		this.issueCoding = issueCoding;
	}

	// Property accessors

	public String getExamType() {
		return this.examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getCheckItem() {
		return this.checkItem;
	}

	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}

	public String getIssueCoding() {
		return this.issueCoding;
	}

	public void setIssueCoding(String issueCoding) {
		this.issueCoding = issueCoding;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractHandoverElevatorCheckItemId))
			return false;
		AbstractHandoverElevatorCheckItemId castOther = (AbstractHandoverElevatorCheckItemId) other;

		return ((this.getExamType() == castOther.getExamType()) || (this
				.getExamType() != null && castOther.getExamType() != null && this
				.getExamType().equals(castOther.getExamType())))
				&& ((this.getCheckItem() == castOther.getCheckItem()) || (this
						.getCheckItem() != null
						&& castOther.getCheckItem() != null && this
						.getCheckItem().equals(castOther.getCheckItem())))
				&& ((this.getIssueCoding() == castOther.getIssueCoding()) || (this
						.getIssueCoding() != null
						&& castOther.getIssueCoding() != null && this
						.getIssueCoding().equals(castOther.getIssueCoding())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getExamType() == null ? 0 : this.getExamType().hashCode());
		result = 37 * result
				+ (getCheckItem() == null ? 0 : this.getCheckItem().hashCode());
		result = 37
				* result
				+ (getIssueCoding() == null ? 0 : this.getIssueCoding()
						.hashCode());
		return result;
	}

}