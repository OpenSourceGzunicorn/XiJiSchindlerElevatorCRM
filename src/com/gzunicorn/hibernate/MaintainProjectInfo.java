package com.gzunicorn.hibernate;

/**
 * MaintainProjectInfo entity. @author MyEclipse Persistence Tools
 */
public class MaintainProjectInfo extends AbstractMaintainProjectInfo implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintainProjectInfo() {
	}

	/** minimal constructor */
	public MaintainProjectInfo(String elevatorType, String maintType,
			String maintItem, String maintContents, Integer orderby,
			String enabledFlag, String operId, String operDate) {
		super(elevatorType, maintType, maintItem, maintContents, orderby,
				enabledFlag, operId, operDate);
	}

	/** full constructor */
	public MaintainProjectInfo(String elevatorType, String maintType,
			String maintItem, String maintContents, Integer orderby,
			String enabledFlag, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(elevatorType, maintType, maintItem, maintContents, orderby,
				enabledFlag, rem, operId, operDate, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10);
	}

}
