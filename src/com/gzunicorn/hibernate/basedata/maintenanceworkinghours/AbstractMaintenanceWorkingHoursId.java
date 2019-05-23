package com.gzunicorn.hibernate.basedata.maintenanceworkinghours;

/**
 * AbstractMaintenanceWorkingHoursId entity provides the base persistence
 * definition of the MaintenanceWorkingHoursId entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractMaintenanceWorkingHoursId implements
		java.io.Serializable {

	// Fields

	private String elevatorType;
	private Integer floor;

	// Constructors

	/** default constructor */
	public AbstractMaintenanceWorkingHoursId() {
	}

	/** full constructor */
	public AbstractMaintenanceWorkingHoursId(String elevatorType, Integer floor) {
		this.elevatorType = elevatorType;
		this.floor = floor;
	}

	// Property accessors

	public String getElevatorType() {
		return this.elevatorType;
	}

	public void setElevatorType(String elevatorType) {
		this.elevatorType = elevatorType;
	}

	public Integer getFloor() {
		return this.floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractMaintenanceWorkingHoursId))
			return false;
		AbstractMaintenanceWorkingHoursId castOther = (AbstractMaintenanceWorkingHoursId) other;

		return ((this.getElevatorType() == castOther.getElevatorType()) || (this
				.getElevatorType() != null
				&& castOther.getElevatorType() != null && this
				.getElevatorType().equals(castOther.getElevatorType())))
				&& ((this.getFloor() == castOther.getFloor()) || (this
						.getFloor() != null && castOther.getFloor() != null && this
						.getFloor().equals(castOther.getFloor())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getElevatorType() == null ? 0 : this.getElevatorType()
						.hashCode());
		result = 37 * result
				+ (getFloor() == null ? 0 : this.getFloor().hashCode());
		return result;
	}

}