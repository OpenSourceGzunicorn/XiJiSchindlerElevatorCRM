package com.gzunicorn.hibernate.costmanagement.reimbursexpensemanag;

import java.util.Set;

/**
 * ReimbursExpenseManag entity. @author MyEclipse Persistence Tools
 */
public class ReimbursExpenseManag extends AbstractReimbursExpenseManag
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ReimbursExpenseManag() {
	}

	/** minimal constructor */
	public ReimbursExpenseManag(String billno, String maintDivision,
			String maintStation, String reimbursPeople, String reimbursDate,
			Double totalAmount, String operId, String operDate) {
		super(billno, maintDivision, maintStation, reimbursPeople,
				reimbursDate, totalAmount, operId, operDate);
	}

	/** full constructor */
	public ReimbursExpenseManag(String billno, String maintDivision,
			String maintStation, String reimbursPeople, String reimbursDate,
			Double totalAmount, Double projectMoney, Double stationMoney,
			String operId, String operDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, Double divsionMoney, Double noReimMoney,
			Set maintStationReimbursements, Set maintDivisionReimbursements,
			Set projectReimbursements, Set noReimbursements) {
		super(billno, maintDivision, maintStation, reimbursPeople,
				reimbursDate, totalAmount, projectMoney, stationMoney, operId,
				operDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				divsionMoney, noReimMoney, maintStationReimbursements,
				maintDivisionReimbursements, projectReimbursements,
				noReimbursements);
	}

}
