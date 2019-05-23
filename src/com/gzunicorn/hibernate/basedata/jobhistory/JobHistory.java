package com.gzunicorn.hibernate.basedata.jobhistory;

/**
 * JobHistory entity. @author MyEclipse Persistence Tools
 */
public class JobHistory extends AbstractJobHistory implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public JobHistory() {
	}

	/** minimal constructor */
	public JobHistory(Integer numNo, String billno, String sdate, String edate,
			String workAddress, String jobTitle, String level,
			String revisionDate) {
		super(numNo, billno, sdate, edate, workAddress, jobTitle, level,
				revisionDate);
	}

	/** full constructor */
	public JobHistory(Integer numNo, String billno, String sdate, String edate,
			String workAddress, String jobTitle, String level,
			String revisionDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(numNo, billno, sdate, edate, workAddress, jobTitle, level,
				revisionDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
