package com.gzunicorn.hibernate.engcontractmanager.entrustcontractdetail;

import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;

/**
 * EntrustContractDetail entity. @author MyEclipse Persistence Tools
 */
public class EntrustContractDetail extends AbstractEntrustContractDetail
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public EntrustContractDetail() {
	}

	/** minimal constructor */
	public EntrustContractDetail(EntrustContractMaster entrustContractMaster,
			Integer maintRowid, String elevatorNo, String elevatorType,
			Integer floor, Integer stage, Integer door, Double high,
			String elevatorParam, String salesContractNo) {
		super(entrustContractMaster, maintRowid, elevatorNo, elevatorType,
				floor, stage, door, high, elevatorParam, salesContractNo);
	}

	/** full constructor */
	public EntrustContractDetail(EntrustContractMaster entrustContractMaster,
			Integer maintRowid, String elevatorNo, String elevatorType,
			Integer floor, Integer stage, Integer door, Double high,
			String elevatorParam, String annualInspectionDate,
			String salesContractNo, String projectName, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10, String maintAddress, String mainSdate,
			String mainEdate) {
		super(entrustContractMaster, maintRowid, elevatorNo, elevatorType,
				floor, stage, door, high, elevatorParam, annualInspectionDate,
				salesContractNo, projectName, r1, r2, r3, r4, r5, r6, r7, r8,
				r9, r10, maintAddress, mainSdate, mainEdate);
	}

}
