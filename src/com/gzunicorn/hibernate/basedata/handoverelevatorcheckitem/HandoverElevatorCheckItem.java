package com.gzunicorn.hibernate.basedata.handoverelevatorcheckitem;

/**
 * HandoverElevatorCheckItem entity. @author MyEclipse Persistence Tools
 */
public class HandoverElevatorCheckItem extends
		AbstractHandoverElevatorCheckItem implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public HandoverElevatorCheckItem() {
	}

	/** minimal constructor */
	public HandoverElevatorCheckItem(HandoverElevatorCheckItemId id,
			String issueContents, String enabledFlag, String operId,Double orderby,
			String operDate) {
		super(id, issueContents, enabledFlag, operId,orderby,operDate);
	}

	/** full constructor */
	public HandoverElevatorCheckItem(HandoverElevatorCheckItemId id,
			String issueContents, String enabledFlag, String rem,
			String operId, String operDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String elevatorType,Double orderby,String itemgroup) {
		super(id, issueContents, enabledFlag, rem, operId, operDate, r1, r2,
				r3, r4, r5, r6, r7, r8, r9, r10, elevatorType,orderby,itemgroup);
	}

}
