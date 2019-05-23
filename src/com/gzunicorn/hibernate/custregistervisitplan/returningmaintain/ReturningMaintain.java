package com.gzunicorn.hibernate.custregistervisitplan.returningmaintain;

/**
 * ReturningMaintain entity. @author MyEclipse Persistence Tools
 */
public class ReturningMaintain extends AbstractReturningMaintain implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ReturningMaintain() {
	}

	/** minimal constructor */
	public ReturningMaintain(String billno, String contactPhone,
			String contacts, String companyId, Integer reOrder, String reMark,
			String enabledFlag, String operId, String operDate) {
		super(billno, contactPhone, contacts, companyId, reOrder, reMark,
				enabledFlag, operId, operDate);
	}

	/** full constructor */
	public ReturningMaintain(String billno, String contactPhone,
			String contacts, String companyId, Integer reOrder, String reMark,
			String enabledFlag, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Integer r7, Integer r8, Double r9, Double r10) {
		super(billno, contactPhone, contacts, companyId, reOrder, reMark,
				enabledFlag, rem, operId, operDate, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10);
	}

}
