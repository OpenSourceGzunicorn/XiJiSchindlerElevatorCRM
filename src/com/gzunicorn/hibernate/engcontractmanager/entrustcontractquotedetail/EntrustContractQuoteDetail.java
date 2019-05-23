package com.gzunicorn.hibernate.engcontractmanager.entrustcontractquotedetail;

/**
 * EntrustContractQuoteDetail entity. @author MyEclipse Persistence Tools
 */
public class EntrustContractQuoteDetail extends
		AbstractEntrustContractQuoteDetail implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public EntrustContractQuoteDetail() {
	}

	/** minimal constructor */
	public EntrustContractQuoteDetail(String billNo, Integer wbRowid) {
		super(billNo, wbRowid);
	}

	/** full constructor */
	public EntrustContractQuoteDetail(
			String billNo,
			Integer wbRowid, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String mainSdate, String mainEdate) {
		super(billNo, wbRowid, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10, mainSdate, mainEdate);
	}

}
