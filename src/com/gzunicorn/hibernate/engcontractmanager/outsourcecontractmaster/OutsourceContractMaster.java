package com.gzunicorn.hibernate.engcontractmanager.outsourcecontractmaster;

import java.util.Set;

/**
 * OutsourceContractMaster entity. @author MyEclipse Persistence Tools
 */
public class OutsourceContractMaster extends AbstractOutsourceContractMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public OutsourceContractMaster() {
	}

	/** minimal constructor */
	public OutsourceContractMaster(String billno, String wgBillno,
			String companyId, String companyId2, String outContractNo,
			String maintContractNo, String busType, String signingDate,
			Double contractTotal, String attn, String maintDivision,
			String maintStation, String operId, String operDate,
			String submitType,String quoteBillNo) {
		super(billno, wgBillno, companyId, companyId2, outContractNo,
				maintContractNo, busType, signingDate, contractTotal, attn,
				maintDivision, maintStation, operId, operDate, submitType,quoteBillNo);
	}

	/** full constructor */
	public OutsourceContractMaster(String billno, String wgBillno,
			String companyId, String companyId2, String outContractNo,
			String maintContractNo, String busType, String signingDate,
			String contractTerms, String paymentMethod, Double contractTotal,
			Double otherFee, String attn, String maintDivision,
			String maintStation, String auditOperid, String auditStatus,
			String auditDate, String auditRem, String operId, String operDate,
			String submitType, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, Set outsourceContractDetails,String quoteBillNo,
			String workisdisplay,String workisdisplay2) {
		super(billno, wgBillno, companyId, companyId2, outContractNo,
				maintContractNo, busType, signingDate, contractTerms,
				paymentMethod, contractTotal, otherFee, attn, maintDivision,
				maintStation, auditOperid, auditStatus, auditDate, auditRem,
				operId, operDate, submitType, r1, r2, r3, r4, r5, r6, r7, r8,
				r9, r10, outsourceContractDetails,quoteBillNo,workisdisplay,workisdisplay2);
	}

}
