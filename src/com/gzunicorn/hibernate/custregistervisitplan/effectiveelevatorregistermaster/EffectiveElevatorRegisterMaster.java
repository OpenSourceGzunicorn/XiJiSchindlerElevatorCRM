package com.gzunicorn.hibernate.custregistervisitplan.effectiveelevatorregistermaster;

/**
 * EffectiveElevatorRegisterMaster entity. @author MyEclipse Persistence Tools
 */
public class EffectiveElevatorRegisterMaster extends
		AbstractEffectiveElevatorRegisterMaster implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public EffectiveElevatorRegisterMaster() {
	}

	/** minimal constructor */
	public EffectiveElevatorRegisterMaster(String billno, String companyId,
			String contacts, String contactPhone, String ministerHandle,
			String operId, String operDate) {
		super(billno, companyId, contacts, contactPhone, ministerHandle,
				operId, operDate);
	}

	/** full constructor */
	public EffectiveElevatorRegisterMaster(String billno, String companyId,
			String contacts, String contactPhone, String ministerHandle,
			String handleId, String handleDate, String handleResult,
			String returnResult, String returnRem, String rem, String operId,
			String operDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9, Double r10,String companyName) {
		super(billno, companyId, contacts, contactPhone, ministerHandle,
				handleId, handleDate, handleResult, returnResult, returnRem,
				rem, operId, operDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,companyName);
	}

}
