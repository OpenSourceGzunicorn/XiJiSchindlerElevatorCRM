package com.gzunicorn.hibernate.infomanager.shouldexamineitemscomply;

/**
 * ShouldExamineItemsComply entity. @author MyEclipse Persistence Tools
 */
public class ShouldExamineItemsComply extends AbstractShouldExamineItemsComply
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ShouldExamineItemsComply() {
	}

	/** minimal constructor */
	public ShouldExamineItemsComply(String billno, String seiid) {
		super(billno, seiid);
	}

	/** full constructor */
	public ShouldExamineItemsComply(String billno, String seiid,
			String appendix, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(billno, seiid, appendix, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
