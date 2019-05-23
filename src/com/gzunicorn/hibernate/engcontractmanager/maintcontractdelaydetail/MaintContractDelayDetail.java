package com.gzunicorn.hibernate.engcontractmanager.maintcontractdelaydetail;

/**
 * MaintContractDelayDetail entity. @author MyEclipse Persistence Tools
 */
public class MaintContractDelayDetail extends AbstractMaintContractDelayDetail
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintContractDelayDetail() {
	}

	/** minimal constructor */
	public MaintContractDelayDetail(String jnlno, Integer rowid,
			String delayEdate) {
		super(jnlno, rowid, delayEdate);
	}

	/** full constructor */
	public MaintContractDelayDetail(String jnlno, Integer rowid,
			String delayEdate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(jnlno, rowid, delayEdate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
