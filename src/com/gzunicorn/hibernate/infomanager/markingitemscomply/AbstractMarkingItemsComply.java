package com.gzunicorn.hibernate.infomanager.markingitemscomply;

/**
 * AbstractMarkingItemsComply entity provides the base persistence definition of
 * the MarkingItemsComply entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractMarkingItemsComply implements
		java.io.Serializable {

	// Fields

	private Integer numno;
	private String billno;
	private String mtId;
	private Double fraction2;
	private Double fraction;
	private String appendix;
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
	public AbstractMarkingItemsComply() {
	}

	/** minimal constructor */
	public AbstractMarkingItemsComply(String billno, String mtId,
			Double fraction2, Double fraction) {
		this.billno = billno;
		this.mtId = mtId;
		this.fraction2 = fraction2;
		this.fraction = fraction;
	}

	/** full constructor */
	public AbstractMarkingItemsComply(String billno, String mtId,
			Double fraction2, Double fraction, String appendix, String r1,
			String r2, String r3, String r4, String r5, Double r6, Double r7,
			Double r8, Integer r9, Integer r10) {
		this.billno = billno;
		this.mtId = mtId;
		this.fraction2 = fraction2;
		this.fraction = fraction;
		this.appendix = appendix;
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

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getMtId() {
		return this.mtId;
	}

	public void setMtId(String mtId) {
		this.mtId = mtId;
	}

	public Double getFraction2() {
		return this.fraction2;
	}

	public void setFraction2(Double fraction2) {
		this.fraction2 = fraction2;
	}

	public Double getFraction() {
		return this.fraction;
	}

	public void setFraction(Double fraction) {
		this.fraction = fraction;
	}

	public String getAppendix() {
		return this.appendix;
	}

	public void setAppendix(String appendix) {
		this.appendix = appendix;
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