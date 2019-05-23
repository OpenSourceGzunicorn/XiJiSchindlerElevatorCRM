package com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractElevatorTransferCaseRegister entity provides the base persistence
 * definition of the ElevatorTransferCaseRegister entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractElevatorTransferCaseRegister implements
		java.io.Serializable {

	// Fields

	private String billno;
	private Integer status;
	private Long tokenId;
	private String processName;
	private String processStatus;
	private String submitType;
	private String checkTime;
	private Integer checkNum;
	private String projectName;
	private String projectAddress;
	private String salesContractNo;
	private String elevatorNo;
	private String elevatorType;
	private String elevatorParam;
	private String insCompanyName;
	private String insLinkPhone;
	private String staffName;
	private String staffLinkPhone;
	private String department;
	private String factoryCheckResult;
	private String aopinion;
	private String aopinionDate;
	private String bopinion;
	private String bopinionDate;
	private String copinion;
	private String copinionDate;
	private String processResult;
	private String isClose;
	private String auditor;
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
	private String isxjs;
	private String dopinion;
	private String dopinionDate;
	private String bhRem;
	private String bhDate;
	private String transferId;
	private String transferDate;
	private String contractType;
	private String salesContractType;
	private Integer floor;
	private Integer stage;
	private Integer door;
	private Double high;
	private String weight;
	private String speed;
	private String isDeductions;
	private Double deductMoney;
	private String checkVersion;
	private String specialClaim;
	private String receiveDate;
	private String historyBillNo;
	private String workisdisplay;
	private String projectManager;
	private String projectManagerPhone;
	private String debugPers;
	private String debugPersPhone;
	private String elevatorAddress;
	private String colseRem;
	private String bhType;
	private String departmentOld;
	private String updateId;
	private String updateDate;
	private Set handoverElevatorCheckItemRegisters = new HashSet(0);
	private Set handoverElevatorSpecialRegisters = new HashSet(0);
	private Set elevatorTransferCaseProcesses = new HashSet(0);
	private String projectProvince;
	private String checkRem;
	private String customerSignature;
	private String customerImage;
	private String checkDate;
	private String checkTime2;
	private String insEmail;
	private String firstInstallation;
	private String factoryCheckResult2;

	// Constructors

	public String getFirstInstallation() {
		return firstInstallation;
	}

	public void setFirstInstallation(String firstInstallation) {
		this.firstInstallation = firstInstallation;
	}

	public String getFactoryCheckResult2() {
		return factoryCheckResult2;
	}

	public void setFactoryCheckResult2(String factoryCheckResult2) {
		this.factoryCheckResult2 = factoryCheckResult2;
	}

	public String getInsEmail() {
		return insEmail;
	}

	public void setInsEmail(String insEmail) {
		this.insEmail = insEmail;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckTime2() {
		return checkTime2;
	}

	public void setCheckTime2(String checkTime2) {
		this.checkTime2 = checkTime2;
	}

	public String getCustomerImage() {
		return customerImage;
	}

	public void setCustomerImage(String customerImage) {
		this.customerImage = customerImage;
	}

	public String getCheckRem() {
		return checkRem;
	}

	public void setCheckRem(String checkRem) {
		this.checkRem = checkRem;
	}

	public String getProjectProvince() {
		return projectProvince;
	}

	public void setProjectProvince(String projectProvince) {
		this.projectProvince = projectProvince;
	}

	/** default constructor */
	public AbstractElevatorTransferCaseRegister() {
	}

	/** minimal constructor */
	public AbstractElevatorTransferCaseRegister(String billno, Integer status,
			Long tokenId, String processName, String processStatus,
			String submitType, String checkTime, Integer checkNum,
			String projectName, String projectAddress, String salesContractNo,
			String elevatorNo, String elevatorType, String elevatorParam,
			String insCompanyName, String insLinkPhone, String department,
			String operId, String operDate) {
		this.billno = billno;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.processStatus = processStatus;
		this.submitType = submitType;
		this.checkTime = checkTime;
		this.checkNum = checkNum;
		this.projectName = projectName;
		this.projectAddress = projectAddress;
		this.salesContractNo = salesContractNo;
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.elevatorParam = elevatorParam;
		this.insCompanyName = insCompanyName;
		this.insLinkPhone = insLinkPhone;
		this.department = department;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractElevatorTransferCaseRegister(String billno, Integer status,
			Long tokenId, String processName, String processStatus,
			String submitType, String checkTime, Integer checkNum,
			String projectName, String projectAddress, String salesContractNo,
			String elevatorNo, String elevatorType, String elevatorParam,
			String insCompanyName, String insLinkPhone, String staffName,
			String staffLinkPhone, String department,
			String factoryCheckResult, String aopinion, String aopinionDate,
			String bopinion, String bopinionDate, String copinion,
			String copinionDate, String processResult, String isClose,
			String auditor, String operId, String operDate, String r1,
			String r2, String r3, String r4, String r5, Double r6, Double r7,
			Double r8, Integer r9, Integer r10, String isxjs, String dopinion,
			String dopinionDate, String bhRem, String bhDate,
			String transferId, String transferDate, String contractType,
			String salesContractType, Integer floor, Integer stage,
			Integer door, Double high, String weight, String speed,
			String isDeductions, Double deductMoney,
			String checkVersion, String specialClaim, String receiveDate,
			String historyBillNo, String workisdisplay, String projectManager,
			String projectManagerPhone, String debugPers,
			String debugPersPhone, String elevatorAddress, String colseRem,
			String bhType, String departmentOld, String updateId,
			String updateDate, Set handoverElevatorCheckItemRegisters,
			Set handoverElevatorSpecialRegisters,
			Set elevatorTransferCaseProcesses,
			String projectProvince,String checkRem, String customerSignature,String customerImage,
			String checkDate,String checkTime2,String insEmail,
			String firstInstallation, String factoryCheckResult2) {
		this.firstInstallation=firstInstallation;
		this.factoryCheckResult2=factoryCheckResult2;
		this.insEmail=insEmail;
		this.checkDate=checkDate;
		this.checkTime2=checkTime2;
		this.customerImage=customerImage;
		this.billno = billno;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.processStatus = processStatus;
		this.submitType = submitType;
		this.checkTime = checkTime;
		this.checkNum = checkNum;
		this.projectName = projectName;
		this.projectAddress = projectAddress;
		this.salesContractNo = salesContractNo;
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.elevatorParam = elevatorParam;
		this.insCompanyName = insCompanyName;
		this.insLinkPhone = insLinkPhone;
		this.staffName = staffName;
		this.staffLinkPhone = staffLinkPhone;
		this.department = department;
		this.factoryCheckResult = factoryCheckResult;
		this.aopinion = aopinion;
		this.aopinionDate = aopinionDate;
		this.bopinion = bopinion;
		this.bopinionDate = bopinionDate;
		this.copinion = copinion;
		this.copinionDate = copinionDate;
		this.processResult = processResult;
		this.isClose = isClose;
		this.auditor = auditor;
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
		this.isxjs = isxjs;
		this.dopinion = dopinion;
		this.dopinionDate = dopinionDate;
		this.bhRem = bhRem;
		this.bhDate = bhDate;
		this.transferId = transferId;
		this.transferDate = transferDate;
		this.contractType = contractType;
		this.salesContractType = salesContractType;
		this.floor = floor;
		this.stage = stage;
		this.door = door;
		this.high = high;
		this.weight = weight;
		this.speed = speed;
		this.isDeductions = isDeductions;
		this.deductMoney = deductMoney;
		this.customerSignature = customerSignature;
		this.checkVersion = checkVersion;
		this.specialClaim = specialClaim;
		this.receiveDate = receiveDate;
		this.historyBillNo = historyBillNo;
		this.workisdisplay = workisdisplay;
		this.projectManager = projectManager;
		this.projectManagerPhone = projectManagerPhone;
		this.debugPers = debugPers;
		this.debugPersPhone = debugPersPhone;
		this.elevatorAddress = elevatorAddress;
		this.colseRem = colseRem;
		this.bhType = bhType;
		this.departmentOld = departmentOld;
		this.updateId = updateId;
		this.updateDate = updateDate;
		this.handoverElevatorCheckItemRegisters = handoverElevatorCheckItemRegisters;
		this.handoverElevatorSpecialRegisters = handoverElevatorSpecialRegisters;
		this.elevatorTransferCaseProcesses = elevatorTransferCaseProcesses;
		this.projectProvince=projectProvince;
		this.checkRem=checkRem;
	}

	// Property accessors

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
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

	public String getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public Integer getCheckNum() {
		return this.checkNum;
	}

	public void setCheckNum(Integer checkNum) {
		this.checkNum = checkNum;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	public String getElevatorType() {
		return this.elevatorType;
	}

	public void setElevatorType(String elevatorType) {
		this.elevatorType = elevatorType;
	}

	public String getElevatorParam() {
		return this.elevatorParam;
	}

	public void setElevatorParam(String elevatorParam) {
		this.elevatorParam = elevatorParam;
	}

	public String getInsCompanyName() {
		return this.insCompanyName;
	}

	public void setInsCompanyName(String insCompanyName) {
		this.insCompanyName = insCompanyName;
	}

	public String getInsLinkPhone() {
		return this.insLinkPhone;
	}

	public void setInsLinkPhone(String insLinkPhone) {
		this.insLinkPhone = insLinkPhone;
	}

	public String getStaffName() {
		return this.staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffLinkPhone() {
		return this.staffLinkPhone;
	}

	public void setStaffLinkPhone(String staffLinkPhone) {
		this.staffLinkPhone = staffLinkPhone;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getFactoryCheckResult() {
		return this.factoryCheckResult;
	}

	public void setFactoryCheckResult(String factoryCheckResult) {
		this.factoryCheckResult = factoryCheckResult;
	}

	public String getAopinion() {
		return this.aopinion;
	}

	public void setAopinion(String aopinion) {
		this.aopinion = aopinion;
	}

	public String getAopinionDate() {
		return this.aopinionDate;
	}

	public void setAopinionDate(String aopinionDate) {
		this.aopinionDate = aopinionDate;
	}

	public String getBopinion() {
		return this.bopinion;
	}

	public void setBopinion(String bopinion) {
		this.bopinion = bopinion;
	}

	public String getBopinionDate() {
		return this.bopinionDate;
	}

	public void setBopinionDate(String bopinionDate) {
		this.bopinionDate = bopinionDate;
	}

	public String getCopinion() {
		return this.copinion;
	}

	public void setCopinion(String copinion) {
		this.copinion = copinion;
	}

	public String getCopinionDate() {
		return this.copinionDate;
	}

	public void setCopinionDate(String copinionDate) {
		this.copinionDate = copinionDate;
	}

	public String getProcessResult() {
		return this.processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public String getIsClose() {
		return this.isClose;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}

	public String getAuditor() {
		return this.auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
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

	public String getIsxjs() {
		return this.isxjs;
	}

	public void setIsxjs(String isxjs) {
		this.isxjs = isxjs;
	}

	public String getDopinion() {
		return this.dopinion;
	}

	public void setDopinion(String dopinion) {
		this.dopinion = dopinion;
	}

	public String getDopinionDate() {
		return this.dopinionDate;
	}

	public void setDopinionDate(String dopinionDate) {
		this.dopinionDate = dopinionDate;
	}

	public String getBhRem() {
		return this.bhRem;
	}

	public void setBhRem(String bhRem) {
		this.bhRem = bhRem;
	}

	public String getBhDate() {
		return this.bhDate;
	}

	public void setBhDate(String bhDate) {
		this.bhDate = bhDate;
	}

	public String getTransferId() {
		return this.transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getTransferDate() {
		return this.transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getSalesContractType() {
		return this.salesContractType;
	}

	public void setSalesContractType(String salesContractType) {
		this.salesContractType = salesContractType;
	}

	public Integer getFloor() {
		return this.floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Integer getStage() {
		return this.stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public Integer getDoor() {
		return this.door;
	}

	public void setDoor(Integer door) {
		this.door = door;
	}

	public Double getHigh() {
		return this.high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSpeed() {
		return this.speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getIsDeductions() {
		return this.isDeductions;
	}

	public void setIsDeductions(String isDeductions) {
		this.isDeductions = isDeductions;
	}

	public Double getDeductMoney() {
		return this.deductMoney;
	}

	public void setDeductMoney(Double deductMoney) {
		this.deductMoney = deductMoney;
	}

	public String getCustomerSignature() {
		return this.customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}

	public String getCheckVersion() {
		return this.checkVersion;
	}

	public void setCheckVersion(String checkVersion) {
		this.checkVersion = checkVersion;
	}

	public String getSpecialClaim() {
		return this.specialClaim;
	}

	public void setSpecialClaim(String specialClaim) {
		this.specialClaim = specialClaim;
	}

	public String getReceiveDate() {
		return this.receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getHistoryBillNo() {
		return this.historyBillNo;
	}

	public void setHistoryBillNo(String historyBillNo) {
		this.historyBillNo = historyBillNo;
	}

	public String getWorkisdisplay() {
		return this.workisdisplay;
	}

	public void setWorkisdisplay(String workisdisplay) {
		this.workisdisplay = workisdisplay;
	}

	public String getProjectManager() {
		return this.projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getProjectManagerPhone() {
		return this.projectManagerPhone;
	}

	public void setProjectManagerPhone(String projectManagerPhone) {
		this.projectManagerPhone = projectManagerPhone;
	}

	public String getDebugPers() {
		return this.debugPers;
	}

	public void setDebugPers(String debugPers) {
		this.debugPers = debugPers;
	}

	public String getDebugPersPhone() {
		return this.debugPersPhone;
	}

	public void setDebugPersPhone(String debugPersPhone) {
		this.debugPersPhone = debugPersPhone;
	}

	public String getElevatorAddress() {
		return this.elevatorAddress;
	}

	public void setElevatorAddress(String elevatorAddress) {
		this.elevatorAddress = elevatorAddress;
	}

	public String getColseRem() {
		return this.colseRem;
	}

	public void setColseRem(String colseRem) {
		this.colseRem = colseRem;
	}

	public String getBhType() {
		return this.bhType;
	}

	public void setBhType(String bhType) {
		this.bhType = bhType;
	}

	public String getDepartmentOld() {
		return this.departmentOld;
	}

	public void setDepartmentOld(String departmentOld) {
		this.departmentOld = departmentOld;
	}

	public String getUpdateId() {
		return this.updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public String getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Set getHandoverElevatorCheckItemRegisters() {
		return this.handoverElevatorCheckItemRegisters;
	}

	public void setHandoverElevatorCheckItemRegisters(
			Set handoverElevatorCheckItemRegisters) {
		this.handoverElevatorCheckItemRegisters = handoverElevatorCheckItemRegisters;
	}

	public Set getHandoverElevatorSpecialRegisters() {
		return this.handoverElevatorSpecialRegisters;
	}

	public void setHandoverElevatorSpecialRegisters(
			Set handoverElevatorSpecialRegisters) {
		this.handoverElevatorSpecialRegisters = handoverElevatorSpecialRegisters;
	}

	public Set getElevatorTransferCaseProcesses() {
		return this.elevatorTransferCaseProcesses;
	}

	public void setElevatorTransferCaseProcesses(
			Set elevatorTransferCaseProcesses) {
		this.elevatorTransferCaseProcesses = elevatorTransferCaseProcesses;
	}

}