package com.gzunicorn.hibernate.basedata.receivablesname;

/**
 * ReceivablesName entity. @author MyEclipse Persistence Tools
 */
public class ReceivablesName extends AbstractReceivablesName implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ReceivablesName() {
	}

	/** minimal constructor */
	public ReceivablesName(String recId, String recName, String enabledFlag,
			String operId, String operDate) {
		super(recId, recName, enabledFlag, operId, operDate);
	}

	/** full constructor */
	public ReceivablesName(String recId, String recName, String enabledFlag,
			String rem, String operId, String operDate, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10) {
		super(recId, recName, enabledFlag, rem, operId, operDate, r1, r2, r3,
				r4, r5, r6, r7, r8, r9, r10);
	}

}
