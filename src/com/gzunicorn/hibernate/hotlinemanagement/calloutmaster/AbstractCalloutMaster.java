package com.gzunicorn.hibernate.hotlinemanagement.calloutmaster;

/**
 * AbstractCalloutMaster entity provides the base persistence definition of the
 * CalloutMaster entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCalloutMaster implements java.io.Serializable {

	// Fields

	private String calloutMasterNo;
	private String handleStatus;
	private String submitType;
	private String operId;
	private String operDate;
	private String repairTime;
	private String repairMode;
	private String repairUser;
	private String repairTel;
	private String serviceObjects;
	private String companyId;
	private String projectAddress;
	private String salesContractNo;
	private String elevatorNo;
	private String elevatorParam;
	private String maintDivision;
	private String maintStation;
	private String assignObject;
	private String phone;
	private String isTrap;
	private String repairDesc;
	private String auditOperid;
	private String auditDate;
	private String auditRem;
	private String isSendSms;
	private String hfcId;
	private String serviceAppraisal;
	private String fittingSituation;
	private String tollSituation;
	private String visitRem;
	private String isColse;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private Double r6;
	private Integer r7;
	private Integer r8;
	private Double r9;
	private Double r10;
	private String isSendSms2;
	private String projectName;
	
	private String isSubSM;
	private String smAuditOperid;
	private String smAuditDate;
	private String smAuditRem;
	private String stophfRem;
	private String stophfOperid;
	private String stophfDate;
	private String bhAuditRem;

	// Constructors

	public String getBhAuditRem() {
		return bhAuditRem;
	}

	public void setBhAuditRem(String bhAuditRem) {
		this.bhAuditRem = bhAuditRem;
	}

	public String getStophfOperid() {
		return stophfOperid;
	}

	public void setStophfOperid(String stophfOperid) {
		this.stophfOperid = stophfOperid;
	}

	public String getStophfDate() {
		return stophfDate;
	}

	public void setStophfDate(String stophfDate) {
		this.stophfDate = stophfDate;
	}

	public String getStophfRem() {
		return stophfRem;
	}

	public void setStophfRem(String stophfRem) {
		this.stophfRem = stophfRem;
	}

	public String getIsSubSM() {
		return isSubSM;
	}

	public void setIsSubSM(String isSubSM) {
		this.isSubSM = isSubSM;
	}

	public String getSmAuditOperid() {
		return smAuditOperid;
	}

	public void setSmAuditOperid(String smAuditOperid) {
		this.smAuditOperid = smAuditOperid;
	}

	public String getSmAuditDate() {
		return smAuditDate;
	}

	public void setSmAuditDate(String smAuditDate) {
		this.smAuditDate = smAuditDate;
	}

	public String getSmAuditRem() {
		return smAuditRem;
	}

	public void setSmAuditRem(String smAuditRem) {
		this.smAuditRem = smAuditRem;
	}

	/** default constructor */
	public AbstractCalloutMaster() {
	}

	/** minimal constructor */
	public AbstractCalloutMaster(String calloutMasterNo, String handleStatus,
			String submitType, String operId, String operDate,
			String repairTime, String repairMode, String serviceObjects,
			String companyId, String isTrap) {
		this.calloutMasterNo = calloutMasterNo;
		this.handleStatus = handleStatus;
		this.submitType = submitType;
		this.operId = operId;
		this.operDate = operDate;
		this.repairTime = repairTime;
		this.repairMode = repairMode;
		this.serviceObjects = serviceObjects;
		this.companyId = companyId;
		this.isTrap = isTrap;
	}

	/** full constructor */
	public AbstractCalloutMaster(String calloutMasterNo, String handleStatus,
			String submitType, String operId, String operDate,
			String repairTime, String repairMode, String repairUser,
			String repairTel, String serviceObjects, String companyId,
			String projectAddress, String salesContractNo, String elevatorNo,
			String elevatorParam, String maintDivision, String maintStation,
			String assignObject, String phone, String isTrap,
			String repairDesc, String auditOperid, String auditDate,
			String auditRem, String isSendSms, String hfcId,
			String serviceAppraisal, String fittingSituation,
			String tollSituation, String visitRem, String isColse, String r1,
			String r2, String r3, String r4, String r5, Double r6, Integer r7,
			Integer r8, Double r9, Double r10, String isSendSms2,
			String projectName,String isSubSM, String smAuditOperid, String smAuditDate,
			String smAuditRem,String stophfRem,String stophfOperid,String stophfDate,
			String bhAuditRem) {
		this.bhAuditRem=bhAuditRem;
		this.stophfOperid=stophfOperid;
		this.stophfDate=stophfDate;
		this.stophfRem=stophfRem;
		this.isSubSM=isSubSM;
		this.smAuditOperid=smAuditOperid;
		this.smAuditDate=smAuditDate;
		this.smAuditRem=smAuditRem;
		this.calloutMasterNo = calloutMasterNo;
		this.handleStatus = handleStatus;
		this.submitType = submitType;
		this.operId = operId;
		this.operDate = operDate;
		this.repairTime = repairTime;
		this.repairMode = repairMode;
		this.repairUser = repairUser;
		this.repairTel = repairTel;
		this.serviceObjects = serviceObjects;
		this.companyId = companyId;
		this.projectAddress = projectAddress;
		this.salesContractNo = salesContractNo;
		this.elevatorNo = elevatorNo;
		this.elevatorParam = elevatorParam;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.assignObject = assignObject;
		this.phone = phone;
		this.isTrap = isTrap;
		this.repairDesc = repairDesc;
		this.auditOperid = auditOperid;
		this.auditDate = auditDate;
		this.auditRem = auditRem;
		this.isSendSms = isSendSms;
		this.hfcId = hfcId;
		this.serviceAppraisal = serviceAppraisal;
		this.fittingSituation = fittingSituation;
		this.tollSituation = tollSituation;
		this.visitRem = visitRem;
		this.isColse = isColse;
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
		this.isSendSms2 = isSendSms2;
		this.projectName = projectName;
	}

	// Property accessors

	public String getCalloutMasterNo() {
		return this.calloutMasterNo;
	}

	public void setCalloutMasterNo(String calloutMasterNo) {
		this.calloutMasterNo = calloutMasterNo;
	}

	public String getHandleStatus() {
		return this.handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
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

	public String getRepairTime() {
		return this.repairTime;
	}

	public void setRepairTime(String repairTime) {
		this.repairTime = repairTime;
	}

	public String getRepairMode() {
		return this.repairMode;
	}

	public void setRepairMode(String repairMode) {
		this.repairMode = repairMode;
	}

	public String getRepairUser() {
		return this.repairUser;
	}

	public void setRepairUser(String repairUser) {
		this.repairUser = repairUser;
	}

	public String getRepairTel() {
		return this.repairTel;
	}

	public void setRepairTel(String repairTel) {
		this.repairTel = repairTel;
	}

	public String getServiceObjects() {
		return this.serviceObjects;
	}

	public void setServiceObjects(String serviceObjects) {
		this.serviceObjects = serviceObjects;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getProjectAddress() {
		return this.projectAddress;
	}

	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
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

	public String getElevatorParam() {
		return this.elevatorParam;
	}

	public void setElevatorParam(String elevatorParam) {
		this.elevatorParam = elevatorParam;
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

	public String getAssignObject() {
		return this.assignObject;
	}

	public void setAssignObject(String assignObject) {
		this.assignObject = assignObject;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIsTrap() {
		return this.isTrap;
	}

	public void setIsTrap(String isTrap) {
		this.isTrap = isTrap;
	}

	public String getRepairDesc() {
		return this.repairDesc;
	}

	public void setRepairDesc(String repairDesc) {
		this.repairDesc = repairDesc;
	}

	public String getAuditOperid() {
		return this.auditOperid;
	}

	public void setAuditOperid(String auditOperid) {
		this.auditOperid = auditOperid;
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

	public String getIsSendSms() {
		return this.isSendSms;
	}

	public void setIsSendSms(String isSendSms) {
		this.isSendSms = isSendSms;
	}

	public String getHfcId() {
		return this.hfcId;
	}

	public void setHfcId(String hfcId) {
		this.hfcId = hfcId;
	}

	public String getServiceAppraisal() {
		return this.serviceAppraisal;
	}

	public void setServiceAppraisal(String serviceAppraisal) {
		this.serviceAppraisal = serviceAppraisal;
	}

	public String getFittingSituation() {
		return this.fittingSituation;
	}

	public void setFittingSituation(String fittingSituation) {
		this.fittingSituation = fittingSituation;
	}

	public String getTollSituation() {
		return this.tollSituation;
	}

	public void setTollSituation(String tollSituation) {
		this.tollSituation = tollSituation;
	}

	public String getVisitRem() {
		return this.visitRem;
	}

	public void setVisitRem(String visitRem) {
		this.visitRem = visitRem;
	}

	public String getIsColse() {
		return this.isColse;
	}

	public void setIsColse(String isColse) {
		this.isColse = isColse;
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

	public Integer getR7() {
		return this.r7;
	}

	public void setR7(Integer r7) {
		this.r7 = r7;
	}

	public Integer getR8() {
		return this.r8;
	}

	public void setR8(Integer r8) {
		this.r8 = r8;
	}

	public Double getR9() {
		return this.r9;
	}

	public void setR9(Double r9) {
		this.r9 = r9;
	}

	public Double getR10() {
		return this.r10;
	}

	public void setR10(Double r10) {
		this.r10 = r10;
	}

	public String getIsSendSms2() {
		return this.isSendSms2;
	}

	public void setIsSendSms2(String isSendSms2) {
		this.isSendSms2 = isSendSms2;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}