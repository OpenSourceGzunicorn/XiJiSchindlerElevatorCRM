package com.gzunicorn.hibernate.basedata.handoverelevatorcheckitem;

/**
 * HandoverElevatorCheckItemId entity. @author MyEclipse Persistence Tools
 */
public class HandoverElevatorCheckItemId extends
		AbstractHandoverElevatorCheckItemId implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public HandoverElevatorCheckItemId() {
	}

	/** full constructor */
	public HandoverElevatorCheckItemId(String examType, String checkItem,
			String issueCoding) {
		super(examType, checkItem, issueCoding);
	}

}
