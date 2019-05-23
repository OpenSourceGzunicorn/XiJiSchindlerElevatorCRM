package com.gzunicorn.hibernate.custregistervisitplan.LostElevatorMaintainDetail;

import com.gzunicorn.hibernate.custregistervisitplan.LostElevatorMaintain.LostElevatorMaintain;

/**
 * AbstractLostElevatorMaintainDetail entity provides the base persistence
 * definition of the LostElevatorMaintainDetail entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractLostElevatorMaintainDetail implements
		java.io.Serializable {

	// Fields

	private Integer numno;
	private LostElevatorMaintain lostElevatorMaintain;
	private String jnlno;
	private String wbBillno;
	private String maintContractNo;
	private String lostElevatorDate;
	private String causeAnalysis;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private Double r6;
	private Integer r7;
	private Integer r8;
	private Double r9;
	private Double r10;

	// Constructors

	/** default constructor */
	public AbstractLostElevatorMaintainDetail() {
	}

	/** minimal constructor */
	public AbstractLostElevatorMaintainDetail(
			LostElevatorMaintain lostElevatorMaintain, String jnlno,
			String wbBillno, String maintContractNo, String lostElevatorDate,
			String causeAnalysis) {
		this.lostElevatorMaintain = lostElevatorMaintain;
		this.jnlno = jnlno;
		this.wbBillno = wbBillno;
		this.maintContractNo = maintContractNo;
		this.lostElevatorDate = lostElevatorDate;
		this.causeAnalysis = causeAnalysis;
	}

	/** full constructor */
	public AbstractLostElevatorMaintainDetail(
			LostElevatorMaintain lostElevatorMaintain, String jnlno,
			String wbBillno, String maintContractNo, String lostElevatorDate,
			String causeAnalysis, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9, Double r10) {
		this.lostElevatorMaintain = lostElevatorMaintain;
		this.jnlno = jnlno;
		this.wbBillno = wbBillno;
		this.maintContractNo = maintContractNo;
		this.lostElevatorDate = lostElevatorDate;
		this.causeAnalysis = causeAnalysis;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.r4 = r4;
		this.r5 = r5;
		this.r6 = r6;
		this.r7 = r7;
		this.r8 = r8;
		this.r9 = r9;
		this.r10 = r10;
	}

	// Property accessors

	public Integer getNumno() {
		return this.numno;
	}

	public void setNumno(Integer numno) {
		this.numno = numno;
	}

	public LostElevatorMaintain getLostElevatorMaintain() {
		return this.lostElevatorMaintain;
	}

	public void setLostElevatorMaintain(
			LostElevatorMaintain lostElevatorMaintain) {
		this.lostElevatorMaintain = lostElevatorMaintain;
	}

	public String getJnlno() {
		return this.jnlno;
	}

	public void setJnlno(String jnlno) {
		this.jnlno = jnlno;
	}

	public String getWbBillno() {
		return this.wbBillno;
	}

	public void setWbBillno(String wbBillno) {
		this.wbBillno = wbBillno;
	}

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public String getLostElevatorDate() {
		return this.lostElevatorDate;
	}

	public void setLostElevatorDate(String lostElevatorDate) {
		this.lostElevatorDate = lostElevatorDate;
	}

	public String getCauseAnalysis() {
		return this.causeAnalysis;
	}

	public void setCauseAnalysis(String causeAnalysis) {
		this.causeAnalysis = causeAnalysis;
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

	public Double getR6() {
		return this.r6;
	}

	public void setR6(Double r6) {
		this.r6 = r6;
	}

	public Integer getR7() {
		return this.r7;
	}

	public void setR7(Integer r7) {
		this.r7 = r7;
	}

	public Integer getR8() {
		return this.r8;
	}

	public void setR8(Integer r8) {
		this.r8 = r8;
	}

	public Double getR9() {
		return this.r9;
	}

	public void setR9(Double r9) {
		this.r9 = r9;
	}

	public Double getR10() {
		return this.r10;
	}

	public void setR10(Double r10) {
		this.r10 = r10;
	}

}