package com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster;

/**
 * MaintContractQuoteMaster entity. @author MyEclipse Persistence Tools
 */
public class MaintContractQuoteMaster extends AbstractMaintContractQuoteMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintContractQuoteMaster() {
	}

	/** minimal constructor */
	public MaintContractQuoteMaster(String billNo, Integer status,
			Long tokenId, String processName, String submitType, String attn,
			String applyDate, String maintDivision, String maintStation,
			String companyId, Double contractYears, Double standardQuoteTotal,
			Double finallyQuoteTotal, Double businessCosts, String operId,
			String operDate) {
		super(billNo, status, tokenId, processName, submitType, attn,
				applyDate, maintDivision, maintStation, companyId,
				contractYears, standardQuoteTotal, finallyQuoteTotal,
				businessCosts, operId, operDate);
	}

	/** full constructor */
	public MaintContractQuoteMaster(String billNo, Integer status,
			Long tokenId, String processName, String submitType, String attn,
			String applyDate, String budgetid, String maintDivision,
			String maintStation, String maintContractNo, String companyId,
			Double contractYears, Double standardQuoteTotal,
			Double finallyQuoteTotal, Double businessCosts,
			String priceFluctuaApply, String businessCostsApply,
			String paymentMethodApply, String specialApplication,
			String contractContentApply, String isPass, String operId,
			String operDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String historyBillNo, String histContractNo,String histContractStatus,
			String customerArea,String companyName, String contacts, String contactPhone,
			String paymentMethodRem,String contractContentRem,
			String workisdisplay,String quoteSignWay,Double discountRate,String xqType) {
		super(billNo, status, tokenId, processName, submitType, attn,
				applyDate, budgetid, maintDivision, maintStation,
				maintContractNo, companyId, contractYears, standardQuoteTotal,
				finallyQuoteTotal, businessCosts, priceFluctuaApply,
				businessCostsApply, paymentMethodApply, specialApplication,
				contractContentApply, isPass, operId, operDate, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10, historyBillNo, histContractNo,histContractStatus,
				customerArea, companyName,  contacts,  contactPhone, paymentMethodRem, contractContentRem,
				workisdisplay,quoteSignWay,discountRate,xqType);
	}

}
