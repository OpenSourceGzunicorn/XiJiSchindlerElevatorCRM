package com.gzunicorn.hibernate.engcontractmanager.maintcontractquoteprocess;

/**
 * MaintContractQuoteProcess entity. @author MyEclipse Persistence Tools
 */
public class MaintContractQuoteProcess extends
		AbstractMaintContractQuoteProcess implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintContractQuoteProcess() {
	}

	/** minimal constructor */
	public MaintContractQuoteProcess(Integer itemId, String billNo,
			Integer taskId, String taskName, Long tokenId, String userId,
			String date1, String time1) {
		super(itemId, billNo, taskId, taskName, tokenId, userId, date1, time1);
	}

	/** full constructor */
	public MaintContractQuoteProcess(Integer itemId, String billNo,
			Integer taskId, String taskName, Long tokenId, String userId,
			String date1, String time1, String approveResult,
			String approveRem, String approveRem2, String approveRem3,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(itemId, billNo, taskId, taskName, tokenId, userId, date1, time1,
				approveResult, approveRem, approveRem2, approveRem3, r1, r2,
				r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
