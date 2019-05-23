package com.gzunicorn.hibernate.engcontractmanager.outsourcecontractmaster;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractOutsourceContractMaster entity provides the base persistence
 * definition of the OutsourceContractMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractOutsourceContractMaster implements
		java.io.Serializable {

	// Fields

	private String billno;
	private String wgBillno;
	private String companyId;
	private String companyId2;
	private String outContractNo;
	private String maintContractNo;
	private String busType;
	private String signingDate;
	private String contractTerms;
	private String paymentMethod;
	private Double contractTotal;
	private Double otherFee;
	private String attn;
	private String maintDivision;
	private String maintStation;
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
	private Set outsourceContractDetails = new HashSet(0);
	private String quoteBillNo;
	private String workisdisplay;
	public String getWorkisdisplay() {
		return workisdisplay;
	}

	public void setWorkisdisplay(String workisdisplay) {
		this.workisdisplay = workisdisplay;
	}

	public String getWorkisdisplay2() {
		return workisdisplay2;
	}

	public void setWorkisdisplay2(String workisdisplay2) {
		this.workisdisplay2 = workisdisplay2;
	}

	private String workisdisplay2;

	// Constructors

	/** default constructor */
	public AbstractOutsourceContractMaster() {
	}

	/** minimal constructor */
	public AbstractOutsourceContractMaster(String billno, String wgBillno,
			String companyId, String companyId2, String outContractNo,
			String maintContractNo, String busType, String signingDate,
			Double contractTotal, String attn, String maintDivision,
			String maintStation, String operId, String operDate,
			String submitType,String quoteBillNo) {
		this.billno = billno;
		this.wgBillno = wgBillno;
		this.companyId = companyId;
		this.companyId2 = companyId2;
		this.outContractNo = outContractNo;
		this.maintContractNo = maintContractNo;
		this.busType = busType;
		this.signingDate = signingDate;
		this.contractTotal = contractTotal;
		this.attn = attn;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.operId = operId;
		this.operDate = operDate;
		this.submitType = submitType;
		this.quoteBillNo=quoteBillNo;
	}

	/** full constructor */
	public AbstractOutsourceContractMaster(String billno, String wgBillno,
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
		this.billno = billno;
		this.wgBillno = wgBillno;
		this.companyId = companyId;
		this.companyId2 = companyId2;
		this.outContractNo = outContractNo;
		this.maintContractNo = maintContractNo;
		this.busType = busType;
		this.signingDate = signingDate;
		this.contractTerms = contractTerms;
		this.paymentMethod = paymentMethod;
		this.contractTotal = contractTotal;
		this.otherFee = otherFee;
		this.attn = attn;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
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
		this.outsourceContractDetails = outsourceContractDetails;
		this.quoteBillNo=quoteBillNo;
		this.workisdisplay=workisdisplay;
		this.workisdisplay2=workisdisplay2;
	}

	// Property accessors

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getWgBillno() {
		return this.wgBillno;
	}

	public void setWgBillno(String wgBillno) {
		this.wgBillno = wgBillno;
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

	public String getOutContractNo() {
		return this.outContractNo;
	}

	public void setOutContractNo(String outContractNo) {
		this.outContractNo = outContractNo;
	}

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public String getBusType() {
		return this.busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getSigningDate() {
		return this.signingDate;
	}

	public void setSigningDate(String signingDate) {
		this.signingDate = signingDate;
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

	public Set getOutsourceContractDetails() {
		return this.outsourceContractDetails;
	}

	public void setOutsourceContractDetails(Set outsourceContractDetails) {
		this.outsourceContractDetails = outsourceContractDetails;
	}

	public String getQuoteBillNo() {
		return quoteBillNo;
	}

	public void setQuoteBillNo(String quoteBillNo) {
		this.quoteBillNo = quoteBillNo;
	}

}