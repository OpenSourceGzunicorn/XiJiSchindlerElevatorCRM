package com.gzunicorn.hibernate.engcontractmanager.servicingcontractquotemaster;

import java.util.Set;

/**
 * ServicingContractQuoteMaster entity. @author MyEclipse Persistence Tools
 */
public class ServicingContractQuoteMaster extends
		AbstractServicingContractQuoteMaster implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ServicingContractQuoteMaster() {
	}

	/** minimal constructor */
	public ServicingContractQuoteMaster(String billNo, Integer status,
			Long tokenId, String processName, String submitType, String attn,
			String applyDate, String maintDivision, String busType,
			String companyId, Double standardQuoteTotal,
			Double finallyQuoteTotal, Double businessCosts, String operId,
			String operDate,String otherCustomer) {
		super(billNo, status, tokenId, processName, submitType, attn,
				applyDate, maintDivision, busType, companyId,
				standardQuoteTotal, finallyQuoteTotal, businessCosts, operId,
				operDate, otherCustomer);
	}

	/** full constructor */
	public ServicingContractQuoteMaster(String billNo, Integer status,
			Long tokenId, String processName, String submitType, String attn,
			String applyDate, String maintDivision, String maintStation,
			String busType, String budgetid, String maintContractNo,
			String companyId, Double standardQuoteTotal,
			Double finallyQuoteTotal, Double businessCosts,
			String priceFluctuaApply, String businessCostsApply,
			String paymentMethodApply, String specialApplication,
			String contractContentApply, String isPass, String operId,
			String operDate, String rem, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, Set servicingContractQuoteDetails,String otherCustomer) {
		super(billNo, status, tokenId, processName, submitType, attn,
				applyDate, maintDivision, maintStation, busType, budgetid,
				maintContractNo, companyId, standardQuoteTotal,
				finallyQuoteTotal, businessCosts, priceFluctuaApply,
				businessCostsApply, paymentMethodApply, specialApplication,
				contractContentApply, isPass, operId, operDate, rem, r1, r2,
				r3, r4, r5, r6, r7, r8, r9, r10, servicingContractQuoteDetails, otherCustomer);
	}

}
