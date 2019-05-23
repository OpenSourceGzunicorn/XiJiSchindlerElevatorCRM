package com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster;

/**
 * AbstractMaintContractMaster entity provides the base persistence definition
 * of the MaintContractMaster entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractMaintContractMaster implements
		java.io.Serializable {

	// Fields

	private String billNo;
	private String quoteBillNo;
	private String companyId;
	private String companyId2;
	private String maintContractNo;
	private String contractNatureOf;
	private String mainMode;
	private String contractPeriod;
	private String contractSdate;
	private String contractEdate;
	private Double contractTotal;
	private Double otherFee;
	private String contractTerms;
	private String paymentMethod;
	private String attn;
	private String maintDivision;
	private String maintStation;
	private String auditOperid;
	private String auditStatus;
	private String auditDate;
	private String auditRem;
	private String operId;
	private String operDate;
	private String taskUserId;
	private String taskSubFlag;
	private String taskSubDate;
	private String taskRem;
	private String surrenderUser;
	private String surrenderDate;
	private String surrenderRem;
	private String contractStatus;
	private String submitType;
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
	private String warningStatus;
	private String histContractNo;
	private String histContractStatus;
	private String workisdisplay;
	private String workisdisplay2;
	private String paymentMethodRem;
	private String rem;
	private String contractContentRem;
	private String modifyId;
	private String modifyDate;
	private Double oldContractTotal;
	private Double oldOtherFee;
	private String quoteSignWay;
	private String xqType;
	private String customizeRem;
	
	public String getCustomizeRem() {
		return customizeRem;
	}

	public void setCustomizeRem(String customizeRem) {
		this.customizeRem = customizeRem;
	}

	public String getXqType() {
		return xqType;
	}

	public void setXqType(String xqType) {
		this.xqType = xqType;
	}

	public String getQuoteSignWay() {
		return quoteSignWay;
	}

	public void setQuoteSignWay(String quoteSignWay) {
		this.quoteSignWay = quoteSignWay;
	}

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Double getOldContractTotal() {
		return oldContractTotal;
	}

	public void setOldContractTotal(Double oldContractTotal) {
		this.oldContractTotal = oldContractTotal;
	}

	public Double getOldOtherFee() {
		return oldOtherFee;
	}

	public void setOldOtherFee(Double oldOtherFee) {
		this.oldOtherFee = oldOtherFee;
	}

	public String getContractContentRem() {
		return contractContentRem;
	}

	public void setContractContentRem(String contractContentRem) {
		this.contractContentRem = contractContentRem;
	}

	// Constructors
	public String getPaymentMethodRem() {
		return paymentMethodRem;
	}

	public void setPaymentMethodRem(String paymentMethodRem) {
		this.paymentMethodRem = paymentMethodRem;
	}

	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getWorkisdisplay2() {
		return workisdisplay2;
	}

	public void setWorkisdisplay2(String workisdisplay2) {
		this.workisdisplay2 = workisdisplay2;
	}

	public String getWorkisdisplay() {
		return workisdisplay;
	}

	public void setWorkisdisplay(String workisdisplay) {
		this.workisdisplay = workisdisplay;
	}

	public String getHistContractStatus() {
		return histContractStatus;
	}

	public void setHistContractStatus(String histContractStatus) {
		this.histContractStatus = histContractStatus;
	}

	/** default constructor */
	public AbstractMaintContractMaster() {
	}

	/** minimal constructor */
	public AbstractMaintContractMaster(String billNo, String companyId,
			String companyId2, String maintContractNo, String contractNatureOf,
			String mainMode, String contractPeriod, String contractSdate,
			String contractEdate, Double contractTotal, String attn,
			String maintDivision, String maintStation, String operId,
			String operDate, String contractStatus, String submitType) {
		this.billNo = billNo;
		this.companyId = companyId;
		this.companyId2 = companyId2;
		this.maintContractNo = maintContractNo;
		this.contractNatureOf = contractNatureOf;
		this.mainMode = mainMode;
		this.contractPeriod = contractPeriod;
		this.contractSdate = contractSdate;
		this.contractEdate = contractEdate;
		this.contractTotal = contractTotal;
		this.attn = attn;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.operId = operId;
		this.operDate = operDate;
		this.contractStatus = contractStatus;
		this.submitType = submitType;
	}

	/** full constructor */
	public AbstractMaintContractMaster(String billNo, String quoteBillNo,
			String companyId, String companyId2, String maintContractNo,
			String contractNatureOf, String mainMode, String contractPeriod,
			String contractSdate, String contractEdate, Double contractTotal,
			Double otherFee, String contractTerms, String paymentMethod,
			String attn, String maintDivision, String maintStation,
			String auditOperid, String auditStatus, String auditDate,
			String auditRem, String operId, String operDate, String taskUserId,
			String taskSubFlag, String taskSubDate, String taskRem,
			String surrenderUser, String surrenderDate, String surrenderRem,
			String contractStatus, String submitType, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10, String historyBillNo,
			String warningStatus, String histContractNo,String histContractStatus,
			String workisdisplay,String workisdisplay2,
			String paymentMethodRem,String contractContentRem,String rem,
			String modifyId,String modifyDate,Double oldContractTotal,Double oldOtherFee,
			String quoteSignWay,String xqType,String customizeRem) {
		this.customizeRem=customizeRem;
		this.xqType=xqType;
		this.quoteSignWay=quoteSignWay;
		this.billNo = billNo;
		this.quoteBillNo = quoteBillNo;
		this.companyId = companyId;
		this.companyId2 = companyId2;
		this.maintContractNo = maintContractNo;
		this.contractNatureOf = contractNatureOf;
		this.mainMode = mainMode;
		this.contractPeriod = contractPeriod;
		this.contractSdate = contractSdate;
		this.contractEdate = contractEdate;
		this.contractTotal = contractTotal;
		this.otherFee = otherFee;
		this.contractTerms = contractTerms;
		this.paymentMethod = paymentMethod;
		this.attn = attn;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.auditOperid = auditOperid;
		this.auditStatus = auditStatus;
		this.auditDate = auditDate;
		this.auditRem = auditRem;
		this.operId = operId;
		this.operDate = operDate;
		this.taskUserId = taskUserId;
		this.taskSubFlag = taskSubFlag;
		this.taskSubDate = taskSubDate;
		this.taskRem = taskRem;
		this.surrenderUser = surrenderUser;
		this.surrenderDate = surrenderDate;
		this.surrenderRem = surrenderRem;
		this.contractStatus = contractStatus;
		this.submitType = submitType;
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
		this.warningStatus = warningStatus;
		this.histContractNo = histContractNo;
		this.histContractStatus = histContractStatus;
		this.workisdisplay = workisdisplay;
		this.workisdisplay2 = workisdisplay2;
		this.paymentMethodRem=paymentMethodRem;
		this.contractContentRem=contractContentRem;
		this.rem=rem;
		this.modifyId=modifyId;
		this.modifyDate=modifyDate;
		this.oldContractTotal=oldContractTotal;
		this.oldOtherFee=oldOtherFee;
	}

	// Property accessors

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getQuoteBillNo() {
		return this.quoteBillNo;
	}

	public void setQuoteBillNo(String quoteBillNo) {
		this.quoteBillNo = quoteBillNo;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyId2() {
		return this.companyId2;
	}

	public void setCompanyId2(String companyId2) {
		this.companyId2 = companyId2;
	}

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public String getContractNatureOf() {
		return this.contractNatureOf;
	}

	public void setContractNatureOf(String contractNatureOf) {
		this.contractNatureOf = contractNatureOf;
	}

	public String getMainMode() {
		return this.mainMode;
	}

	public void setMainMode(String mainMode) {
		this.mainMode = mainMode;
	}

	public String getContractPeriod() {
		return this.contractPeriod;
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public String getContractSdate() {
		return this.contractSdate;
	}

	public void setContractSdate(String contractSdate) {
		this.contractSdate = contractSdate;
	}

	public String getContractEdate() {
		return this.contractEdate;
	}

	public void setContractEdate(String contractEdate) {
		this.contractEdate = contractEdate;
	}

	public Double getContractTotal() {
		return this.contractTotal;
	}

	public void setContractTotal(Double contractTotal) {
		this.contractTotal = contractTotal;
	}

	public Double getOtherFee() {
		return this.otherFee;
	}

	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	public String getContractTerms() {
		return this.contractTerms;
	}

	public void setContractTerms(String contractTerms) {
		this.contractTerms = contractTerms;
	}

	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getAttn() {
		return this.attn;
	}

	public void setAttn(String attn) {
		this.attn = attn;
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

	public String getAuditOperid() {
		return this.auditOperid;
	}

	public void setAuditOperid(String auditOperid) {
		this.auditOperid = auditOperid;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditRem() {
		return this.auditRem;
	}

	public void setAuditRem(String auditRem) {
		this.auditRem = auditRem;
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

	public String getTaskUserId() {
		return this.taskUserId;
	}

	public void setTaskUserId(String taskUserId) {
		this.taskUserId = taskUserId;
	}

	public String getTaskSubFlag() {
		return this.taskSubFlag;
	}

	public void setTaskSubFlag(String taskSubFlag) {
		this.taskSubFlag = taskSubFlag;
	}

	public String getTaskSubDate() {
		return this.taskSubDate;
	}

	public void setTaskSubDate(String taskSubDate) {
		this.taskSubDate = taskSubDate;
	}

	public String getTaskRem() {
		return this.taskRem;
	}

	public void setTaskRem(String taskRem) {
		this.taskRem = taskRem;
	}

	public String getSurrenderUser() {
		return this.surrenderUser;
	}

	public void setSurrenderUser(String surrenderUser) {
		this.surrenderUser = surrenderUser;
	}

	public String getSurrenderDate() {
		return this.surrenderDate;
	}

	public void setSurrenderDate(String surrenderDate) {
		this.surrenderDate = surrenderDate;
	}

	public String getSurrenderRem() {
		return this.surrenderRem;
	}

	public void setSurrenderRem(String surrenderRem) {
		this.surrenderRem = surrenderRem;
	}

	public String getContractStatus() {
		return this.contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
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

	public String getWarningStatus() {
		return this.warningStatus;
	}

	public void setWarningStatus(String warningStatus) {
		this.warningStatus = warningStatus;
	}

	public String getHistContractNo() {
		return this.histContractNo;
	}

	public void setHistContractNo(String histContractNo) {
		this.histContractNo = histContractNo;
	}

}