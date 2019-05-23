package com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster;

/**
 * AbstractMaintContractQuoteMaster entity provides the base persistence
 * definition of the MaintContractQuoteMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractMaintContractQuoteMaster implements
		java.io.Serializable {

	// Fields

	private String billNo;
	private Integer status;
	private Long tokenId;
	private String processName;
	private String submitType;
	private String attn;
	private String applyDate;
	private String budgetid;
	private String maintDivision;
	private String maintStation;
	private String maintContractNo;
	private String companyId;
	private Double contractYears;
	private Double standardQuoteTotal;
	private Double finallyQuoteTotal;
	private Double businessCosts;
	private String priceFluctuaApply;
	private String businessCostsApply;
	private String paymentMethodApply;
	private String specialApplication;
	private String contractContentApply;
	private String isPass;
	private String operId;
	private String operDate;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private Double r6;
	private Double r7;
	private Double r8;
	private Integer r9;
	private Integer r10;
	private String historyBillNo;
	private String histContractNo;
	private String histContractStatus;
	private String customerArea;
	private String companyName;
	private String contacts;
	private String contactPhone;
	private String paymentMethodRem;
	private String workisdisplay;
	private String quoteSignWay;
	private Double discountRate;
	private String xqType;
	


	public String getXqType() {
		return xqType;
	}

	public void setXqType(String xqType) {
		this.xqType = xqType;
	}

	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public String getQuoteSignWay() {
		return quoteSignWay;
	}

	public void setQuoteSignWay(String quoteSignWay) {
		this.quoteSignWay = quoteSignWay;
	}

	public String getWorkisdisplay() {
		return workisdisplay;
	}

	public void setWorkisdisplay(String workisdisplay) {
		this.workisdisplay = workisdisplay;
	}

	public String getPaymentMethodRem() {
		return paymentMethodRem;
	}

	public void setPaymentMethodRem(String paymentMethodRem) {
		this.paymentMethodRem = paymentMethodRem;
	}

	public String getContractContentRem() {
		return contractContentRem;
	}

	public void setContractContentRem(String contractContentRem) {
		this.contractContentRem = contractContentRem;
	}

	private String contractContentRem;

	// Constructors

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	/** default constructor */
	public AbstractMaintContractQuoteMaster() {
	}

	/** minimal constructor */
	public AbstractMaintContractQuoteMaster(String billNo, Integer status,
			Long tokenId, String processName, String submitType, String attn,
			String applyDate, String maintDivision, String maintStation,
			String companyId, Double contractYears, Double standardQuoteTotal,
			Double finallyQuoteTotal, Double businessCosts, String operId,
			String operDate) {
		this.billNo = billNo;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.submitType = submitType;
		this.attn = attn;
		this.applyDate = applyDate;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.companyId = companyId;
		this.contractYears = contractYears;
		this.standardQuoteTotal = standardQuoteTotal;
		this.finallyQuoteTotal = finallyQuoteTotal;
		this.businessCosts = businessCosts;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractMaintContractQuoteMaster(String billNo, Integer status,
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
			String paymentMethodRem,String contractContentRem,String workisdisplay,String quoteSignWay,Double discountRate,
			String xqType) {
		this.xqType=xqType;
		this.discountRate=discountRate;
		this.quoteSignWay=quoteSignWay;
		this.billNo = billNo;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.submitType = submitType;
		this.attn = attn;
		this.applyDate = applyDate;
		this.budgetid = budgetid;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.maintContractNo = maintContractNo;
		this.companyId = companyId;
		this.contractYears = contractYears;
		this.standardQuoteTotal = standardQuoteTotal;
		this.finallyQuoteTotal = finallyQuoteTotal;
		this.businessCosts = businessCosts;
		this.priceFluctuaApply = priceFluctuaApply;
		this.businessCostsApply = businessCostsApply;
		this.paymentMethodApply = paymentMethodApply;
		this.specialApplication = specialApplication;
		this.contractContentApply = contractContentApply;
		this.isPass = isPass;
		this.operId = operId;
		this.operDate = operDate;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.r4 = r4;
		this.r5 = r5;
		this.r6 = r6;
		this.r7 = r7;
		this.r8 = r8;
		this.r9 = r9;
		this.r10 = r10;
		this.historyBillNo = historyBillNo;
		this.histContractNo = histContractNo;
		this.histContractStatus = histContractStatus;
		this.customerArea = customerArea;
		this.companyName=companyName;  
		this.contacts=contacts; 
		this.contactPhone=contactPhone;
		this.paymentMethodRem= paymentMethodRem;
		this.contractContentRem= contractContentRem;
		this.workisdisplay=workisdisplay;
	}

	public String getHistContractStatus() {
		return histContractStatus;
	}

	public void setHistContractStatus(String histContractStatus) {
		this.histContractStatus = histContractStatus;
	}


	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}

	public String getProcessName() {
		return this.processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getAttn() {
		return this.attn;
	}

	public void setAttn(String attn) {
		this.attn = attn;
	}

	public String getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getBudgetid() {
		return this.budgetid;
	}

	public void setBudgetid(String budgetid) {
		this.budgetid = budgetid;
	}

	public String getMaintDivision() {
		return this.maintDivision;
	}

	public void setMaintDivision(String maintDivision) {
		this.maintDivision = maintDivision;
	}

	public String getMaintStation() {
		return this.maintStation;
	}

	public void setMaintStation(String maintStation) {
		this.maintStation = maintStation;
	}

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Double getContractYears() {
		return this.contractYears;
	}

	public void setContractYears(Double contractYears) {
		this.contractYears = contractYears;
	}

	public Double getStandardQuoteTotal() {
		return this.standardQuoteTotal;
	}

	public void setStandardQuoteTotal(Double standardQuoteTotal) {
		this.standardQuoteTotal = standardQuoteTotal;
	}

	public Double getFinallyQuoteTotal() {
		return this.finallyQuoteTotal;
	}

	public void setFinallyQuoteTotal(Double finallyQuoteTotal) {
		this.finallyQuoteTotal = finallyQuoteTotal;
	}

	public Double getBusinessCosts() {
		return this.businessCosts;
	}

	public void setBusinessCosts(Double businessCosts) {
		this.businessCosts = businessCosts;
	}

	public String getPriceFluctuaApply() {
		return this.priceFluctuaApply;
	}

	public void setPriceFluctuaApply(String priceFluctuaApply) {
		this.priceFluctuaApply = priceFluctuaApply;
	}

	public String getBusinessCostsApply() {
		return this.businessCostsApply;
	}

	public void setBusinessCostsApply(String businessCostsApply) {
		this.businessCostsApply = businessCostsApply;
	}

	public String getPaymentMethodApply() {
		return this.paymentMethodApply;
	}

	public void setPaymentMethodApply(String paymentMethodApply) {
		this.paymentMethodApply = paymentMethodApply;
	}

	public String getSpecialApplication() {
		return this.specialApplication;
	}

	public void setSpecialApplication(String specialApplication) {
		this.specialApplication = specialApplication;
	}

	public String getContractContentApply() {
		return this.contractContentApply;
	}

	public void setContractContentApply(String contractContentApply) {
		this.contractContentApply = contractContentApply;
	}

	public String getIsPass() {
		return this.isPass;
	}

	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}

	public String getOperId() {
		return this.operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	public String getOperDate() {
		return this.operDate;
	}

	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}

	public String getR1() {
		return this.r1;
	}

	public void setR1(String r1) {
		this.r1 = r1;
	}

	public String getR2() {
		return this.r2;
	}

	public void setR2(String r2) {
		this.r2 = r2;
	}

	public String getR3() {
		return this.r3;
	}

	public void setR3(String r3) {
		this.r3 = r3;
	}

	public String getR4() {
		return this.r4;
	}

	public void setR4(String r4) {
		this.r4 = r4;
	}

	public String getR5() {
		return this.r5;
	}

	public void setR5(String r5) {
		this.r5 = r5;
	}

	public Double getR6() {
		return this.r6;
	}

	public void setR6(Double r6) {
		this.r6 = r6;
	}

	public Double getR7() {
		return this.r7;
	}

	public void setR7(Double r7) {
		this.r7 = r7;
	}

	public Double getR8() {
		return this.r8;
	}

	public void setR8(Double r8) {
		this.r8 = r8;
	}

	public Integer getR9() {
		return this.r9;
	}

	public void setR9(Integer r9) {
		this.r9 = r9;
	}

	public Integer getR10() {
		return this.r10;
	}

	public void setR10(Integer r10) {
		this.r10 = r10;
	}

	public String getHistoryBillNo() {
		return this.historyBillNo;
	}

	public void setHistoryBillNo(String historyBillNo) {
		this.historyBillNo = historyBillNo;
	}

	public String getHistContractNo() {
		return this.histContractNo;
	}

	public void setHistContractNo(String histContractNo) {
		this.histContractNo = histContractNo;
	}

	public String getCustomerArea() {
		return this.customerArea;
	}

	public void setCustomerArea(String customerArea) {
		this.customerArea = customerArea;
	}

}