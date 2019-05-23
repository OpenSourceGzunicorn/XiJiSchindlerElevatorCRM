package com.gzunicorn.hibernate.sysmanager.pulldown;

/**
 * Pulldown entity. @author MyEclipse Persistence Tools
 */
public class Pulldown extends AbstractPulldown implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Pulldown() {
	}

	/** minimal constructor */
	public Pulldown(PulldownId id, String pullname, String enabledflag,
			String pullrem) {
		super(id, pullname, enabledflag, pullrem);
	}

	/** full constructor */
	public Pulldown(PulldownId id, String pullname, String enabledflag,
			String pullrem, String r1, String r2, String r3, String r4,
			String r5, String r6,Integer orderby) {
		super(id, pullname, enabledflag, pullrem, r1, r2, r3, r4, r5, r6,orderby);
	}

}
