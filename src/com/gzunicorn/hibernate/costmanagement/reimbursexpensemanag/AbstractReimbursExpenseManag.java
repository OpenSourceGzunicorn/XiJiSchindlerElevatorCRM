package com.gzunicorn.hibernate.costmanagement.reimbursexpensemanag;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractReimbursExpenseManag entity provides the base persistence definition
 * of the ReimbursExpenseManag entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractReimbursExpenseManag implements
		java.io.Serializable {

	// Fields

	private String billno;
	private String maintDivision;
	private String maintStation;
	private String reimbursPeople;
	private String reimbursDate;
	private Double totalAmount;
	private Double projectMoney;
	private Double stationMoney;
	private String operId;
	private String operDate;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private Double r6;
	private Double r7;
	private Double r8;
	private Integer r9;
	private Integer r10;
	private Double divsionMoney;
	private Double noReimMoney;
	private Set maintStationReimbursements = new HashSet(0);
	private Set maintDivisionReimbursements = new HashSet(0);
	private Set projectReimbursements = new HashSet(0);
	private Set noReimbursements = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractReimbursExpenseManag() {
	}

	/** minimal constructor */
	public AbstractReimbursExpenseManag(String billno, String maintDivision,
			String maintStation, String reimbursPeople, String reimbursDate,
			Double totalAmount, String operId, String operDate) {
		this.billno = billno;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.reimbursPeople = reimbursPeople;
		this.reimbursDate = reimbursDate;
		this.totalAmount = totalAmount;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractReimbursExpenseManag(String billno, String maintDivision,
			String maintStation, String reimbursPeople, String reimbursDate,
			Double totalAmount, Double projectMoney, Double stationMoney,
			String operId, String operDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, Double divsionMoney, Double noReimMoney,
			Set maintStationReimbursements, Set maintDivisionReimbursements,
			Set projectReimbursements, Set noReimbursements) {
		this.billno = billno;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.reimbursPeople = reimbursPeople;
		this.reimbursDate = reimbursDate;
		this.totalAmount = totalAmount;
		this.projectMoney = projectMoney;
		this.stationMoney = stationMoney;
		this.operId = operId;
		this.operDate = operDate;
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
		this.divsionMoney = divsionMoney;
		this.noReimMoney = noReimMoney;
		this.maintStationReimbursements = maintStationReimbursements;
		this.maintDivisionReimbursements = maintDivisionReimbursements;
		this.projectReimbursements = projectReimbursements;
		this.noReimbursements = noReimbursements;
	}

	// Property accessors

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getMaintDivision() {
		return this.maintDivision;
	}

	public void setMaintDivision(String maintDivision) {
		this.maintDivision = maintDivision;
	}

	public String getMaintStation() {
		return this.maintStation;
	}

	public void setMaintStation(String maintStation) {
		this.maintStation = maintStation;
	}

	public String getReimbursPeople() {
		return this.reimbursPeople;
	}

	public void setReimbursPeople(String reimbursPeople) {
		this.reimbursPeople = reimbursPeople;
	}

	public String getReimbursDate() {
		return this.reimbursDate;
	}

	public void setReimbursDate(String reimbursDate) {
		this.reimbursDate = reimbursDate;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getProjectMoney() {
		return this.projectMoney;
	}

	public void setProjectMoney(Double projectMoney) {
		this.projectMoney = projectMoney;
	}

	public Double getStationMoney() {
		return this.stationMoney;
	}

	public void setStationMoney(Double stationMoney) {
		this.stationMoney = stationMoney;
	}

	public String getOperId() {
		return this.operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	public String getOperDate() {
		return this.operDate;
	}

	public void setOperDate(String operDate) {
		this.operDate = operDate;
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

	public Double getR7() {
		return this.r7;
	}

	public void setR7(Double r7) {
		this.r7 = r7;
	}

	public Double getR8() {
		return this.r8;
	}

	public void setR8(Double r8) {
		this.r8 = r8;
	}

	public Integer getR9() {
		return this.r9;
	}

	public void setR9(Integer r9) {
		this.r9 = r9;
	}

	public Integer getR10() {
		return this.r10;
	}

	public void setR10(Integer r10) {
		this.r10 = r10;
	}

	public Double getDivsionMoney() {
		return this.divsionMoney;
	}

	public void setDivsionMoney(Double divsionMoney) {
		this.divsionMoney = divsionMoney;
	}

	public Double getNoReimMoney() {
		return this.noReimMoney;
	}

	public void setNoReimMoney(Double noReimMoney) {
		this.noReimMoney = noReimMoney;
	}

	public Set getMaintStationReimbursements() {
		return this.maintStationReimbursements;
	}

	public void setMaintStationReimbursements(Set maintStationReimbursements) {
		this.maintStationReimbursements = maintStationReimbursements;
	}

	public Set getMaintDivisionReimbursements() {
		return this.maintDivisionReimbursements;
	}

	public void setMaintDivisionReimbursements(Set maintDivisionReimbursements) {
		this.maintDivisionReimbursements = maintDivisionReimbursements;
	}

	public Set getProjectReimbursements() {
		return this.projectReimbursements;
	}

	public void setProjectReimbursements(Set projectReimbursements) {
		this.projectReimbursements = projectReimbursements;
	}

	public Set getNoReimbursements() {
		return this.noReimbursements;
	}

	public void setNoReimbursements(Set noReimbursements) {
		this.noReimbursements = noReimbursements;
	}

}