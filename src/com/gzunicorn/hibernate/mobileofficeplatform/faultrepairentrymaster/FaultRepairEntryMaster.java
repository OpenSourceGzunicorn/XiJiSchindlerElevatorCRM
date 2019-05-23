package com.gzunicorn.hibernate.mobileofficeplatform.faultrepairentrymaster;

/**
 * FaultRepairEntryMaster entity. @author MyEclipse Persistence Tools
 */
public class FaultRepairEntryMaster extends AbstractFaultRepairEntryMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public FaultRepairEntryMaster() {
	}

	/** minimal constructor */
	public FaultRepairEntryMaster(String appNo, String repairName,
			String repairPhone, String repairDesc) {
		super(appNo, repairName, repairPhone, repairDesc);
	}

	/** full constructor */
	public FaultRepairEntryMaster(String appNo, String repairName,
			String repairPhone, String repairDesc, String projectName,
			String projectAddress, String isTiring, String operId,
			String operDate, String isProcess, String dealId, String dealDate,String rem,
			String r1, String r2, String r3, String r4, String r5, String r6,
			String r7, String r8, String r9, String r10) {
		super(appNo, repairName, repairPhone, repairDesc, projectName,
				projectAddress, isTiring, operId, operDate, isProcess, dealId,
				dealDate,rem, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
