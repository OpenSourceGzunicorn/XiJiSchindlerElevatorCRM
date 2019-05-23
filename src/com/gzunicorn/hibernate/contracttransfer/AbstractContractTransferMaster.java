package com.gzunicorn.hibernate.contracttransfer;

/**
 * AbstractContractTransferMaster entity provides the base persistence
 * definition of the ContractTransferMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractContractTransferMaster implements
		java.io.Serializable {

	// Fields

	private String billNo;
	private String companyId;
	private String maintContractNo;
	private String salesContractNo;
	private String elevatorNo;
	private String maintDivision;
	private String maintStation;
	private String contractSdate;
	private String contractEdate;
	private String submitType;
	private String operId;
	private String operDate;
	private String transfeSubmitType;
	private String transfeId;
	private String transfeDate;
	private String transferRem;
	private String auditOperid;
	private String auditStatus;
	private String auditDate;
	private String auditRem;
	private String transferComplete;
	private String workisdisplay;
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
	private String isTrans;
	private String wbgTransfeId;
	private String auditOperid2;
	private String auditStatus2;
	private String auditDate2;
	private String auditRem2;
	private String isTransDate;
	private String isTransId;

	// Constructors

	public String getIsTransId() {
		return isTransId;
	}

	public void setIsTransId(String isTransId) {
		this.isTransId = isTransId;
	}

	public String getIsTransDate() {
		return isTransDate;
	}

	public void setIsTransDate(String isTransDate) {
		this.isTransDate = isTransDate;
	}

	public String getIsTrans() {
		return isTrans;
	}

	public void setIsTrans(String isTrans) {
		this.isTrans = isTrans;
	}

	public String getWbgTransfeId() {
		return wbgTransfeId;
	}

	public void setWbgTransfeId(String wbgTransfeId) {
		this.wbgTransfeId = wbgTransfeId;
	}

	public String getAuditOperid2() {
		return auditOperid2;
	}

	public void setAuditOperid2(String auditOperid2) {
		this.auditOperid2 = auditOperid2;
	}

	public String getAuditStatus2() {
		return auditStatus2;
	}

	public void setAuditStatus2(String auditStatus2) {
		this.auditStatus2 = auditStatus2;
	}

	public String getAuditDate2() {
		return auditDate2;
	}

	public void setAuditDate2(String auditDate2) {
		this.auditDate2 = auditDate2;
	}

	public String getAuditRem2() {
		return auditRem2;
	}

	public void setAuditRem2(String auditRem2) {
		this.auditRem2 = auditRem2;
	}

	/** default constructor */
	public AbstractContractTransferMaster() {
	}

	/** minimal constructor */
	public AbstractContractTransferMaster(String billNo, String companyId,
			String maintContractNo, String salesContractNo, String elevatorNo,
			String maintDivision, String maintStation, String contractSdate,
			String contractEdate, String submitType, String operId,
			String operDate) {
		this.billNo = billNo;
		this.companyId = companyId;
		this.maintContractNo = maintContractNo;
		this.salesContractNo = salesContractNo;
		this.elevatorNo = elevatorNo;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.contractSdate = contractSdate;
		this.contractEdate = contractEdate;
		this.submitType = submitType;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractContractTransferMaster(String billNo, String companyId,
			String maintContractNo, String salesContractNo, String elevatorNo,
			String maintDivision, String maintStation, String contractSdate,
			String contractEdate, String submitType, String operId,
			String operDate, String transfeSubmitType, String transfeId,
			String transfeDate, String transferRem, String auditOperid,
			String auditStatus, String auditDate, String auditRem,
			String transferComplete, String workisdisplay, String r1,
			String r2, String r3, String r4, String r5, Double r6, Double r7,
			Double r8, Integer r9, Integer r10,
			String isTrans, String wbgTransfeId, String auditOperid2, 
			String auditStatus2,String auditDate2, String auditRem2,String isTransId,String isTransDate) {
		this.isTrans=isTrans;
		this.isTransId=isTransId;
		this.isTransDate=isTransDate;
		this.wbgTransfeId=wbgTransfeId;
		this.auditOperid2=auditOperid2;
		this.auditStatus2=auditStatus2;
		this.auditDate2=auditDate2;
		this.auditRem2=auditRem2;
		this.billNo = billNo;
		this.companyId = companyId;
		this.maintContractNo = maintContractNo;
		this.salesContractNo = salesContractNo;
		this.elevatorNo = elevatorNo;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.contractSdate = contractSdate;
		this.contractEdate = contractEdate;
		this.submitType = submitType;
		this.operId = operId;
		this.operDate = operDate;
		this.transfeSubmitType = transfeSubmitType;
		this.transfeId = transfeId;
		this.transfeDate = transfeDate;
		this.transferRem = transferRem;
		this.auditOperid = auditOperid;
		this.auditStatus = auditStatus;
		this.auditDate = auditDate;
		this.auditRem = auditRem;
		this.transferComplete = transferComplete;
		this.workisdisplay = workisdisplay;
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
	}

	// Property accessors

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

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public String getSalesContractNo() {
		return this.salesContractNo;
	}

	public void setSalesContractNo(String salesContractNo) {
		this.salesContractNo = salesContractNo;
	}

	public String getElevatorNo() {
		return this.elevatorNo;
	}

	public void setElevatorNo(String elevatorNo) {
		this.elevatorNo = elevatorNo;
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

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
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

	public String getTransfeSubmitType() {
		return this.transfeSubmitType;
	}

	public void setTransfeSubmitType(String transfeSubmitType) {
		this.transfeSubmitType = transfeSubmitType;
	}

	public String getTransfeId() {
		return this.transfeId;
	}

	public void setTransfeId(String transfeId) {
		this.transfeId = transfeId;
	}

	public String getTransfeDate() {
		return this.transfeDate;
	}

	public void setTransfeDate(String transfeDate) {
		this.transfeDate = transfeDate;
	}

	public String getTransferRem() {
		return this.transferRem;
	}

	public void setTransferRem(String transferRem) {
		this.transferRem = transferRem;
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

	public String getTransferComplete() {
		return this.transferComplete;
	}

	public void setTransferComplete(String transferComplete) {
		this.transferComplete = transferComplete;
	}

	public String getWorkisdisplay() {
		return this.workisdisplay;
	}

	public void setWorkisdisplay(String workisdisplay) {
		this.workisdisplay = workisdisplay;
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

}