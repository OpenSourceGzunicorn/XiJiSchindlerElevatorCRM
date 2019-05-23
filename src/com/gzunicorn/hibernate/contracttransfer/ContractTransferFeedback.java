package com.gzunicorn.hibernate.contracttransfer;

/**
 * ContractTransferFeedback entity. @author MyEclipse Persistence Tools
 */
public class ContractTransferFeedback extends AbstractContractTransferFeedback
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ContractTransferFeedback() {
	}

	/** minimal constructor */
	public ContractTransferFeedback(String jnlno, String operId, String operDate) {
		super(jnlno, operId, operDate);
	}

	/** full constructor */
	public ContractTransferFeedback(String jnlno, String billNo, String operId,
			String operDate, String transferRem, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10,String feedbacktypeid) {
		super(jnlno, billNo, operId, operDate, transferRem, r1, r2, r3, r4, r5,
				r6, r7, r8, r9, r10,feedbacktypeid);
	}

}
