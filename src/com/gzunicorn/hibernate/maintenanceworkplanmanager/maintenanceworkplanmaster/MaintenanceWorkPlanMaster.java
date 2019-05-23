package com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanmaster;

import java.util.Set;

/**
 * MaintenanceWorkPlanMaster entity. @author MyEclipse Persistence Tools
 */
public class MaintenanceWorkPlanMaster extends
		AbstractMaintenanceWorkPlanMaster implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintenanceWorkPlanMaster() {
	}

	/** minimal constructor */
	public MaintenanceWorkPlanMaster(String billno, String elevatorNo) {
		super(billno, elevatorNo);
	}

	/** full constructor */
	public MaintenanceWorkPlanMaster(String billno, Integer rowid,
			String elevatorNo, String checkflag, String checkoperid,
			String checkdate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String submitType, String maintPersonnel,
			String zpdate, String zpoperdate, String zpoperid,
			Set maintenanceWorkPlanDetails,String operid, String operdate,
			String maintLogic) {
		super(billno, rowid, elevatorNo, checkflag, checkoperid, checkdate, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10, submitType,
				maintPersonnel, zpdate, zpoperdate, zpoperid,
				maintenanceWorkPlanDetails,operid,operdate,maintLogic);
	}

}
