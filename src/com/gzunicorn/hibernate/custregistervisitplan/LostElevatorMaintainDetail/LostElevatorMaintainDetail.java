package com.gzunicorn.hibernate.custregistervisitplan.LostElevatorMaintainDetail;

import com.gzunicorn.hibernate.custregistervisitplan.LostElevatorMaintain.LostElevatorMaintain;

/**
 * LostElevatorMaintainDetail entity. @author MyEclipse Persistence Tools
 */
public class LostElevatorMaintainDetail extends
		AbstractLostElevatorMaintainDetail implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public LostElevatorMaintainDetail() {
	}

	/** minimal constructor */
	public LostElevatorMaintainDetail(
			LostElevatorMaintain lostElevatorMaintain, String jnlno,
			String wbBillno, String maintContractNo, String lostElevatorDate,
			String causeAnalysis) {
		super(lostElevatorMaintain, jnlno, wbBillno, maintContractNo,
				lostElevatorDate, causeAnalysis);
	}

	/** full constructor */
	public LostElevatorMaintainDetail(
			LostElevatorMaintain lostElevatorMaintain, String jnlno,
			String wbBillno, String maintContractNo, String lostElevatorDate,
			String causeAnalysis, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9, Double r10) {
		super(lostElevatorMaintain, jnlno, wbBillno, maintContractNo,
				lostElevatorDate, causeAnalysis, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10);
	}

}
