package com.gzunicorn.hibernate.costmanagement.noreimbursement;

import com.gzunicorn.hibernate.costmanagement.reimbursexpensemanag.ReimbursExpenseManag;

/**
 * AbstractNoReimbursement entity provides the base persistence definition of
 * the NoReimbursement entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractNoReimbursement implements java.io.Serializable {

	// Fields

	private Integer rowid;
	private ReimbursExpenseManag reimbursExpenseManag;
	private String reimburType;
	private Double money;
	private String belongsDepart;
	private String rem;
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

	// Constructors

	/** default constructor */
	public AbstractNoReimbursement() {
	}

	/** minimal constructor */
	public AbstractNoReimbursement(String reimburType, Double money,
			String belongsDepart) {
		this.reimburType = reimburType;
		this.money = money;
		this.belongsDepart = belongsDepart;
	}

	/** full constructor */
	public AbstractNoReimbursement(ReimbursExpenseManag reimbursExpenseManag,
			String reimburType, Double money, String belongsDepart, String rem,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		this.reimbursExpenseManag = reimbursExpenseManag;
		this.reimburType = reimburType;
		this.money = money;
		this.belongsDepart = belongsDepart;
		this.rem = rem;
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

	public Integer getRowid() {
		return this.rowid;
	}

	public void setRowid(Integer rowid) {
		this.rowid = rowid;
	}

	public ReimbursExpenseManag getReimbursExpenseManag() {
		return this.reimbursExpenseManag;
	}

	public void setReimbursExpenseManag(
			ReimbursExpenseManag reimbursExpenseManag) {
		this.reimbursExpenseManag = reimbursExpenseManag;
	}

	public String getReimburType() {
		return this.reimburType;
	}

	public void setReimburType(String reimburType) {
		this.reimburType = reimburType;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getBelongsDepart() {
		return this.belongsDepart;
	}

	public void setBelongsDepart(String belongsDepart) {
		this.belongsDepart = belongsDepart;
	}

	public String getRem() {
		return this.rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
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

}