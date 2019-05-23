package com.gzunicorn.hibernate.basedata.elevatorcoordinatelocation;

/**
 * ElevatorCoordinateLocation entity. @author MyEclipse Persistence Tools
 */
public class ElevatorCoordinateLocation extends
		AbstractElevatorCoordinateLocation implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ElevatorCoordinateLocation() {
	}

	/** minimal constructor */
	public ElevatorCoordinateLocation(String elevatorNo,
			String elevatorLocation, Double beginLongitude,
			Double endLongitude, Double beginDimension, Double endDimension,
			String enabledFlag, String operId, String operDate) {
		super(elevatorNo, elevatorLocation, beginLongitude, endLongitude,
				beginDimension, endDimension, enabledFlag, operId, operDate);
	}

	/** full constructor */
	public ElevatorCoordinateLocation(String elevatorNo,
			String elevatorLocation, Double beginLongitude,
			Double endLongitude, Double beginDimension, Double endDimension,
			String enabledFlag, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(elevatorNo, elevatorLocation, beginLongitude, endLongitude,
				beginDimension, endDimension, enabledFlag, rem, operId,
				operDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
