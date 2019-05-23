package com.gzunicorn.hibernate.costmanagement.maintstationreimbursementdetail;

import com.gzunicorn.hibernate.costmanagement.maintstationreimbursement.MaintStationReimbursement;

/**
 * MaintStationReimbursementDetail entity. @author MyEclipse Persistence Tools
 */
public class MaintStationReimbursementDetail extends
		AbstractMaintStationReimbursementDetail implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintStationReimbursementDetail() {
	}

	/** minimal constructor */
	public MaintStationReimbursementDetail(String maintContractNo, Integer num,
			Double money, String maintDivisionBx, String maintStationBx) {
		super(maintContractNo, num, money, maintDivisionBx, maintStationBx);
	}

	/** full constructor */
	public MaintStationReimbursementDetail(
			MaintStationReimbursement maintStationReimbursement,
			String maintContractNo, Integer num, Double money, String rem,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10,
			String maintDivisionBx, String maintStationBx) {
		super(maintStationReimbursement, maintContractNo, num, money, rem, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10, maintDivisionBx,
				maintStationBx);
	}

}
