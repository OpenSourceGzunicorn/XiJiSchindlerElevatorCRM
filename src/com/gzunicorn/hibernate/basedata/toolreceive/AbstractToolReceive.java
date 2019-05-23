package com.gzunicorn.hibernate.basedata.toolreceive;

/**
 * AbstractToolReceive entity provides the base persistence definition of the
 * ToolReceive entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractToolReceive implements java.io.Serializable {

	// Fields

	private Integer numNo;
	private String billno;
	private String toolId;
	private String toolName;
	private String toolParam;
	private Double toolnum;
	private String operName;
	private String operDate;
	private String isCharge;
	private String isLiquidation;
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

	public String getToolId() {
		return toolId;
	}

	public void setToolId(String toolId) {
		this.toolId = toolId;
	}

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public String getToolParam() {
		return toolParam;
	}

	public void setToolParam(String toolParam) {
		this.toolParam = toolParam;
	}

	public Double getToolnum() {
		return toolnum;
	}

	public void setToolnum(Double toolnum) {
		this.toolnum = toolnum;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getOperDate() {
		return operDate;
	}

	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}

	public String getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(String isCharge) {
		this.isCharge = isCharge;
	}

	public String getIsLiquidation() {
		return isLiquidation;
	}

	public void setIsLiquidation(String isLiquidation) {
		this.isLiquidation = isLiquidation;
	}

	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getR1() {
		return r1;
	}

	public void setR1(String r1) {
		this.r1 = r1;
	}

	public String getR2() {
		return r2;
	}

	public void setR2(String r2) {
		this.r2 = r2;
	}

	public String getR3() {
		return r3;
	}

	public void setR3(String r3) {
		this.r3 = r3;
	}

	public String getR4() {
		return r4;
	}

	public void setR4(String r4) {
		this.r4 = r4;
	}

	public String getR5() {
		return r5;
	}

	public void setR5(String r5) {
		this.r5 = r5;
	}

	public Double getR6() {
		return r6;
	}

	public void setR6(Double r6) {
		this.r6 = r6;
	}

	public Double getR7() {
		return r7;
	}

	public void setR7(Double r7) {
		this.r7 = r7;
	}

	public Double getR8() {
		return r8;
	}

	public void setR8(Double r8) {
		this.r8 = r8;
	}

	public Integer getR9() {
		return r9;
	}

	public void setR9(Integer r9) {
		this.r9 = r9;
	}

	public Integer getR10() {
		return r10;
	}

	public void setR10(Integer r10) {
		this.r10 = r10;
	}

	/** default constructor */
	public AbstractToolReceive() {
	}

	/** minimal constructor */
	public AbstractToolReceive(Integer numNo, String billno, String toolId, String toolName,
			String toolParam, Double toolnum, String operName,
			String operDate) {
		this.numNo = numNo;
		this.billno = billno;
		this.toolId = toolId;
		this.toolName = toolName;
		this.toolParam = toolParam;
		this.toolnum = toolnum;
		this.operName = operName;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractToolReceive(Integer numNo, String billno, String toolId, String toolName,
			String toolParam, Double toolnum, String operName,
			String operDate, String isCharge, String isLiquidation, String rem, 
			String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		this.numNo = numNo;
		this.billno = billno;
		this.toolId = toolId;
		this.toolName = toolName;
		this.toolParam = toolParam;
		this.toolnum = toolnum;
		this.operName = operName;
		this.operDate = operDate;
		this.isCharge = isCharge;
		this.isLiquidation = isLiquidation;
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

	public Integer getNumNo() {
		return this.numNo;
	}

	public void setNumNo(Integer numNo) {
		this.numNo = numNo;
	}

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}


}