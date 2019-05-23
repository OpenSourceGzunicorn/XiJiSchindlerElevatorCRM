package com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister;

import java.util.Set;

/**
 * ElevatorTransferCaseRegister entity. @author MyEclipse Persistence Tools
 */
public class ElevatorTransferCaseRegister extends
		AbstractElevatorTransferCaseRegister implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ElevatorTransferCaseRegister() {
	}

	/** minimal constructor */
	public ElevatorTransferCaseRegister(String billno, Integer status,
			Long tokenId, String processName, String processStatus,
			String submitType, String checkTime, Integer checkNum,
			String projectName, String projectAddress, String salesContractNo,
			String elevatorNo, String elevatorType, String elevatorParam,
			String insCompanyName, String insLinkPhone, String department,
			String operId, String operDate) {
		super(billno, status, tokenId, processName, processStatus, submitType,
				checkTime, checkNum, projectName, projectAddress,
				salesContractNo, elevatorNo, elevatorType, elevatorParam,
				insCompanyName, insLinkPhone, department, operId, operDate);
	}

	/** full constructor */
	public ElevatorTransferCaseRegister(String billno, Integer status,
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
			String projectProvince,String checkRem,
			String customerSignature, String customerImage,String checkDate,String checkTime2, String insEmail,
			String firstInstallation, String factoryCheckResult2) {
		super(billno, status, tokenId, processName, processStatus, submitType,
				checkTime, checkNum, projectName, projectAddress,
				salesContractNo, elevatorNo, elevatorType, elevatorParam,
				insCompanyName, insLinkPhone, staffName, staffLinkPhone,
				department, factoryCheckResult, aopinion, aopinionDate,
				bopinion, bopinionDate, copinion, copinionDate, processResult,
				isClose, auditor, operId, operDate, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10, isxjs, dopinion, dopinionDate, bhRem, bhDate,
				transferId, transferDate, contractType, salesContractType,
				floor, stage, door, high, weight, speed, isDeductions,
				deductMoney, checkVersion, specialClaim,
				receiveDate, historyBillNo, workisdisplay, projectManager,
				projectManagerPhone, debugPers, debugPersPhone,
				elevatorAddress, colseRem, bhType, departmentOld, updateId,
				updateDate, handoverElevatorCheckItemRegisters,
				handoverElevatorSpecialRegisters, elevatorTransferCaseProcesses,
				projectProvince,checkRem,customerSignature,customerImage,checkDate,checkTime2,insEmail,
				firstInstallation, factoryCheckResult2);
	}

}
