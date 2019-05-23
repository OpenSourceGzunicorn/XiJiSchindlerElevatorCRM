package com.gzunicorn.hibernate.contractpayment.contractinvoicemanage;

import com.gzunicorn.hibernate.contractpayment.procontractarfeemaster.ProContractArfeeMaster;

/**
 * AbstractContractInvoiceManage entity provides the base persistence definition
 * of the ContractInvoiceManage entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractContractInvoiceManage implements
		java.io.Serializable {

	// Fields

	private String jnlNo;
	private String arfJnlNo;
	private String billNo;
	private String contractNo;
	private String contractType;
	private String companyId;
	private String invoiceNo;
	private String invoiceType;
	private Double invoiceMoney;
	private String invoiceDate;
	private String maintDivision;
	private String operId;
	private String operDate;
	private String rem;
	private String informDate;
	private String auditOperid;
	private String auditStatus;
	private String auditDate;
	private String auditRem;
	private String submitType;
	private String istbp;
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
	private String maintStation;
	private String invoiceName;
	private String maintScope;
	private String auditOperid2;
	private String auditStatus2;
	private String auditDate2;
	private String auditRem2;
	private String auditOperid3;
	private String auditStatus3;
	private String auditDate3;
	private String auditRem3;
	private String workisdisplay;
	private String auditOperid4;
	private String auditStatus4;
	private String auditDate4;
	private String auditRem4;
	
	public String getAuditOperid4() {
		return auditOperid4;
	}

	public void setAuditOperid4(String auditOperid4) {
		this.auditOperid4 = auditOperid4;
	}

	public String getAuditStatus4() {
		return auditStatus4;
	}

	public void setAuditStatus4(String auditStatus4) {
		this.auditStatus4 = auditStatus4;
	}

	public String getAuditDate4() {
		return auditDate4;
	}

	public void setAuditDate4(String auditDate4) {
		this.auditDate4 = auditDate4;
	}

	public String getAuditRem4() {
		return auditRem4;
	}

	public void setAuditRem4(String auditRem4) {
		this.auditRem4 = auditRem4;
	}

	public String getWorkisdisplay() {
		return workisdisplay;
	}

	public void setWorkisdisplay(String workisdisplay) {
		this.workisdisplay = workisdisplay;
	}

	public String getAuditOperid2() {
		return auditOperid2;
	}

	public void setAuditOperid2(String auditOperid2) {
		this.auditOperid2 = auditOperid2;
	}

	public String getAuditStatus2() {
		return auditStatus2;
	}

	public void setAuditStatus2(String auditStatus2) {
		this.auditStatus2 = auditStatus2;
	}

	public String getAuditDate2() {
		return auditDate2;
	}

	public void setAuditDate2(String auditDate2) {
		this.auditDate2 = auditDate2;
	}

	public String getAuditRem2() {
		return auditRem2;
	}

	public void setAuditRem2(String auditRem2) {
		this.auditRem2 = auditRem2;
	}

	public String getAuditOperid3() {
		return auditOperid3;
	}

	public void setAuditOperid3(String auditOperid3) {
		this.auditOperid3 = auditOperid3;
	}

	public String getAuditStatus3() {
		return auditStatus3;
	}

	public void setAuditStatus3(String auditStatus3) {
		this.auditStatus3 = auditStatus3;
	}

	public String getAuditDate3() {
		return auditDate3;
	}

	public void setAuditDate3(String auditDate3) {
		this.auditDate3 = auditDate3;
	}

	public String getAuditRem3() {
		return auditRem3;
	}

	public void setAuditRem3(String auditRem3) {
		this.auditRem3 = auditRem3;
	}


	// Constructors

	/** default constructor */
	public AbstractContractInvoiceManage() {
	}

	/** minimal constructor */
	public AbstractContractInvoiceManage(String jnlNo,
			String arfJnlNo, String billNo,
			String contractNo, String contractType, String companyId,
			String invoiceNo, String invoiceType, Double invoiceMoney,
			String invoiceDate, String maintDivision, String operId,
			String operDate, String submitType) {
		this.jnlNo = jnlNo;
		this.arfJnlNo = arfJnlNo;
		this.billNo = billNo;
		this.contractNo = contractNo;
		this.contractType = contractType;
		this.companyId = companyId;
		this.invoiceNo = invoiceNo;
		this.invoiceType = invoiceType;
		this.invoiceMoney = invoiceMoney;
		this.invoiceDate = invoiceDate;
		this.maintDivision = maintDivision;
		this.operId = operId;
		this.operDate = operDate;
		this.submitType = submitType;
	}

	/** full constructor */
	public AbstractContractInvoiceManage(String jnlNo,
			String arfJnlNo, String billNo,
			String contractNo, String contractType, String companyId,
			String invoiceNo, String invoiceType, Double invoiceMoney,
			String invoiceDate, String maintDivision, String operId,
			String operDate, String rem, String informDate, String auditOperid,
			String auditStatus, String auditDate, String auditRem,
			String submitType, String istbp, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String maintStation, String invoiceName,
			String maintScope, String auditOperid2,
			String auditStatus2, String auditDate2, String auditRem2,
			String auditOperid3,String auditStatus3, String auditDate3, String auditRem3,
			String auditOperid4,String auditStatus4, String auditDate4, String auditRem4,
			String workisdisplay) {
		this.auditOperid4 = auditOperid4;
		this.auditStatus4 = auditStatus4;
		this.auditDate4 = auditDate4;
		this.auditRem4 = auditRem4;
		this.jnlNo = jnlNo;
		this.arfJnlNo = arfJnlNo;
		this.billNo = billNo;
		this.contractNo = contractNo;
		this.contractType = contractType;
		this.companyId = companyId;
		this.invoiceNo = invoiceNo;
		this.invoiceType = invoiceType;
		this.invoiceMoney = invoiceMoney;
		this.invoiceDate = invoiceDate;
		this.maintDivision = maintDivision;
		this.operId = operId;
		this.operDate = operDate;
		this.rem = rem;
		this.informDate = informDate;
		this.auditOperid = auditOperid;
		this.auditStatus = auditStatus;
		this.auditDate = auditDate;
		this.auditRem = auditRem;
		this.submitType = submitType;
		this.istbp = istbp;
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
		this.maintStation = maintStation;
		this.invoiceName = invoiceName;
		this.maintScope = maintScope;
		this.auditOperid2 = auditOperid2;
		this.auditStatus2 = auditStatus2;
		this.auditDate2 = auditDate2;
		this.auditRem2 = auditRem2;
		this.auditOperid3 = auditOperid3;
		this.auditStatus3 = auditStatus3;
		this.auditDate3 = auditDate3;
		this.auditRem3 = auditRem3;
		this.workisdisplay=workisdisplay;
	}

	// Property accessors

	public String getJnlNo() {
		return this.jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getArfJnlNo() {
		return this.arfJnlNo;
	}

	public void setArfJnlNo(String arfJnlNo) {
		this.arfJnlNo = arfJnlNo;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Double getInvoiceMoney() {
		return this.invoiceMoney;
	}

	public void setInvoiceMoney(Double invoiceMoney) {
		this.invoiceMoney = invoiceMoney;
	}

	public String getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
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

	public String getInformDate() {
		return this.informDate;
	}

	public void setInformDate(String informDate) {
		this.informDate = informDate;
	}

	public String getAuditOperid() {
		return this.auditOperid;
	}

	public void setAuditOperid(String auditOperid) {
		this.auditOperid = auditOperid;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditRem() {
		return this.auditRem;
	}

	public void setAuditRem(String auditRem) {
		this.auditRem = auditRem;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getIstbp() {
		return this.istbp;
	}

	public void setIstbp(String istbp) {
		this.istbp = istbp;
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

	public String getMaintStation() {
		return this.maintStation;
	}

	public void setMaintStation(String maintStation) {
		this.maintStation = maintStation;
	}

	public String getInvoiceName() {
		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getMaintScope() {
		return this.maintScope;
	}

	public void setMaintScope(String maintScope) {
		this.maintScope = maintScope;
	}

}