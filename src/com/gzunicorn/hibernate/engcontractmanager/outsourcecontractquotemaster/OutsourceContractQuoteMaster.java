package com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquotemaster;

/**
 * OutsourceContractQuoteMaster entity. @author MyEclipse Persistence Tools
 */
public class OutsourceContractQuoteMaster extends
		AbstractOutsourceContractQuoteMaster implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public OutsourceContractQuoteMaster() {
	}

	/** minimal constructor */
	public OutsourceContractQuoteMaster(String billNo, String wgBillno,
			Integer status, Long tokenId, String processName,
			String submitType, String companyId, Double standardPrice,
			Double realPrice, Double markups, String operId, String operDate) {
		super(billNo, wgBillno, status, tokenId, processName, submitType,
				companyId, standardPrice, realPrice, markups, operId, operDate);
	}

	/** full constructor */
	public OutsourceContractQuoteMaster(String billNo, String wgBillno,
			Integer status, Long tokenId, String processName,
			String submitType, String companyId, Double standardPrice,
			Double realPrice, Double markups, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10,
			String maintContractNo, String maintDivision, String maintStation,String rem) {
		super(billNo, wgBillno, status, tokenId, processName, submitType,
				companyId, standardPrice, realPrice, markups, operId, operDate,
				r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, maintContractNo,
				maintDivision, maintStation,rem);
	}

}
