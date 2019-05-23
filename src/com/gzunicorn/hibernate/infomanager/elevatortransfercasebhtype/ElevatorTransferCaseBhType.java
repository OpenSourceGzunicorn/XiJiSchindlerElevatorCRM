package com.gzunicorn.hibernate.infomanager.elevatortransfercasebhtype;


/**
 * HandoverElevatorSpecialRegister entity. @author MyEclipse Persistence Tools
 */
public class ElevatorTransferCaseBhType extends
		AbstractElevatorTransferCaseBhType implements java.io.Serializable {

	// Constructors
	/** default constructor */
	public ElevatorTransferCaseBhType() {
	}
	
	/** default constructor */
	public ElevatorTransferCaseBhType(Integer numid) {
		super(numid);
	}


	/** full constructor */
	public ElevatorTransferCaseBhType(Integer numid,String billno,
			String bhType, String bhRem,String bhDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10) {
		super(numid, billno,bhType, bhRem,bhDate, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10);
	}

}
