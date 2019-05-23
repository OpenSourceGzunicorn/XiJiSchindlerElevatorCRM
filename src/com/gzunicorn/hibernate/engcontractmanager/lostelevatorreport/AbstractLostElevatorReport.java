package com.gzunicorn.hibernate.engcontractmanager.lostelevatorreport;

/**
 * AbstractLostElevatorReport entity provides the base persistence definition of
 * the LostElevatorReport entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractLostElevatorReport implements
		java.io.Serializable {

	// Fields

	private String jnlno;
	private String billNo;
	private String maintDivision;
	private String maintStation;
	private String maintContractNo;
	private String contractNatureOf;
	private String projectName;
	private Integer eleNum;
	private String lostElevatorDate;
	private String causeAnalysis;
	private String contacts;
	private String contactPhone;
	private String detailedRem;
	private String competeCompany;
	private String recoveryPlan;
	private String submitType;
	private String auditOperid;
	private String auditStatus;
	private String auditDate;
	private String auditRem;
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
	private String auditOperid2;
	private String auditStatus2;
	private String auditDate2;
	private String auditRem2;

	private String workisdisplay;
	private String workisdisplay2;
	private String workisdisplay3;

	private String isCharge;
	private String auditOperid3;
	private String auditStatus3;
	private String auditDate3;
	private String auditRem3;
	private String fhRem;
	private String hfOperid;
	private String hfDate;
	private String hfRemLast;
	// Constructors

	public String getHfRemLast() {
		return hfRemLast;
	}

	public void setHfRemLast(String hfRemLast) {
		this.hfRemLast = hfRemLast;
	}

	public String getHfOperid() {
		return hfOperid;
	}

	public void setHfOperid(String hfOperid) {
		this.hfOperid = hfOperid;
	}

	public String getHfDate() {
		return hfDate;
	}

	public void setHfDate(String hfDate) {
		this.hfDate = hfDate;
	}

	public String getFhRem() {
		return fhRem;
	}

	public void setFhRem(String fhRem) {
		this.fhRem = fhRem;
	}

	public String getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(String isCharge) {
		this.isCharge = isCharge;
	}

	public String getAuditOperid3() {
		return auditOperid3;
	}

	public void setAuditOperid3(String auditOperid3) {
		this.auditOperid3 = auditOperid3;
	}

	public String getAuditStatus3() {
		return auditStatus3;
	}

	public void setAuditStatus3(String auditStatus3) {
		this.auditStatus3 = auditStatus3;
	}

	public String getAuditDate3() {
		return auditDate3;
	}

	public void setAuditDate3(String auditDate3) {
		this.auditDate3 = auditDate3;
	}

	public String getAuditRem3() {
		return auditRem3;
	}

	public void setAuditRem3(String auditRem3) {
		this.auditRem3 = auditRem3;
	}

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

	public String getWorkisdisplay3() {
		return workisdisplay3;
	}

	public void setWorkisdisplay3(String workisdisplay3) {
		this.workisdisplay3 = workisdisplay3;
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
	public AbstractLostElevatorReport() {
	}

	/** minimal constructor */
	public AbstractLostElevatorReport(String jnlno, String billNo,
			String maintDivision, String maintStation, String maintContractNo,
			String contractNatureOf, String projectName, Integer eleNum,
			String lostElevatorDate, String causeAnalysis, String submitType,
			String operId, String operDate) {
		this.jnlno = jnlno;
		this.billNo = billNo;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.maintContractNo = maintContractNo;
		this.contractNatureOf = contractNatureOf;
		this.projectName = projectName;
		this.eleNum = eleNum;
		this.lostElevatorDate = lostElevatorDate;
		this.causeAnalysis = causeAnalysis;
		this.submitType = submitType;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractLostElevatorReport(String jnlno, String billNo,
			String maintDivision, String maintStation, String maintContractNo,
			String contractNatureOf, String projectName, Integer eleNum,
			String lostElevatorDate, String causeAnalysis, String contacts,
			String contactPhone, String detailedRem, String competeCompany,
			String recoveryPlan, String submitType, String auditOperid,
			String auditStatus, String auditDate, String auditRem,
			String operId, String operDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String auditOperid2,String auditStatus2, String auditDate2, String auditRem2,
			String workisdisplay,String workisdisplay2,String workisdisplay3,
			String isCharge, String auditOperid3,String auditStatus3, String auditDate3, String auditRem3,
			String hfOperid,String hfDate,String fhRem,String hfRemLast) {
		this.hfRemLast=hfRemLast;
		this.hfOperid=hfOperid;
		this.hfDate=hfDate;
		this.fhRem=fhRem;
		this.jnlno = jnlno;
		this.billNo = billNo;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.maintContractNo = maintContractNo;
		this.contractNatureOf = contractNatureOf;
		this.projectName = projectName;
		this.eleNum = eleNum;
		this.lostElevatorDate = lostElevatorDate;
		this.causeAnalysis = causeAnalysis;
		this.contacts = contacts;
		this.contactPhone = contactPhone;
		this.detailedRem = detailedRem;
		this.competeCompany = competeCompany;
		this.recoveryPlan = recoveryPlan;
		this.submitType = submitType;
		this.auditOperid = auditOperid;
		this.auditStatus = auditStatus;
		this.auditDate = auditDate;
		this.auditRem = auditRem;
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
		this.auditOperid2 = auditOperid2;
		this.auditStatus2 = auditStatus2;
		this.auditDate2 = auditDate2;
		this.auditRem2 = auditRem2;
		this.isCharge = isCharge;
		this.auditOperid3 = auditOperid3;
		this.auditStatus3 = auditStatus3;
		this.auditDate3 = auditDate3;
		this.auditRem3 = auditRem3;

		this.workisdisplay = workisdisplay;
		this.workisdisplay2 = workisdisplay2;
		this.workisdisplay3 = workisdisplay3;
	}

	// Property accessors

	public String getJnlno() {
		return this.jnlno;
	}

	public void setJnlno(String jnlno) {
		this.jnlno = jnlno;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
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

	public String getContractNatureOf() {
		return this.contractNatureOf;
	}

	public void setContractNatureOf(String contractNatureOf) {
		this.contractNatureOf = contractNatureOf;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getEleNum() {
		return this.eleNum;
	}

	public void setEleNum(Integer eleNum) {
		this.eleNum = eleNum;
	}

	public String getLostElevatorDate() {
		return this.lostElevatorDate;
	}

	public void setLostElevatorDate(String lostElevatorDate) {
		this.lostElevatorDate = lostElevatorDate;
	}

	public String getCauseAnalysis() {
		return this.causeAnalysis;
	}

	public void setCauseAnalysis(String causeAnalysis) {
		this.causeAnalysis = causeAnalysis;
	}

	public String getContacts() {
		return this.contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getDetailedRem() {
		return this.detailedRem;
	}

	public void setDetailedRem(String detailedRem) {
		this.detailedRem = detailedRem;
	}

	public String getCompeteCompany() {
		return this.competeCompany;
	}

	public void setCompeteCompany(String competeCompany) {
		this.competeCompany = competeCompany;
	}

	public String getRecoveryPlan() {
		return this.recoveryPlan;
	}

	public void setRecoveryPlan(String recoveryPlan) {
		this.recoveryPlan = recoveryPlan;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
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