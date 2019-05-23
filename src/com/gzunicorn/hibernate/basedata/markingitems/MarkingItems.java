package com.gzunicorn.hibernate.basedata.markingitems;

/**
 * MarkingItems entity. @author MyEclipse Persistence Tools
 */
public class MarkingItems extends AbstractMarkingItems implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MarkingItems() {
	}

	/** minimal constructor */
	public MarkingItems(String mtId, String mtName, Double fraction,
			String enabledFlag, String operId, String operDate) {
		super(mtId, mtName, fraction, enabledFlag, operId, operDate);
	}

	/** full constructor */
	public MarkingItems(String mtId, String mtName, Double fraction,
			String enabledFlag, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(mtId, mtName, fraction, enabledFlag, rem, operId, operDate, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
