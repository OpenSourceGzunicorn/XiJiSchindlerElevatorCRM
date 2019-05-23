package com.gzunicorn.hibernate.mobileofficeplatform.logmanagement;

/**
 * LogManagement entity. @author MyEclipse Persistence Tools
 */
public class LogManagement extends AbstractLogManagement implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public LogManagement() {
	}

	/** minimal constructor */
	public LogManagement(String maintDivision, String maintStation,
			String operId, String operDate, String salesContractNo,
			String projectName, String workContent) {
		super(maintDivision, maintStation, operId, operDate, salesContractNo,
				projectName, workContent);
	}

	/** full constructor */
	public LogManagement(String maintDivision, String maintStation,
			String operId, String operDate, String salesContractNo,
			String projectName, String workContent, String r1, String r2,
			String r3, String r4, String r5, Double r6, Integer r7, Integer r8,
			Double r9, Double r10) {
		super(maintDivision, maintStation, operId, operDate, salesContractNo,
				projectName, workContent, r1, r2, r3, r4, r5, r6, r7, r8, r9,
				r10);
	}

}
