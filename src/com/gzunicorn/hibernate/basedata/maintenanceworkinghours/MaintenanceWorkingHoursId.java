package com.gzunicorn.hibernate.basedata.maintenanceworkinghours;

/**
 * MaintenanceWorkingHoursId entity. @author MyEclipse Persistence Tools
 */
public class MaintenanceWorkingHoursId extends
		AbstractMaintenanceWorkingHoursId implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintenanceWorkingHoursId() {
	}

	/** full constructor */
	public MaintenanceWorkingHoursId(String elevatorType, Integer floor) {
		super(elevatorType, floor);
	}

}
