package com.gzunicorn.hibernate.engcontractmanager.entrustcontractquotemaster;

import java.util.Set;

/**
 * EntrustContractQuoteMaster entity. @author MyEclipse Persistence Tools
 */
public class EntrustContractQuoteMaster extends
		AbstractEntrustContractQuoteMaster implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public EntrustContractQuoteMaster() {
	}

	/** minimal constructor */
	public EntrustContractQuoteMaster(String billNo, String maintBillNo,
			Integer status, Long tokenId, String processName,
			String submitType, String companyId, Double standardPrice,
			Double realPrice, Double markups, String operId, String operDate) {
		super(billNo, maintBillNo, status, tokenId, processName, submitType,
				companyId, standardPrice, realPrice, markups, operId, operDate);
	}

	/** full constructor */
	public EntrustContractQuoteMaster(String billNo, String maintBillNo,
			Integer status, Long tokenId, String processName,
			String submitType, String companyId, String assLevel,
			Double standardPrice, Double realPrice, Double markups,
			String operId, String operDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String maintContractNo, String maintDivision,
			String maintStation, String contractPeriod, String contractSdate,
			String contractEdate, String contractNatureOf,String rem,String isEnd) {
		super(billNo, maintBillNo, status, tokenId, processName, submitType,
				companyId, assLevel, standardPrice, realPrice, markups, operId,
				operDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				maintContractNo, maintDivision, maintStation, contractPeriod,
				contractSdate, contractEdate, contractNatureOf,rem,isEnd);
	}

}
