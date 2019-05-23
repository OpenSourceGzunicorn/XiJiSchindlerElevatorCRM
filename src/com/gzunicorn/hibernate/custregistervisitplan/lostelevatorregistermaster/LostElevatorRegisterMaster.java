package com.gzunicorn.hibernate.custregistervisitplan.lostelevatorregistermaster;

/**
 * LostElevatorRegisterMaster entity. @author MyEclipse Persistence Tools
 */
public class LostElevatorRegisterMaster extends
		AbstractLostElevatorRegisterMaster implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public LostElevatorRegisterMaster() {
	}

	/** minimal constructor */
	public LostElevatorRegisterMaster(String billno, String companyId,
			String contacts, String contactPhone, String ministerHandle,
			String operId, String operDate, String maintDivision) {
		super(billno, companyId, contacts, contactPhone, ministerHandle,
				operId, operDate, maintDivision);
	}

	/** full constructor */
	public LostElevatorRegisterMaster(String billno, String companyId,
			String contacts, String contactPhone, String ministerHandle,
			String handleId, String handleDate, String handleResult,
			String returnResult, String returnRem, String rem, String operId,
			String operDate, String maintDivision, String r1, String r2,
			String r3, String r4, String r5, Double r6, Integer r7, Integer r8,
			Double r9, Double r10) {
		super(billno, companyId, contacts, contactPhone, ministerHandle,
				handleId, handleDate, handleResult, returnResult, returnRem,
				rem, operId, operDate, maintDivision, r1, r2, r3, r4, r5, r6,
				r7, r8, r9, r10);
	}

}
