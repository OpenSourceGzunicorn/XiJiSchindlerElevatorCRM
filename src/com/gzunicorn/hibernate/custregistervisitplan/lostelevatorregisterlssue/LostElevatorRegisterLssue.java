package com.gzunicorn.hibernate.custregistervisitplan.lostelevatorregisterlssue;

/**
 * LostElevatorRegisterLssue entity. @author MyEclipse Persistence Tools
 */
public class LostElevatorRegisterLssue extends
		AbstractLostElevatorRegisterLssue implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public LostElevatorRegisterLssue() {
	}

	/** minimal constructor */
	public LostElevatorRegisterLssue(String jnlno, String lssueDetail) {
		super(jnlno, lssueDetail);
	}

	/** full constructor */
	public LostElevatorRegisterLssue(String jnlno, String lssueDetail,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Integer r7, Integer r8, Double r9, Double r10) {
		super(jnlno, lssueDetail, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
