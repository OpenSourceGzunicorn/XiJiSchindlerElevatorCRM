package com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail;

/**
 * MaintContractDetail entity. @author MyEclipse Persistence Tools
 */
public class MaintContractDetail extends AbstractMaintContractDetail implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintContractDetail() {
	}

	/** minimal constructor */
	public MaintContractDetail(String elevatorNo, String elevatorType,
			Integer floor, Integer stage, Integer door, Double high,
			String elevatorParam, String annualInspectionDate,
			String salesContractNo, String mainSdate, String mainEdate) {
		super(elevatorNo, elevatorType, floor, stage, door, high,
				elevatorParam, annualInspectionDate, salesContractNo,
				mainSdate, mainEdate);
	}

	/** full constructor */
	public MaintContractDetail(String billNo, String signWay,
			String elevatorNo, String elevatorType, Integer floor,
			Integer stage, Integer door, Double high, String elevatorParam,
			String annualInspectionDate, String salesContractNo,
			String projectName, String mainSdate, String mainEdate,
			String realityEdate, String shippedDate, String issueDate,
			String tranCustDate, String mainConfirmDate,
			String assignedMainStation, String assignedSignFlag,
			String assignedSign, String assignedSignDate, String returnReason,
			String maintPersonnel, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String maintAddress, String isSurrender,
			String surrenderUser, String surrenderDate,String delayEDate,
			String elevatorNature,String elevatorStatus,String isCertificate,String certificateDate,
			String aidateoperid,String aidateopertime) {
		super(billNo, signWay, elevatorNo, elevatorType, floor, stage, door,
				high, elevatorParam, annualInspectionDate, salesContractNo,
				projectName, mainSdate, mainEdate, realityEdate, shippedDate,
				issueDate, tranCustDate, mainConfirmDate, assignedMainStation,
				assignedSignFlag, assignedSign, assignedSignDate, returnReason,
				maintPersonnel, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				maintAddress, isSurrender, surrenderUser, surrenderDate,delayEDate,
				elevatorNature,elevatorStatus,isCertificate,certificateDate,
				aidateoperid,aidateopertime);
	}

}
