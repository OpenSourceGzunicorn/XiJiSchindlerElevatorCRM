package com.gzunicorn.hibernate.infomanager.handoverelevatorcheckitemregister;

import java.util.Set;

import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;

/**
 * HandoverElevatorCheckItemRegister entity. @author MyEclipse Persistence Tools
 */
public class HandoverElevatorCheckItemRegister extends
		AbstractHandoverElevatorCheckItemRegister implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public HandoverElevatorCheckItemRegister() {
	}

	/** minimal constructor */
	public HandoverElevatorCheckItemRegister(String jnlno, String examType,
			String checkItem, String issueCoding, String issueContents) {
		super(jnlno, examType, checkItem, issueCoding, issueContents);
	}

	/** full constructor */
	public HandoverElevatorCheckItemRegister(String jnlno,
			ElevatorTransferCaseRegister elevatorTransferCaseRegister,
			String examType, String checkItem, String issueCoding,
			String issueContents, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String isRecheck, String isDelete, String deleteRem,
			String rem,String isyzg, Set handoverElevatorCheckFileinfos) {
		super(jnlno, elevatorTransferCaseRegister, examType, checkItem,
				issueCoding, issueContents, r1, r2, r3, r4, r5, r6, r7, r8, r9,
				r10, isRecheck, isDelete, deleteRem, rem,isyzg,
				handoverElevatorCheckFileinfos);
	}

}
