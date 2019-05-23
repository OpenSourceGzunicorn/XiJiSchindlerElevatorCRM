package com.gzunicorn.hibernate.costmanagement.maintstationreimbursement;

import java.util.HashSet;
import java.util.Set;

import com.gzunicorn.hibernate.costmanagement.reimbursexpensemanag.ReimbursExpenseManag;

/**
 * AbstractMaintStationReimbursement entity provides the base persistence
 * definition of the MaintStationReimbursement entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractMaintStationReimbursement implements
		java.io.Serializable {

	// Fields

	private String jnlno;
	private ReimbursExpenseManag reimbursExpenseManag;
	private String reimburType;
	private Double money;
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
	private String maintDivisionBx;
	private String maintStationBx;
	private Set maintStationReimbursementDetails = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractMaintStationReimbursement() {
	}

	/** minimal constructor */
	public AbstractMaintStationReimbursement(String jnlno, String reimburType,
			Double money, String maintDivisionBx, String maintStationBx) {
		this.jnlno = jnlno;
		this.reimburType = reimburType;
		this.money = money;
		this.maintDivisionBx = maintDivisionBx;
		this.maintStationBx = maintStationBx;
	}

	/** full constructor */
	public AbstractMaintStationReimbursement(String jnlno,
			ReimbursExpenseManag reimbursExpenseManag, String reimburType,
			Double money, String rem, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String maintDivisionBx, String maintStationBx,
			Set maintStationReimbursementDetails) {
		this.jnlno = jnlno;
		this.reimbursExpenseManag = reimbursExpenseManag;
		this.reimburType = reimburType;
		this.money = money;
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
		this.maintDivisionBx = maintDivisionBx;
		this.maintStationBx = maintStationBx;
		this.maintStationReimbursementDetails = maintStationReimbursementDetails;
	}

	// Property accessors

	public String getJnlno() {
		return this.jnlno;
	}

	public void setJnlno(String jnlno) {
		this.jnlno = jnlno;
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

	public String getMaintDivisionBx() {
		return this.maintDivisionBx;
	}

	public void setMaintDivisionBx(String maintDivisionBx) {
		this.maintDivisionBx = maintDivisionBx;
	}

	public String getMaintStationBx() {
		return this.maintStationBx;
	}

	public void setMaintStationBx(String maintStationBx) {
		this.maintStationBx = maintStationBx;
	}

	public Set getMaintStationReimbursementDetails() {
		return this.maintStationReimbursementDetails;
	}

	public void setMaintStationReimbursementDetails(
			Set maintStationReimbursementDetails) {
		this.maintStationReimbursementDetails = maintStationReimbursementDetails;
	}

}