package com.gzunicorn.hibernate.sysmanager.pulldown;

/**
 * AbstractPulldown entity provides the base persistence definition of the
 * Pulldown entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractPulldown implements java.io.Serializable {

	// Fields

	private PulldownId id;
	private String pullname;
	private String enabledflag;
	private String pullrem;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private String r6;
	private Integer orderby;

	// Constructors

	/** default constructor */
	public AbstractPulldown() {
	}

	/** minimal constructor */
	public AbstractPulldown(PulldownId id, String pullname, String enabledflag,
			String pullrem) {
		this.id = id;
		this.pullname = pullname;
		this.enabledflag = enabledflag;
		this.pullrem = pullrem;
	}

	/** full constructor */
	public AbstractPulldown(PulldownId id, String pullname, String enabledflag,
			String pullrem, String r1, String r2, String r3, String r4,
			String r5, String r6,Integer orderby) {
		this.id = id;
		this.pullname = pullname;
		this.enabledflag = enabledflag;
		this.pullrem = pullrem;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.r4 = r4;
		this.r5 = r5;
		this.r6 = r6;
		this.orderby=orderby;
	}

	// Property accessors

	public PulldownId getId() {
		return this.id;
	}

	public void setId(PulldownId id) {
		this.id = id;
	}

	public String getPullname() {
		return this.pullname;
	}

	public void setPullname(String pullname) {
		this.pullname = pullname;
	}

	public String getEnabledflag() {
		return this.enabledflag;
	}

	public void setEnabledflag(String enabledflag) {
		this.enabledflag = enabledflag;
	}

	public String getPullrem() {
		return this.pullrem;
	}

	public void setPullrem(String pullrem) {
		this.pullrem = pullrem;
	}

	public String getR1() {
		return this.r1;
	}

	public void setR1(String r1) {
		this.r1 = r1;
	}

	public String getR2() {
		return this.r2;
	}

	public void setR2(String r2) {
		this.r2 = r2;
	}

	public String getR3() {
		return this.r3;
	}

	public void setR3(String r3) {
		this.r3 = r3;
	}

	public String getR4() {
		return this.r4;
	}

	public void setR4(String r4) {
		this.r4 = r4;
	}

	public String getR5() {
		return this.r5;
	}

	public void setR5(String r5) {
		this.r5 = r5;
	}

	public String getR6() {
		return this.r6;
	}

	public void setR6(String r6) {
		this.r6 = r6;
	}
	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

}