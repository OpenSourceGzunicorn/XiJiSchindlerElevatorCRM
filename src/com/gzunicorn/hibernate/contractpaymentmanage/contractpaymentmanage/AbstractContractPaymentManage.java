package com.gzunicorn.hibernate.contractpaymentmanage.contractpaymentmanage;

/**
 * AbstractContractPaymentManage entity provides the base persistence definition
 * of the ContractPaymentManage entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractContractPaymentManage implements
		java.io.Serializable {

	// Fields

	private String jnlNo;
	private String ctJnlNo;
	private String billNo;
	private String entrustContractNo;
	private String companyId;
	private String paragraphNo;
	private Double paragraphMoney;
	private String paragraphDate;
	private String maintDivision;
	private String operId;
	private String operDate;
	private String rem;
	private String submitType;
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
	private Integer status;
	private Long tokenId;
	private String processName;
	private String bydAuditDate;
	private String bydAuditEvaluate;
	private String bydAuditRem;
	private String hfAuditDate;
	private Integer hfAuditNum;
	private Integer hfAuditNum2;
	private String hfAuditRem;
	private String rxAuditDate;
	private Integer rxAuditNum;
	private Integer rxAuditNum2;
	private String rxAuditRem;
	private String jjthAuditDate;
	private String jjthAuditEvaluate;
	private String jjthAuditRem;
	private String fbzAuditDate;
	private String fbzAuditEvaluate;
	private Double debitMoney;
	private String fbzAuditRem;
	private String zbzAuditDate;
	private String zbzAuditRem;

	// Constructors

	/** default constructor */
	public AbstractContractPaymentManage() {
	}

	/** minimal constructor */
	public AbstractContractPaymentManage(String jnlNo, String ctJnlNo,
			String billNo, String entrustContractNo, String companyId,
			String paragraphNo, Double paragraphMoney, String paragraphDate,
			String maintDivision, String operId, String operDate,
			String submitType, Integer status, Long tokenId, String processName) {
		this.jnlNo = jnlNo;
		this.ctJnlNo = ctJnlNo;
		this.billNo = billNo;
		this.entrustContractNo = entrustContractNo;
		this.companyId = companyId;
		this.paragraphNo = paragraphNo;
		this.paragraphMoney = paragraphMoney;
		this.paragraphDate = paragraphDate;
		this.maintDivision = maintDivision;
		this.operId = operId;
		this.operDate = operDate;
		this.submitType = submitType;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
	}

	/** full constructor */
	public AbstractContractPaymentManage(String jnlNo, String ctJnlNo,
			String billNo, String entrustContractNo, String companyId,
			String paragraphNo, Double paragraphMoney, String paragraphDate,
			String maintDivision, String operId, String operDate, String rem,
			String submitType, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, Integer status, Long tokenId, String processName,
			String bydAuditDate, String bydAuditEvaluate, String bydAuditRem,
			String hfAuditDate, Integer hfAuditNum, Integer hfAuditNum2,
			String hfAuditRem, String rxAuditDate, Integer rxAuditNum,
			Integer rxAuditNum2, String rxAuditRem, String jjthAuditDate,
			String jjthAuditEvaluate, String jjthAuditRem, String fbzAuditDate,
			String fbzAuditEvaluate, Double debitMoney, String fbzAuditRem,
			String zbzAuditDate, String zbzAuditRem) {
		this.jnlNo = jnlNo;
		this.ctJnlNo = ctJnlNo;
		this.billNo = billNo;
		this.entrustContractNo = entrustContractNo;
		this.companyId = companyId;
		this.paragraphNo = paragraphNo;
		this.paragraphMoney = paragraphMoney;
		this.paragraphDate = paragraphDate;
		this.maintDivision = maintDivision;
		this.operId = operId;
		this.operDate = operDate;
		this.rem = rem;
		this.submitType = submitType;
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
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.bydAuditDate = bydAuditDate;
		this.bydAuditEvaluate = bydAuditEvaluate;
		this.bydAuditRem = bydAuditRem;
		this.hfAuditDate = hfAuditDate;
		this.hfAuditNum = hfAuditNum;
		this.hfAuditNum2 = hfAuditNum2;
		this.hfAuditRem = hfAuditRem;
		this.rxAuditDate = rxAuditDate;
		this.rxAuditNum = rxAuditNum;
		this.rxAuditNum2 = rxAuditNum2;
		this.rxAuditRem = rxAuditRem;
		this.jjthAuditDate = jjthAuditDate;
		this.jjthAuditEvaluate = jjthAuditEvaluate;
		this.jjthAuditRem = jjthAuditRem;
		this.fbzAuditDate = fbzAuditDate;
		this.fbzAuditEvaluate = fbzAuditEvaluate;
		this.debitMoney = debitMoney;
		this.fbzAuditRem = fbzAuditRem;
		this.zbzAuditDate = zbzAuditDate;
		this.zbzAuditRem = zbzAuditRem;
	}

	// Property accessors

	public String getJnlNo() {
		return this.jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getCtJnlNo() {
		return this.ctJnlNo;
	}

	public void setCtJnlNo(String ctJnlNo) {
		this.ctJnlNo = ctJnlNo;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getEntrustContractNo() {
		return this.entrustContractNo;
	}

	public void setEntrustContractNo(String entrustContractNo) {
		this.entrustContractNo = entrustContractNo;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getParagraphNo() {
		return this.paragraphNo;
	}

	public void setParagraphNo(String paragraphNo) {
		this.paragraphNo = paragraphNo;
	}

	public Double getParagraphMoney() {
		return this.paragraphMoney;
	}

	public void setParagraphMoney(Double paragraphMoney) {
		this.paragraphMoney = paragraphMoney;
	}

	public String getParagraphDate() {
		return this.paragraphDate;
	}

	public void setParagraphDate(String paragraphDate) {
		this.paragraphDate = paragraphDate;
	}

	public String getMaintDivision() {
		return this.maintDivision;
	}

	public void setMaintDivision(String maintDivision) {
		this.maintDivision = maintDivision;
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

	public String getRem() {
		return this.rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
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

	public String getBydAuditDate() {
		return this.bydAuditDate;
	}

	public void setBydAuditDate(String bydAuditDate) {
		this.bydAuditDate = bydAuditDate;
	}

	public String getBydAuditEvaluate() {
		return this.bydAuditEvaluate;
	}

	public void setBydAuditEvaluate(String bydAuditEvaluate) {
		this.bydAuditEvaluate = bydAuditEvaluate;
	}

	public String getBydAuditRem() {
		return this.bydAuditRem;
	}

	public void setBydAuditRem(String bydAuditRem) {
		this.bydAuditRem = bydAuditRem;
	}

	public String getHfAuditDate() {
		return this.hfAuditDate;
	}

	public void setHfAuditDate(String hfAuditDate) {
		this.hfAuditDate = hfAuditDate;
	}

	public Integer getHfAuditNum() {
		return this.hfAuditNum;
	}

	public void setHfAuditNum(Integer hfAuditNum) {
		this.hfAuditNum = hfAuditNum;
	}

	public Integer getHfAuditNum2() {
		return this.hfAuditNum2;
	}

	public void setHfAuditNum2(Integer hfAuditNum2) {
		this.hfAuditNum2 = hfAuditNum2;
	}

	public String getHfAuditRem() {
		return this.hfAuditRem;
	}

	public void setHfAuditRem(String hfAuditRem) {
		this.hfAuditRem = hfAuditRem;
	}

	public String getRxAuditDate() {
		return this.rxAuditDate;
	}

	public void setRxAuditDate(String rxAuditDate) {
		this.rxAuditDate = rxAuditDate;
	}

	public Integer getRxAuditNum() {
		return this.rxAuditNum;
	}

	public void setRxAuditNum(Integer rxAuditNum) {
		this.rxAuditNum = rxAuditNum;
	}

	public Integer getRxAuditNum2() {
		return this.rxAuditNum2;
	}

	public void setRxAuditNum2(Integer rxAuditNum2) {
		this.rxAuditNum2 = rxAuditNum2;
	}

	public String getRxAuditRem() {
		return this.rxAuditRem;
	}

	public void setRxAuditRem(String rxAuditRem) {
		this.rxAuditRem = rxAuditRem;
	}

	public String getJjthAuditDate() {
		return this.jjthAuditDate;
	}

	public void setJjthAuditDate(String jjthAuditDate) {
		this.jjthAuditDate = jjthAuditDate;
	}

	public String getJjthAuditEvaluate() {
		return this.jjthAuditEvaluate;
	}

	public void setJjthAuditEvaluate(String jjthAuditEvaluate) {
		this.jjthAuditEvaluate = jjthAuditEvaluate;
	}

	public String getJjthAuditRem() {
		return this.jjthAuditRem;
	}

	public void setJjthAuditRem(String jjthAuditRem) {
		this.jjthAuditRem = jjthAuditRem;
	}

	public String getFbzAuditDate() {
		return this.fbzAuditDate;
	}

	public void setFbzAuditDate(String fbzAuditDate) {
		this.fbzAuditDate = fbzAuditDate;
	}

	public String getFbzAuditEvaluate() {
		return this.fbzAuditEvaluate;
	}

	public void setFbzAuditEvaluate(String fbzAuditEvaluate) {
		this.fbzAuditEvaluate = fbzAuditEvaluate;
	}

	public Double getDebitMoney() {
		return this.debitMoney;
	}

	public void setDebitMoney(Double debitMoney) {
		this.debitMoney = debitMoney;
	}

	public String getFbzAuditRem() {
		return this.fbzAuditRem;
	}

	public void setFbzAuditRem(String fbzAuditRem) {
		this.fbzAuditRem = fbzAuditRem;
	}

	public String getZbzAuditDate() {
		return this.zbzAuditDate;
	}

	public void setZbzAuditDate(String zbzAuditDate) {
		this.zbzAuditDate = zbzAuditDate;
	}

	public String getZbzAuditRem() {
		return this.zbzAuditRem;
	}

	public void setZbzAuditRem(String zbzAuditRem) {
		this.zbzAuditRem = zbzAuditRem;
	}

}