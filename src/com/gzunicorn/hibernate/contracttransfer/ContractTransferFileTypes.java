package com.gzunicorn.hibernate.contracttransfer;

/**
 * ContractTransferFileTypes entity. @author MyEclipse Persistence Tools
 */
public class ContractTransferFileTypes extends
		AbstractContractTransferFileTypes implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ContractTransferFileTypes() {
	}

	/** minimal constructor */
	public ContractTransferFileTypes(String jnlno, String fileType) {
		super(jnlno, fileType);
	}

	/** full constructor */
	public ContractTransferFileTypes(String jnlno, String billNo,
			String fileType, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(jnlno, billNo, fileType, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
