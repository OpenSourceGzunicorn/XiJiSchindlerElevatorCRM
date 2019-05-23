package com.gzunicorn.hibernate.infomanager.elevatortransfercasebhtype;

/**
 * AbstractHandoverElevatorSpecialRegister entity provides the base persistence
 * definition of the HandoverElevatorSpecialRegister entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractElevatorTransferCaseBhType implements
		java.io.Serializable {

	// Fields
	private Integer numid;
	private String billno;
	private String bhType;
	private String bhRem;
	private String bhDate;
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
	public AbstractElevatorTransferCaseBhType() {
	}
	
	public AbstractElevatorTransferCaseBhType(Integer numid) {
		this.numid = numid;
	}

	/** full constructor */
	public AbstractElevatorTransferCaseBhType(Integer numid,String billno,String bhType,
			String bhRem, String bhDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10) {
		this.numid = numid;
		this.billno=billno;
		this.bhType = bhType;
		this.bhRem = bhRem;
		this.bhDate = bhDate;
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

	public Integer getNumid() {
		return numid;
	}

	public void setNumid(Integer numid) {
		this.numid = numid;
	}
	
	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getBhType() {
		return bhType;
	}

	public void setBhType(String bhType) {
		this.bhType = bhType;
	}

	public String getBhRem() {
		return bhRem;
	}

	public void setBhRem(String bhRem) {
		this.bhRem = bhRem;
	}

	public String getBhDate() {
		return bhDate;
	}

	public void setBhDate(String bhDate) {
		this.bhDate = bhDate;
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