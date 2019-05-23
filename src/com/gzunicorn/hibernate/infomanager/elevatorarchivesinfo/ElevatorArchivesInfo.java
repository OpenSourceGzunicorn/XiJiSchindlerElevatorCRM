package com.gzunicorn.hibernate.infomanager.elevatorarchivesinfo;

/**
 * ElevatorArchivesInfo entity. @author MyEclipse Persistence Tools
 */
public class ElevatorArchivesInfo extends AbstractElevatorArchivesInfo
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ElevatorArchivesInfo() {
	}

	/** minimal constructor */
	public ElevatorArchivesInfo(Integer numno) {
		super(numno);
	}

	/** full constructor */
	public ElevatorArchivesInfo(Integer numno, String salesContractNo,
			String maintContractNo, String projectName, String projectAddress,
			String maintDivision, String maintStation, String elevatorNo,
			String elevatorType, String elevatorParam, Integer floor,
			Integer stage, Integer door, Double high, String detailConfig,
			String deliveryDate, String certificatDate, String customerDate,
			String confirmDate, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(numno, salesContractNo, maintContractNo, projectName,
				projectAddress, maintDivision, maintStation, elevatorNo,
				elevatorType, elevatorParam, floor, stage, door, high,
				detailConfig, deliveryDate, certificatDate, customerDate,
				confirmDate, rem, operId, operDate, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10);
	}

}
