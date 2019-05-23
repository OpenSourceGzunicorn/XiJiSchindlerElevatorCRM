package com.gzunicorn.hibernate.basedata.markingscore;

/**
 * MarkingScore entity. @author MyEclipse Persistence Tools
 */
public class MarkingScore extends AbstractMarkingScore implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MarkingScore() {
	}

	/** minimal constructor */
	public MarkingScore(String msId, String msName, Double fraction,
			String enabledFlag, String operId, String operDate) {
		super(msId, msName, fraction, enabledFlag, operId, operDate);
	}

	/** full constructor */
	public MarkingScore(String msId, String msName, Double fraction,
			String enabledFlag, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10, String elevatorType,Integer orderby) {
		super(msId, msName, fraction, enabledFlag, rem, operId, operDate, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10, elevatorType,orderby);
	}

}
