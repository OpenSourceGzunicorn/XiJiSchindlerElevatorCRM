package com.gzunicorn.hibernate.infomanager.termsecurityriskscomply;

/**
 * TermSecurityRisksComply entity. @author MyEclipse Persistence Tools
 */
public class TermSecurityRisksComply extends AbstractTermSecurityRisksComply
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public TermSecurityRisksComply() {
	}

	/** minimal constructor */
	public TermSecurityRisksComply(String billno, String tsrId) {
		super(billno, tsrId);
	}

	/** full constructor */
	public TermSecurityRisksComply(String billno, String tsrId,
			String appendix, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(billno, tsrId, appendix, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
