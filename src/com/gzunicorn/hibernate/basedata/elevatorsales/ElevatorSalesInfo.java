package com.gzunicorn.hibernate.basedata.elevatorsales;

/**
 * ElevatorSalesInfo entity. @author MyEclipse Persistence Tools
 */
public class ElevatorSalesInfo extends AbstractElevatorSalesInfo implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ElevatorSalesInfo() {
	}

	/** minimal constructor */
	public ElevatorSalesInfo(String elevatorNo, String elevatorType,
			String salesContractNo, String salesContractName,
			String salesContractType, String inspectDate, String elevatorParam,
			Integer floor, Integer stage, Integer door, String weight,
			String speed, Double high, String seriesName, String enabledFlag,
			String operId, String operDate) {
		super(elevatorNo, elevatorType, salesContractNo, salesContractName,
				salesContractType, inspectDate, elevatorParam, floor, stage,
				door, weight, speed, high, seriesName, enabledFlag, operId,
				operDate);
	}

	/** full constructor */
	public ElevatorSalesInfo(String elevatorNo, String elevatorType,
			String salesContractNo, String salesContractName,
			String salesContractType, String inspectDate, String elevatorParam,
			Integer floor, Integer stage, Integer door, String weight,
			String speed, Double high, String seriesName, String useUnit,
			String dealer, String deliveryAddress, String department,
			String operationName, String operationPhone, String enabledFlag,
			String rem, String operId, String operDate, String seriesId,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10, String configuring,
			String isOutsideFootball, String maintenanceClause, String warranty) {
		super(elevatorNo, elevatorType, salesContractNo, salesContractName,
				salesContractType, inspectDate, elevatorParam, floor, stage,
				door, weight, speed, high, seriesName, useUnit, dealer,
				deliveryAddress, department, operationName, operationPhone,
				enabledFlag, rem, operId, operDate, seriesId, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10, configuring, isOutsideFootball,
				maintenanceClause, warranty);
	}

}
