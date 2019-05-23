package com.gzunicorn.hibernate.hotlinemanagement.calloutmaster;

/**
 * CalloutMaster entity. @author MyEclipse Persistence Tools
 */
public class CalloutMaster extends AbstractCalloutMaster implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public CalloutMaster() {
	}

	/** minimal constructor */
	public CalloutMaster(String calloutMasterNo, String handleStatus,
			String submitType, String operId, String operDate,
			String repairTime, String repairMode, String serviceObjects,
			String companyId, String isTrap) {
		super(calloutMasterNo, handleStatus, submitType, operId, operDate,
				repairTime, repairMode, serviceObjects, companyId, isTrap);
	}

	/** full constructor */
	public CalloutMaster(String calloutMasterNo, String handleStatus,
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
			String smAuditRem,String stophfRem,String stophfOperid,String stophfDate,String bhAuditRem) {
		super(calloutMasterNo, handleStatus, submitType, operId, operDate,
				repairTime, repairMode, repairUser, repairTel, serviceObjects,
				companyId, projectAddress, salesContractNo, elevatorNo,
				elevatorParam, maintDivision, maintStation, assignObject,
				phone, isTrap, repairDesc, auditOperid, auditDate, auditRem,
				isSendSms, hfcId, serviceAppraisal, fittingSituation,
				tollSituation, visitRem, isColse, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10, isSendSms2, projectName, isSubSM,  smAuditOperid,  smAuditDate,
				 smAuditRem,stophfRem,stophfOperid,stophfDate,bhAuditRem);
	}

}
