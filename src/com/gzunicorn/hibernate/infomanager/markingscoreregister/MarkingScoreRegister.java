package com.gzunicorn.hibernate.infomanager.markingscoreregister;

/**
 * MarkingScoreRegister entity. @author MyEclipse Persistence Tools
 */
public class MarkingScoreRegister extends AbstractMarkingScoreRegister
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MarkingScoreRegister() {
	}

	/** minimal constructor */
	public MarkingScoreRegister(String jnlno, String billno, String msId,
			String msName, Double fraction) {
		super(jnlno, billno, msId, msName, fraction);
	}

	/** full constructor */
	public MarkingScoreRegister(String jnlno, String billno, String msId,
			String msName, Double fraction, String rem, String isDelete,
			String deleteRem, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(jnlno, billno, msId, msName, fraction, rem, isDelete, deleteRem,
				r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
