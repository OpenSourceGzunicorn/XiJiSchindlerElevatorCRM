package com.gzunicorn.hibernate.costmanagement.maintdivisionreimbursementdetail;

import com.gzunicorn.hibernate.costmanagement.maintdivisionreimbursement.MaintDivisionReimbursement;

/**
 * MaintDivisionReimbursementDetail entity. @author MyEclipse Persistence Tools
 */
public class MaintDivisionReimbursementDetail extends
		AbstractMaintDivisionReimbursementDetail implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintDivisionReimbursementDetail() {
	}

	/** minimal constructor */
	public MaintDivisionReimbursementDetail(String maintContractNo,
			Integer num, Double money, String maintDivisionBx,
			String maintStationBx) {
		super(maintContractNo, num, money, maintDivisionBx, maintStationBx);
	}

	/** full constructor */
	public MaintDivisionReimbursementDetail(
			MaintDivisionReimbursement maintDivisionReimbursement,
			String maintContractNo, Integer num, Double money, String rem,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10,
			String maintDivisionBx, String maintStationBx) {
		super(maintDivisionReimbursement, maintContractNo, num, money, rem, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10, maintDivisionBx,
				maintStationBx);
	}

}
