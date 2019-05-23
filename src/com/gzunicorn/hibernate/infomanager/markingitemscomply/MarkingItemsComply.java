package com.gzunicorn.hibernate.infomanager.markingitemscomply;

/**
 * MarkingItemsComply entity. @author MyEclipse Persistence Tools
 */
public class MarkingItemsComply extends AbstractMarkingItemsComply implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MarkingItemsComply() {
	}

	/** minimal constructor */
	public MarkingItemsComply(String billno, String mtId, Double fraction2,
			Double fraction) {
		super(billno, mtId, fraction2, fraction);
	}

	/** full constructor */
	public MarkingItemsComply(String billno, String mtId, Double fraction2,
			Double fraction, String appendix, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10) {
		super(billno, mtId, fraction2, fraction, appendix, r1, r2, r3, r4, r5,
				r6, r7, r8, r9, r10);
	}

}
