package com.gzunicorn.hibernate.infomanager.markingscoreregisterdetail;

/**
 * MarkingScoreRegisterDetail entity. @author MyEclipse Persistence Tools
 */
public class MarkingScoreRegisterDetail extends
		AbstractMarkingScoreRegisterDetail implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MarkingScoreRegisterDetail() {
	}

	/** minimal constructor */
	public MarkingScoreRegisterDetail(String jnlno, String msId,
			String detailId, String detailName) {
		super(jnlno, msId, detailId, detailName);
	}

	/** full constructor */
	public MarkingScoreRegisterDetail(String jnlno, String msId,
			String detailId, String detailName, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10) {
		super(jnlno, msId, detailId, detailName, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10);
	}

}
