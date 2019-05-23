package com.gzunicorn.hibernate.custregistervisitplan.effectiveelevatormaintain;

/**
 * EffectiveElevatorMaintain entity. @author MyEclipse Persistence Tools
 */
public class EffectiveElevatorMaintain extends
		AbstractEffectiveElevatorMaintain implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public EffectiveElevatorMaintain() {
	}

	/** minimal constructor */
	public EffectiveElevatorMaintain(String billno, String companyId,
			String contacts, String contactPhone, Integer reOrder,
			String reMark, String enabledFlag, String operId, String operDate) {
		super(billno, companyId, contacts, contactPhone, reOrder, reMark,
				enabledFlag, operId, operDate);
	}

	/** full constructor */
	public EffectiveElevatorMaintain(String billno, String companyId,
			String contacts, String contactPhone, Integer reOrder,
			String reMark, String enabledFlag, String rem, String operId,
			String operDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9, Double r10,String companyName) {
		super(billno, companyId, contacts, contactPhone, reOrder, reMark,
				enabledFlag, rem, operId, operDate, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10,companyName);
	}

}
