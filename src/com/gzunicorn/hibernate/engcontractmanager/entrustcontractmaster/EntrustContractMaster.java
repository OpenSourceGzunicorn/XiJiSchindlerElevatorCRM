package com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster;

import java.util.Set;

/**
 * EntrustContractMaster entity. @author MyEclipse Persistence Tools
 */
public class EntrustContractMaster extends AbstractEntrustContractMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public EntrustContractMaster() {
	}

	/** minimal constructor */
	public EntrustContractMaster(String billNo, String maintBillNo,
			String companyId, String companyId2, String entrustContractNo,
			String maintContractNo, String contractNatureOf, String mainMode,
			String contractPeriod, String contractSdate, String contractEdate,
			Double contractTotal, String attn, String maintDivision,
			String operId, String operDate, String submitType,String quoteBillNo) {
		super(billNo, maintBillNo, companyId, companyId2, entrustContractNo,
				maintContractNo, contractNatureOf, mainMode, contractPeriod,
				contractSdate, contractEdate, contractTotal, attn,
				maintDivision, operId, operDate, submitType,quoteBillNo);
	}

	/** full constructor */
	public EntrustContractMaster(String billNo, String maintBillNo,
			String companyId, String companyId2, String entrustContractNo,
			String maintContractNo, String contractNatureOf, String mainMode,
			String contractPeriod, String contractSdate, String contractEdate,
			Double contractTotal, Double otherFee, String contractTerms,
			String paymentMethod, String attn, String maintDivision,
			String auditOperid, String auditStatus, String auditDate,
			String auditRem, String operId, String operDate, String submitType,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10, String maintStation,
			Set entrustContractDetails,
			String quoteBillNo,String workisdisplay,String workisdisplay2,
			String endDate,String endOperId,String endOperDate,
			String tbDate,String tbOperId,String tbOperDate,
			String modifyId,String modifyDate, Double oldContractTotal,Double oldOtherFee) {
		super(billNo, maintBillNo, companyId, companyId2, entrustContractNo,
				maintContractNo, contractNatureOf, mainMode, contractPeriod,
				contractSdate, contractEdate, contractTotal, otherFee,
				contractTerms, paymentMethod, attn, maintDivision, auditOperid,
				auditStatus, auditDate, auditRem, operId, operDate, submitType,
				r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, maintStation,
				entrustContractDetails,quoteBillNo,workisdisplay,workisdisplay2,
			    endDate,endOperId,endOperDate, tbDate, tbOperId, tbOperDate, 
			    modifyId, modifyDate, oldContractTotal, oldOtherFee);
	}

}
