package com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractEntrustContractMaster entity provides the base persistence definition
 * of the EntrustContractMaster entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractEntrustContractMaster implements
		java.io.Serializable {

	// Fields

	private String billNo;
	private String maintBillNo;
	private String companyId;
	private String companyId2;
	private String entrustContractNo;
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
	private String auditOperid;
	private String auditStatus;
	private String auditDate;
	private String auditRem;
	private String operId;
	private String operDate;
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
	private String maintStation;
	private Set entrustContractDetails = new HashSet(0);
	private String quoteBillNo;
	private String workisdisplay;
	private String workisdisplay2;
	private String endDate;
	private String endOperId;
	private String endOperDate;
	private String tbDate;
	private String tbOperId;
	private String tbOperDate;
	private String modifyId;
	private String modifyDate;
	private Double oldContractTotal;
	private Double oldOtherFee;
	
	public Double getOldContractTotal() {
		return oldContractTotal==null?0:oldContractTotal;
	}

	public void setOldContractTotal(Double oldContractTotal) {
		this.oldContractTotal = oldContractTotal;
	}

	public Double getOldOtherFee() {
		return oldOtherFee==null?0:oldOtherFee;
	}

	public void setOldOtherFee(Double oldOtherFee) {
		this.oldOtherFee = oldOtherFee;
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
	// Constructors

	public String getTbDate() {
		return tbDate;
	}

	public void setTbDate(String tbDate) {
		this.tbDate = tbDate;
	}

	public String getTbOperId() {
		return tbOperId;
	}

	public void setTbOperId(String tbOperId) {
		this.tbOperId = tbOperId;
	}

	public String getTbOperDate() {
		return tbOperDate;
	}

	public void setTbOperDate(String tbOperDate) {
		this.tbOperDate = tbOperDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndOperId() {
		return endOperId;
	}

	public void setEndOperId(String endOperId) {
		this.endOperId = endOperId;
	}

	public String getEndOperDate() {
		return endOperDate;
	}

	public void setEndOperDate(String endOperDate) {
		this.endOperDate = endOperDate;
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

	/** default constructor */
	public AbstractEntrustContractMaster() {
	}

	/** minimal constructor */
	public AbstractEntrustContractMaster(String billNo, String maintBillNo,
			String companyId, String companyId2, String entrustContractNo,
			String maintContractNo, String contractNatureOf, String mainMode,
			String contractPeriod, String contractSdate, String contractEdate,
			Double contractTotal, String attn, String maintDivision,
			String operId, String operDate, String submitType,String quoteBillNo) {
		this.billNo = billNo;
		this.maintBillNo = maintBillNo;
		this.companyId = companyId;
		this.companyId2 = companyId2;
		this.entrustContractNo = entrustContractNo;
		this.maintContractNo = maintContractNo;
		this.contractNatureOf = contractNatureOf;
		this.mainMode = mainMode;
		this.contractPeriod = contractPeriod;
		this.contractSdate = contractSdate;
		this.contractEdate = contractEdate;
		this.contractTotal = contractTotal;
		this.attn = attn;
		this.maintDivision = maintDivision;
		this.operId = operId;
		this.operDate = operDate;
		this.submitType = submitType;
		this.quoteBillNo=quoteBillNo;
	}

	/** full constructor */
	public AbstractEntrustContractMaster(String billNo, String maintBillNo,
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
			String modifyId,String modifyDate,Double oldContractTotal,Double oldOtherFee) {
		this.billNo = billNo;
		this.maintBillNo = maintBillNo;
		this.companyId = companyId;
		this.companyId2 = companyId2;
		this.entrustContractNo = entrustContractNo;
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
		this.auditOperid = auditOperid;
		this.auditStatus = auditStatus;
		this.auditDate = auditDate;
		this.auditRem = auditRem;
		this.operId = operId;
		this.operDate = operDate;
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
		this.maintStation = maintStation;
		this.entrustContractDetails = entrustContractDetails;
		this.quoteBillNo=quoteBillNo;
		this.workisdisplay=workisdisplay;
		this.workisdisplay2=workisdisplay2;
		this.endDate=endDate;
		this.endOperId=endOperId;
		this.endOperDate=endOperDate;
		this.tbDate=tbDate;
		this.tbOperId=tbOperId;
		this.tbOperDate=tbOperDate;
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

	public String getMaintBillNo() {
		return this.maintBillNo;
	}

	public void setMaintBillNo(String maintBillNo) {
		this.maintBillNo = maintBillNo;
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

	public String getEntrustContractNo() {
		return this.entrustContractNo;
	}

	public void setEntrustContractNo(String entrustContractNo) {
		this.entrustContractNo = entrustContractNo;
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

	public String getMaintStation() {
		return this.maintStation;
	}

	public void setMaintStation(String maintStation) {
		this.maintStation = maintStation;
	}

	public Set getEntrustContractDetails() {
		return this.entrustContractDetails;
	}

	public void setEntrustContractDetails(Set entrustContractDetails) {
		this.entrustContractDetails = entrustContractDetails;
	}

	public String getQuoteBillNo() {
		return quoteBillNo;
	}

	public void setQuoteBillNo(String quoteBillNo) {
		this.quoteBillNo = quoteBillNo;
	}

}