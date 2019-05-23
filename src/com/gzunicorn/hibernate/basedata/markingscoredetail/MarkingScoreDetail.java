package com.gzunicorn.hibernate.basedata.markingscoredetail;

import com.gzunicorn.hibernate.basedata.markingscore.MarkingScore;

/**
 * MarkingScoreDetail entity. @author MyEclipse Persistence Tools
 */
public class MarkingScoreDetail extends AbstractMarkingScoreDetail implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MarkingScoreDetail() {
	}

	/** minimal constructor */
	public MarkingScoreDetail(MarkingScore markingScore,String detailId, String detailName, String msId) {
		super(markingScore,detailId, detailName, msId);
	}

	/** full constructor */
	public MarkingScoreDetail(MarkingScore markingScore,String detailId, String detailName, String msId,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(markingScore,detailId, detailName,msId,r1, r2, r3, r4, r5, r6, r7, r8, r9,
				r10);
	}

}
