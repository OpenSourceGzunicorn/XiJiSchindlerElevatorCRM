package com.gzunicorn.hibernate.costmanagement.maintstationreimbursement;

import java.util.Set;

import com.gzunicorn.hibernate.costmanagement.reimbursexpensemanag.ReimbursExpenseManag;

/**
 * MaintStationReimbursement entity. @author MyEclipse Persistence Tools
 */
public class MaintStationReimbursement extends
		AbstractMaintStationReimbursement implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintStationReimbursement() {
	}

	/** minimal constructor */
	public MaintStationReimbursement(String jnlno, String reimburType,
			Double money, String maintDivisionBx, String maintStationBx) {
		super(jnlno, reimburType, money, maintDivisionBx, maintStationBx);
	}

	/** full constructor */
	public MaintStationReimbursement(String jnlno,
			ReimbursExpenseManag reimbursExpenseManag, String reimburType,
			Double money, String rem, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String maintDivisionBx, String maintStationBx,
			Set maintStationReimbursementDetails) {
		super(jnlno, reimbursExpenseManag, reimburType, money, rem, r1, r2, r3,
				r4, r5, r6, r7, r8, r9, r10, maintDivisionBx, maintStationBx,
				maintStationReimbursementDetails);
	}

}
