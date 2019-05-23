package com.gzunicorn.hibernate.maintcontractother;

/**
 * MaintContractOther entity. @author MyEclipse Persistence Tools
 */
public class MaintContractOther extends AbstractMaintContractOther implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintContractOther() {
	}

	/** minimal constructor */
	public MaintContractOther(String billno) {
		super(billno);
	}

	/** full constructor */
	public MaintContractOther(String billno, String paydate, Double paymoney,
			String rem, String operid, String operdate, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10) {
		super(billno, paydate, paymoney, rem, operid, operdate, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10);
	}

}
