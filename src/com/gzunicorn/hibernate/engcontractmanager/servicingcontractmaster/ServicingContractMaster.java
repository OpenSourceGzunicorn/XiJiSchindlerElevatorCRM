package com.gzunicorn.hibernate.engcontractmanager.servicingcontractmaster;

import java.util.Set;

/**
 * ServicingContractMaster entity. @author MyEclipse Persistence Tools
 */
public class ServicingContractMaster extends AbstractServicingContractMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ServicingContractMaster() {
	}

	/** minimal constructor */
	public ServicingContractMaster(String wgBillno, String billNo,
			String companyId, String companyId2, String maintContractNo,
			String busType, String signingDate, Double contractTotal,
			String attn, String maintDivision, String maintStation,
			String operId, String operDate) {
		super(wgBillno, billNo, companyId, companyId2, maintContractNo,
				busType, signingDate, contractTotal, attn, maintDivision,
				maintStation, operId, operDate);
	}

	/** full constructor */
	public ServicingContractMaster(String wgBillno, String billNo,
			String companyId, String companyId2, String maintContractNo,
			String busType, String signingDate, String contractTerms,
			String paymentMethod, Double contractTotal, Double otherFee,
			String attn, String maintDivision, String maintStation,
			String auditOperid, String auditStatus, String auditDate,
			String auditRem, String operId, String operDate, String taskUserId,
			String taskSubFlag, String taskSubDate, String taskRem,
			String forecastDate, String comfirmDate, String itemUserId,
			String appWorkDate, String enterArenaDate, String epibolyFlag,
			String finishFlag, String finishDate, String finishId,
			String finishRem, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String submitType, Double basePrice,
			Set servicingContractDetails) {
		super(wgBillno, billNo, companyId, companyId2, maintContractNo,
				busType, signingDate, contractTerms, paymentMethod,
				contractTotal, otherFee, attn, maintDivision, maintStation,
				auditOperid, auditStatus, auditDate, auditRem, operId,
				operDate, taskUserId, taskSubFlag, taskSubDate, taskRem,
				forecastDate, comfirmDate, itemUserId, appWorkDate,
				enterArenaDate, epibolyFlag, finishFlag, finishDate, finishId,
				finishRem, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, submitType,
				basePrice, servicingContractDetails);
	}

}
