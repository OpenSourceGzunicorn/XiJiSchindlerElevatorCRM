package com.gzunicorn.hibernate.proshr;

/**
 * MainMugContractMasterProcessApprove entity. @author MyEclipse Persistence
 * Tools
 */
public class MainMugContractMasterProcessApprove extends
		AbstractMainMugContractMasterProcessApprove implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MainMugContractMasterProcessApprove() {
	}

	/** minimal constructor */
	public MainMugContractMasterProcessApprove(String billNo, Integer taskId,
			String taskName, Long tokenId, String userId, String date1,
			String time1) {
		super(billNo, taskId, taskName, tokenId, userId, date1, time1);
	}

	/** full constructor */
	public MainMugContractMasterProcessApprove(String billNo, Integer taskId,
			String taskName, Long tokenId, String userId, String date1,
			String time1, String approveResult, String approveRem,
			String approveRem2, String approveRem3, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10) {
		super(billNo, taskId, taskName, tokenId, userId, date1, time1,
				approveResult, approveRem, approveRem2, approveRem3, r1, r2,
				r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
