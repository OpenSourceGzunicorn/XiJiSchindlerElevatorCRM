package com.gzunicorn.hibernate.costmanagement.projectreimbursement;

import com.gzunicorn.hibernate.costmanagement.reimbursexpensemanag.ReimbursExpenseManag;

/**
 * ProjectReimbursement entity. @author MyEclipse Persistence Tools
 */
public class ProjectReimbursement extends AbstractProjectReimbursement
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ProjectReimbursement() {
	}

	/** minimal constructor */
	public ProjectReimbursement(String maintContractNo, String busType,
			String projectName, Integer num, Double money, String reimburType) {
		super(maintContractNo, busType, projectName, num, money, reimburType);
	}

	/** full constructor */
	public ProjectReimbursement(ReimbursExpenseManag reimbursExpenseManag,
			String maintContractNo, String busType, String projectName,
			Integer num, Double money, String reimburType, String rem,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10,
			String maintDivisionBx, String maintStationBx) {
		super(reimbursExpenseManag, maintContractNo, busType, projectName, num,
				money, reimburType, rem, r1, r2, r3, r4, r5, r6, r7, r8, r9,
				r10, maintDivisionBx, maintStationBx);
	}

}
