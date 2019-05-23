package com.gzunicorn.hibernate.custregistervisitplan.customervisitplanmaster;

import java.util.Set;

/**
 * CustomerVisitPlanMaster entity. @author MyEclipse Persistence Tools
 */
public class CustomerVisitPlanMaster extends AbstractCustomerVisitPlanMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public CustomerVisitPlanMaster() {
	}

	/** minimal constructor */
	public CustomerVisitPlanMaster(String billno, String companyId,
			String companyName, String principalName, String principalPhone,
			String custLevel, String maintDivision, String maintStation,
			String operId, String operDate) {
		super(billno, companyId, companyName, principalName, principalPhone,
				custLevel, maintDivision, maintStation, operId, operDate);
	}

	/** full constructor */
	public CustomerVisitPlanMaster(String billno, String companyId,
			String companyName, String principalName, String principalPhone,
			String custLevel, String maintDivision, String maintStation,
			String rem, String operId, String operDate, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10, String maintContractNo,
			Set customerVisitPlanDetails) {
		super(billno, companyId, companyName, principalName, principalPhone,
				custLevel, maintDivision, maintStation, rem, operId, operDate,
				r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, maintContractNo,
				customerVisitPlanDetails);
	}

}
