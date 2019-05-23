package com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquotedetail;

/**
 * OutsourceContractQuoteDetail entity. @author MyEclipse Persistence Tools
 */
public class OutsourceContractQuoteDetail extends
		AbstractOutsourceContractQuoteDetail implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public OutsourceContractQuoteDetail() {
	}

	/** minimal constructor */
	public OutsourceContractQuoteDetail(String billNo, Integer wgRowid) {
		super(billNo, wgRowid);
	}

	/** full constructor */
	public OutsourceContractQuoteDetail(String billNo, Integer wgRowid,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(billNo, wgRowid, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
