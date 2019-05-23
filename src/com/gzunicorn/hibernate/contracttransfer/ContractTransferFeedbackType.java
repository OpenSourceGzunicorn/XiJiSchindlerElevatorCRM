package com.gzunicorn.hibernate.contracttransfer;

/**
 * ContractTransferFeedbackType entity. @author MyEclipse Persistence Tools
 */
public class ContractTransferFeedbackType extends
		AbstractContractTransferFeedbackType implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ContractTransferFeedbackType() {
	}

	/** minimal constructor */
	public ContractTransferFeedbackType(String feedbackTypeId,
			String feedbackTypeName, String enabledFlag) {
		super(feedbackTypeId, feedbackTypeName, enabledFlag);
	}

	/** full constructor */
	public ContractTransferFeedbackType(String feedbackTypeId,
			String feedbackTypeName, String enabledFlag, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10) {
		super(feedbackTypeId, feedbackTypeName, enabledFlag, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10);
	}

}
