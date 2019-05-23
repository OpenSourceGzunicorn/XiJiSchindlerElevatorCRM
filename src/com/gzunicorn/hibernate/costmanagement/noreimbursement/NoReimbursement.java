package com.gzunicorn.hibernate.costmanagement.noreimbursement;

import com.gzunicorn.hibernate.costmanagement.reimbursexpensemanag.ReimbursExpenseManag;

/**
 * NoReimbursement entity. @author MyEclipse Persistence Tools
 */
public class NoReimbursement extends AbstractNoReimbursement implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public NoReimbursement() {
	}

	/** minimal constructor */
	public NoReimbursement(String reimburType, Double money,
			String belongsDepart) {
		super(reimburType, money, belongsDepart);
	}

	/** full constructor */
	public NoReimbursement(ReimbursExpenseManag reimbursExpenseManag,
			String reimburType, Double money, String belongsDepart, String rem,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(reimbursExpenseManag, reimburType, money, belongsDepart, rem, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
