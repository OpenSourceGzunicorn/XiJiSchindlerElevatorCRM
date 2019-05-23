package com.gzunicorn.hibernate.costmanagement.maintdivisionreimbursement;

import java.util.Set;

import com.gzunicorn.hibernate.costmanagement.reimbursexpensemanag.ReimbursExpenseManag;

/**
 * MaintDivisionReimbursement entity. @author MyEclipse Persistence Tools
 */
public class MaintDivisionReimbursement extends
		AbstractMaintDivisionReimbursement implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintDivisionReimbursement() {
	}

	/** minimal constructor */
	public MaintDivisionReimbursement(String jnlno, String reimburType,
			Double money, String maintDivisionBx) {
		super(jnlno, reimburType, money, maintDivisionBx);
	}

	/** full constructor */
	public MaintDivisionReimbursement(String jnlno,
			ReimbursExpenseManag reimbursExpenseManag, String reimburType,
			Double money, String maintDivisionBx, String rem, String r1,
			String r2, String r3, String r4, String r5, Double r6, Double r7,
			Double r8, Integer r9, Integer r10,
			Set maintDivisionReimbursementDetails) {
		super(jnlno, reimbursExpenseManag, reimburType, money, maintDivisionBx,
				rem, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				maintDivisionReimbursementDetails);
	}

}
