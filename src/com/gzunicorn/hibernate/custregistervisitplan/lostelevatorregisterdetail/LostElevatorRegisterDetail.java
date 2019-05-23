package com.gzunicorn.hibernate.custregistervisitplan.lostelevatorregisterdetail;

/**
 * LostElevatorRegisterDetail entity. @author MyEclipse Persistence Tools
 */
public class LostElevatorRegisterDetail extends
		AbstractLostElevatorRegisterDetail implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public LostElevatorRegisterDetail() {
	}

	/** minimal constructor */
	public LostElevatorRegisterDetail(String jnlno, String billno,
			String dtJnlno, String wbBillno, String maintContractNo,
			String lostElevatorDate, String causeAnalysis) {
		super(jnlno, billno, dtJnlno, wbBillno, maintContractNo,
				lostElevatorDate, causeAnalysis);
	}

	/** full constructor */
	public LostElevatorRegisterDetail(String jnlno, String billno,
			String dtJnlno, String wbBillno, String maintContractNo,
			String lostElevatorDate, String causeAnalysis, String r1,
			String r2, String r3, String r4, String r5, Double r6, Integer r7,
			Integer r8, Double r9, Double r10) {
		super(jnlno, billno, dtJnlno, wbBillno, maintContractNo,
				lostElevatorDate, causeAnalysis, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10);
	}

}
