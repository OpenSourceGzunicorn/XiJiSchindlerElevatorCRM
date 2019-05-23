package com.gzunicorn.hibernate.engcontractmanager.servicingcontractmaster;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractServicingContractMaster entity provides the base persistence
 * definition of the ServicingContractMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractServicingContractMaster implements
		java.io.Serializable {

	// Fields

	private String wgBillno;
	private String billNo;
	private String companyId;
	private String companyId2;
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
	private String taskUserId;
	private String taskSubFlag;
	private String taskSubDate;
	private String taskRem;
	private String forecastDate;
	private String comfirmDate;
	private String itemUserId;
	private String appWorkDate;
	private String enterArenaDate;
	private String epibolyFlag;
	private String finishFlag;
	private String finishDate;
	private String finishId;
	private String finishRem;
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
	private String submitType;
	private Double basePrice;
	private Set servicingContractDetails = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractServicingContractMaster() {
	}

	/** minimal constructor */
	public AbstractServicingContractMaster(String wgBillno, String billNo,
			String companyId, String companyId2, String maintContractNo,
			String busType, String signingDate, Double contractTotal,
			String attn, String maintDivision, String maintStation,
			String operId, String operDate) {
		this.wgBillno = wgBillno;
		this.billNo = billNo;
		this.companyId = companyId;
		this.companyId2 = companyId2;
		this.maintContractNo = maintContractNo;
		this.busType = busType;
		this.signingDate = signingDate;
		this.contractTotal = contractTotal;
		this.attn = attn;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractServicingContractMaster(String wgBillno, String billNo,
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
		this.wgBillno = wgBillno;
		this.billNo = billNo;
		this.companyId = companyId;
		this.companyId2 = companyId2;
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
		this.taskUserId = taskUserId;
		this.taskSubFlag = taskSubFlag;
		this.taskSubDate = taskSubDate;
		this.taskRem = taskRem;
		this.forecastDate = forecastDate;
		this.comfirmDate = comfirmDate;
		this.itemUserId = itemUserId;
		this.appWorkDate = appWorkDate;
		this.enterArenaDate = enterArenaDate;
		this.epibolyFlag = epibolyFlag;
		this.finishFlag = finishFlag;
		this.finishDate = finishDate;
		this.finishId = finishId;
		this.finishRem = finishRem;
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
		this.submitType = submitType;
		this.basePrice = basePrice;
		this.servicingContractDetails = servicingContractDetails;
	}

	// Property accessors

	public String getWgBillno() {
		return this.wgBillno;
	}

	public void setWgBillno(String wgBillno) {
		this.wgBillno = wgBillno;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
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

	public String getForecastDate() {
		return this.forecastDate;
	}

	public void setForecastDate(String forecastDate) {
		this.forecastDate = forecastDate;
	}

	public String getComfirmDate() {
		return this.comfirmDate;
	}

	public void setComfirmDate(String comfirmDate) {
		this.comfirmDate = comfirmDate;
	}

	public String getItemUserId() {
		return this.itemUserId;
	}

	public void setItemUserId(String itemUserId) {
		this.itemUserId = itemUserId;
	}

	public String getAppWorkDate() {
		return this.appWorkDate;
	}

	public void setAppWorkDate(String appWorkDate) {
		this.appWorkDate = appWorkDate;
	}

	public String getEnterArenaDate() {
		return this.enterArenaDate;
	}

	public void setEnterArenaDate(String enterArenaDate) {
		this.enterArenaDate = enterArenaDate;
	}

	public String getEpibolyFlag() {
		return this.epibolyFlag;
	}

	public void setEpibolyFlag(String epibolyFlag) {
		this.epibolyFlag = epibolyFlag;
	}

	public String getFinishFlag() {
		return this.finishFlag;
	}

	public void setFinishFlag(String finishFlag) {
		this.finishFlag = finishFlag;
	}

	public String getFinishDate() {
		return this.finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getFinishId() {
		return this.finishId;
	}

	public void setFinishId(String finishId) {
		this.finishId = finishId;
	}

	public String getFinishRem() {
		return this.finishRem;
	}

	public void setFinishRem(String finishRem) {
		this.finishRem = finishRem;
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

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public Double getBasePrice() {
		return this.basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Set getServicingContractDetails() {
		return this.servicingContractDetails;
	}

	public void setServicingContractDetails(Set servicingContractDetails) {
		this.servicingContractDetails = servicingContractDetails;
	}

}