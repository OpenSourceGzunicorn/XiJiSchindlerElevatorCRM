package com.gzunicorn.hibernate.engcontractmanager.servicingcontractquoteotherdetail;

import com.gzunicorn.hibernate.engcontractmanager.servicingcontractquotemaster.ServicingContractQuoteMaster;

/**
 * ServicingContractQuoteOtherDetail entity. @author MyEclipse Persistence Tools
 */
public class ServicingContractQuoteOtherDetail extends
		AbstractServicingContractQuoteOtherDetail implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ServicingContractQuoteOtherDetail() {
	}

	/** minimal constructor */
	public ServicingContractQuoteOtherDetail(
			ServicingContractQuoteMaster servicingContractQuoteMaster,
			String feeName, Double price, Double finalPrice) {
		super(servicingContractQuoteMaster, feeName, price, finalPrice);
	}

	/** full constructor */
	public ServicingContractQuoteOtherDetail(
			ServicingContractQuoteMaster servicingContractQuoteMaster,
			String feeName, Double price, Double finalPrice, String r1,
			String r2, String r3, String r4, String r5, Double r6, Double r7,
			Double r8, Integer r9, Integer r10) {
		super(servicingContractQuoteMaster, feeName, price, finalPrice, r1, r2,
				r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
