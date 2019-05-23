package com.gzunicorn.hibernate.maintainprojectinfowork;

/**
 * MaintainProjectInfoWork entity. @author MyEclipse Persistence Tools
 */
public class MaintainProjectInfoWork extends AbstractMaintainProjectInfoWork
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintainProjectInfoWork() {
	}

	/** minimal constructor */
	public MaintainProjectInfoWork(String singleno, Integer orderby,
			String maintItem, String maintContents, String isMaintain) {
		super(singleno, orderby, maintItem, maintContents, isMaintain);
	}

	/** full constructor */
	public MaintainProjectInfoWork(String singleno, Integer orderby,
			String maintItem, String maintContents, String isMaintain,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(singleno, orderby, maintItem, maintContents, isMaintain, r1, r2,
				r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
