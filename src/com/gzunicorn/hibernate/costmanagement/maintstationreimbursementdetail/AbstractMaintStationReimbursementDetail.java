package com.gzunicorn.hibernate.costmanagement.maintstationreimbursementdetail;

import com.gzunicorn.hibernate.costmanagement.maintstationreimbursement.MaintStationReimbursement;

/**
 * AbstractMaintStationReimbursementDetail entity provides the base persistence
 * definition of the MaintStationReimbursementDetail entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractMaintStationReimbursementDetail implements
		java.io.Serializable {

	// Fields

	private Integer rowid;
	private MaintStationReimbursement maintStationReimbursement;
	private String maintContractNo;
	private Integer num;
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

	// Constructors

	/** default constructor */
	public AbstractMaintStationReimbursementDetail() {
	}

	/** minimal constructor */
	public AbstractMaintStationReimbursementDetail(String maintContractNo,
			Integer num, Double money, String maintDivisionBx,
			String maintStationBx) {
		this.maintContractNo = maintContractNo;
		this.num = num;
		this.money = money;
		this.maintDivisionBx = maintDivisionBx;
		this.maintStationBx = maintStationBx;
	}

	/** full constructor */
	public AbstractMaintStationReimbursementDetail(
			MaintStationReimbursement maintStationReimbursement,
			String maintContractNo, Integer num, Double money, String rem,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10,
			String maintDivisionBx, String maintStationBx) {
		this.maintStationReimbursement = maintStationReimbursement;
		this.maintContractNo = maintContractNo;
		this.num = num;
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
	}

	// Property accessors

	public Integer getRowid() {
		return this.rowid;
	}

	public void setRowid(Integer rowid) {
		this.rowid = rowid;
	}

	public MaintStationReimbursement getMaintStationReimbursement() {
		return this.maintStationReimbursement;
	}

	public void setMaintStationReimbursement(
			MaintStationReimbursement maintStationReimbursement) {
		this.maintStationReimbursement = maintStationReimbursement;
	}

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
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

}