package com.gzunicorn.hibernate.engcontractmanager.maintcontractdelayprocess;

/**
 * MaintContractDelayProcess entity. @author MyEclipse Persistence Tools
 */
public class MaintContractDelayProcess extends
		AbstractMaintContractDelayProcess implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintContractDelayProcess() {
	}

	/** minimal constructor */
	public MaintContractDelayProcess(Integer taskId, Long tokenId,
			String userId, String date1, String time1) {
		super(taskId, tokenId, userId, date1, time1);
	}

	/** full constructor */
	public MaintContractDelayProcess(String jnlno, Integer taskId,
			String taskName, Long tokenId, String userId, String date1,
			String time1, String approveResult, String approveRem,
			String approveRem2, String approveRem3, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10) {
		super(jnlno, taskId, taskName, tokenId, userId, date1, time1,
				approveResult, approveRem, approveRem2, approveRem3, r1, r2,
				r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
