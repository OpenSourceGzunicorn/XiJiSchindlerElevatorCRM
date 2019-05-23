package com.gzunicorn.hibernate.infomanager.handoverelevatorspecialregister;

import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;

/**
 * HandoverElevatorSpecialRegister entity. @author MyEclipse Persistence Tools
 */
public class HandoverElevatorSpecialRegister extends
		AbstractHandoverElevatorSpecialRegister implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public HandoverElevatorSpecialRegister() {
	}

	/** minimal constructor */
	public HandoverElevatorSpecialRegister(String jnlno,
			ElevatorTransferCaseRegister elevatorTransferCaseRegister,
			String scId) {
		super(jnlno, elevatorTransferCaseRegister, scId);
	}

	/** full constructor */
	public HandoverElevatorSpecialRegister(String jnlno,
			ElevatorTransferCaseRegister elevatorTransferCaseRegister,
			String scId, String isOk, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10) {
		super(jnlno, elevatorTransferCaseRegister, scId, isOk, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10);
	}

}
