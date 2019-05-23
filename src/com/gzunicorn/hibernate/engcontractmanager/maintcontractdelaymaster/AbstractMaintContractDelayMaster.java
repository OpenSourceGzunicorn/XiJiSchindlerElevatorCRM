package com.gzunicorn.hibernate.engcontractmanager.maintcontractdelaymaster;

/**
 * AbstractMaintContractDelayMaster entity provides the base persistence
 * definition of the MaintContractDelayMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractMaintContractDelayMaster implements
		java.io.Serializable {

	// Fields

	private String jnlno;
	private String billno;
	private Integer status;
	private Long tokenId;
	private String processName;
	private String submitType;
	private String operId;
	private String operDate;
	private String maintStation;
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
	private String auditOperid;
	private String auditStatus;
	private String auditDate;
	private String auditRem;
	private String workisdisplay;
	private String workisdisplay2;
	private String rem;

	// Constructors

	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getWorkisdisplay2() {
		return workisdisplay2;
	}

	public void setWorkisdisplay2(String workisdisplay2) {
		this.workisdisplay2 = workisdisplay2;
	}

	public String getWorkisdisplay() {
		return workisdisplay;
	}

	public void setWorkisdisplay(String workisdisplay) {
		this.workisdisplay = workisdisplay;
	}

	/** default constructor */
	public AbstractMaintContractDelayMaster() {
	}

	/** minimal constructor */
	public AbstractMaintContractDelayMaster(String jnlno, Integer status,
			Long tokenId, String processName, String submitType, String operId,
			String operDate, String maintStation) {
		this.jnlno = jnlno;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.submitType = submitType;
		this.operId = operId;
		this.operDate = operDate;
		this.maintStation = maintStation;
	}

	/** full constructor */
	public AbstractMaintContractDelayMaster(String jnlno, String billno,
			Integer status, Long tokenId, String processName,
			String submitType, String operId, String operDate,
			String maintStation, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String auditOperid, String auditStatus,
			String auditDate, String auditRem,
			String workisdisplay,String workisdisplay2,String rem) {
		this.jnlno = jnlno;
		this.billno = billno;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.submitType = submitType;
		this.operId = operId;
		this.operDate = operDate;
		this.maintStation = maintStation;
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
		this.auditOperid = auditOperid;
		this.auditStatus = auditStatus;
		this.auditDate = auditDate;
		this.auditRem = auditRem;
		this.workisdisplay = workisdisplay;
		this.workisdisplay2 = workisdisplay2;
		this.rem=rem;
	}

	// Property accessors

	public String getJnlno() {
		return this.jnlno;
	}

	public void setJnlno(String jnlno) {
		this.jnlno = jnlno;
	}

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
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

	public String getMaintStation() {
		return this.maintStation;
	}

	public void setMaintStation(String maintStation) {
		this.maintStation = maintStation;
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

}