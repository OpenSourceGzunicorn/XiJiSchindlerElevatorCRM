package com.gzunicorn.hibernate.basedata.maintenanceworkinghours;

/**
 * MaintenanceWorkingHours entity. @author MyEclipse Persistence Tools
 */
public class MaintenanceWorkingHours extends AbstractMaintenanceWorkingHours
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintenanceWorkingHours() {
	}

	/** minimal constructor */
	public MaintenanceWorkingHours(MaintenanceWorkingHoursId id,
			Integer halfMonth, Integer quarter, Integer halfYear,
			Integer yearDegree, String enabledFlag, String operId,
			String operDate) {
		super(id, halfMonth, quarter, halfYear, yearDegree, enabledFlag,
				operId, operDate);
	}

	/** full constructor */
	public MaintenanceWorkingHours(MaintenanceWorkingHoursId id,
			Integer halfMonth, Integer quarter, Integer halfYear,
			Integer yearDegree, String enabledFlag, String rem, String operId,
			String operDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(id, halfMonth, quarter, halfYear, yearDegree, enabledFlag, rem,
				operId, operDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
