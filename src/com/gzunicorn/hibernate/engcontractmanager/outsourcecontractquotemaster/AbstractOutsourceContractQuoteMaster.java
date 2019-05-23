package com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquotemaster;

/**
 * AbstractOutsourceContractQuoteMaster entity provides the base persistence
 * definition of the OutsourceContractQuoteMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractOutsourceContractQuoteMaster implements
		java.io.Serializable {

	// Fields

	private String billNo;
	private String wgBillno;
	private Integer status;
	private Long tokenId;
	private String processName;
	private String submitType;
	private String companyId;
	private Double standardPrice;
	private Double realPrice;
	private Double markups;
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
	private String maintContractNo;
	private String maintDivision;
	private String maintStation;
	private String rem;

	// Constructors

	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	/** default constructor */
	public AbstractOutsourceContractQuoteMaster() {
	}

	/** minimal constructor */
	public AbstractOutsourceContractQuoteMaster(String billNo, String wgBillno,
			Integer status, Long tokenId, String processName,
			String submitType, String companyId, Double standardPrice,
			Double realPrice, Double markups, String operId, String operDate) {
		this.billNo = billNo;
		this.wgBillno = wgBillno;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.submitType = submitType;
		this.companyId = companyId;
		this.standardPrice = standardPrice;
		this.realPrice = realPrice;
		this.markups = markups;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractOutsourceContractQuoteMaster(String billNo, String wgBillno,
			Integer status, Long tokenId, String processName,
			String submitType, String companyId, Double standardPrice,
			Double realPrice, Double markups, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10,
			String maintContractNo, String maintDivision, String maintStation,
			String rem) {
		this.billNo = billNo;
		this.wgBillno = wgBillno;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.submitType = submitType;
		this.companyId = companyId;
		this.standardPrice = standardPrice;
		this.realPrice = realPrice;
		this.markups = markups;
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
		this.maintContractNo = maintContractNo;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.rem = rem;
	}

	// Property accessors

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getWgBillno() {
		return this.wgBillno;
	}

	public void setWgBillno(String wgBillno) {
		this.wgBillno = wgBillno;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}

	public String getProcessName() {
		return this.processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Double getStandardPrice() {
		return this.standardPrice;
	}

	public void setStandardPrice(Double standardPrice) {
		this.standardPrice = standardPrice;
	}

	public Double getRealPrice() {
		return this.realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}

	public Double getMarkups() {
		return this.markups;
	}

	public void setMarkups(Double markups) {
		this.markups = markups;
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

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
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

}