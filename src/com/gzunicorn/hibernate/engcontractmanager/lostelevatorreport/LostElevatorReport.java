package com.gzunicorn.hibernate.engcontractmanager.lostelevatorreport;

/**
 * LostElevatorReport entity. @author MyEclipse Persistence Tools
 */
public class LostElevatorReport extends AbstractLostElevatorReport implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public LostElevatorReport() {
	}

	/** minimal constructor */
	public LostElevatorReport(String jnlno, String billNo,
			String maintDivision, String maintStation, String maintContractNo,
			String contractNatureOf, String projectName, Integer eleNum,
			String lostElevatorDate, String causeAnalysis, String submitType,
			String operId, String operDate) {
		super(jnlno, billNo, maintDivision, maintStation, maintContractNo,
				contractNatureOf, projectName, eleNum, lostElevatorDate,
				causeAnalysis, submitType, operId, operDate);
	}

	/** full constructor */
	public LostElevatorReport(String jnlno, String billNo,
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
		super(jnlno, billNo, maintDivision, maintStation, maintContractNo,
				contractNatureOf, projectName, eleNum, lostElevatorDate,
				causeAnalysis, contacts, contactPhone, detailedRem,
				competeCompany, recoveryPlan, submitType, auditOperid,
				auditStatus, auditDate, auditRem, operId, operDate, r1, r2, r3,
				r4, r5, r6, r7, r8, r9, r10, auditOperid2,auditStatus2, auditDate2, auditRem2,
				workisdisplay,workisdisplay2,workisdisplay3,
				isCharge, auditOperid3,auditStatus3, auditDate3, auditRem3,
				hfOperid,hfDate,fhRem,hfRemLast);
	}

}
