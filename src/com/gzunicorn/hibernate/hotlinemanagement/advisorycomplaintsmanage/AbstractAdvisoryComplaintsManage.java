package com.gzunicorn.hibernate.hotlinemanagement.advisorycomplaintsmanage;

/**
 * AbstractAdvisoryComplaintsManage entity provides the base persistence
 * definition of the AdvisoryComplaintsManage entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractAdvisoryComplaintsManage implements
		java.io.Serializable {

	// Fields

	private String processSingleNo;
	private String messageSource;
	private String receivePer;
	private String receiveDate;
	private String feedbackPer;
	private String feedbackTel;
	private String problemDesc;
	private String submitType;
	private String processPer;
	private String projectName;
	private String contractNo;
	private String infoNo;
	private String elevatorNo;
	private String octId;
	private String factoryName;
	private String dutyDepar;
	private String carryOutSituat;
	private String processType;
	private String issueSort1;
	private String issueSort2;
	private String issueSort3;
	private String issueSort4;
	private String partsName;
	private String qualityIndex;
	private String occId;
	private String assessNo;
	private String ocaId;
	private String processDifficulty;
	private String isClose;
	private String completeDate;
	private String processTime;
	private String auditType;
	private String auditOperid;
	private String auditDate;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private Double r6;
	private Integer r7;
	private Integer r8;
	private Double r9;
	private Double r10;
	private String processDate;
	private String isEntrust;
	private String dispatchType;

	// Constructors


	/** default constructor */
	public AbstractAdvisoryComplaintsManage() {
	}

	/** minimal constructor */
	public AbstractAdvisoryComplaintsManage(String processSingleNo,
			String submitType, String processType, String auditType) {
		this.processSingleNo = processSingleNo;
		this.submitType = submitType;
		this.processType = processType;
		this.auditType = auditType;
	}

	/** full constructor */
	public AbstractAdvisoryComplaintsManage(String processSingleNo,
			String messageSource, String receivePer, String receiveDate,
			String feedbackPer, String feedbackTel, String problemDesc,
			String submitType, String processPer, String projectName,
			String contractNo, String infoNo, String elevatorNo, String octId,
			String factoryName, String dutyDepar, String carryOutSituat,
			String processType, String issueSort1, String issueSort2,
			String issueSort3, String issueSort4, String partsName,
			String qualityIndex, String occId, String assessNo, String ocaId,
			String processDifficulty, String isClose, String completeDate,
			String processTime, String auditType, String auditOperid,
			String auditDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9,
			Double r10, String processDate,String isEntrust, String dispatchType) {
		this.processSingleNo = processSingleNo;
		this.messageSource = messageSource;
		this.receivePer = receivePer;
		this.receiveDate = receiveDate;
		this.feedbackPer = feedbackPer;
		this.feedbackTel = feedbackTel;
		this.problemDesc = problemDesc;
		this.submitType = submitType;
		this.processPer = processPer;
		this.projectName = projectName;
		this.contractNo = contractNo;
		this.infoNo = infoNo;
		this.elevatorNo = elevatorNo;
		this.octId = octId;
		this.factoryName = factoryName;
		this.dutyDepar = dutyDepar;
		this.carryOutSituat = carryOutSituat;
		this.processType = processType;
		this.issueSort1 = issueSort1;
		this.issueSort2 = issueSort2;
		this.issueSort3 = issueSort3;
		this.issueSort4 = issueSort4;
		this.partsName = partsName;
		this.qualityIndex = qualityIndex;
		this.occId = occId;
		this.assessNo = assessNo;
		this.ocaId = ocaId;
		this.processDifficulty = processDifficulty;
		this.isClose = isClose;
		this.completeDate = completeDate;
		this.processTime = processTime;
		this.auditType = auditType;
		this.auditOperid = auditOperid;
		this.auditDate = auditDate;
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
		this.processDate = processDate;
		this.isEntrust=isEntrust;
		this.dispatchType=dispatchType;
	}

	// Property accessors

	public String getProcessSingleNo() {
		return this.processSingleNo;
	}

	public void setProcessSingleNo(String processSingleNo) {
		this.processSingleNo = processSingleNo;
	}

	public String getMessageSource() {
		return this.messageSource;
	}

	public void setMessageSource(String messageSource) {
		this.messageSource = messageSource;
	}

	public String getReceivePer() {
		return this.receivePer;
	}

	public void setReceivePer(String receivePer) {
		this.receivePer = receivePer;
	}

	public String getReceiveDate() {
		return this.receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getFeedbackPer() {
		return this.feedbackPer;
	}

	public void setFeedbackPer(String feedbackPer) {
		this.feedbackPer = feedbackPer;
	}

	public String getFeedbackTel() {
		return this.feedbackTel;
	}

	public void setFeedbackTel(String feedbackTel) {
		this.feedbackTel = feedbackTel;
	}

	public String getProblemDesc() {
		return this.problemDesc;
	}

	public void setProblemDesc(String problemDesc) {
		this.problemDesc = problemDesc;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getProcessPer() {
		return this.processPer;
	}

	public void setProcessPer(String processPer) {
		this.processPer = processPer;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getInfoNo() {
		return this.infoNo;
	}

	public void setInfoNo(String infoNo) {
		this.infoNo = infoNo;
	}

	public String getElevatorNo() {
		return this.elevatorNo;
	}

	public void setElevatorNo(String elevatorNo) {
		this.elevatorNo = elevatorNo;
	}

	public String getOctId() {
		return this.octId;
	}

	public void setOctId(String octId) {
		this.octId = octId;
	}

	public String getFactoryName() {
		return this.factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getDutyDepar() {
		return this.dutyDepar;
	}

	public void setDutyDepar(String dutyDepar) {
		this.dutyDepar = dutyDepar;
	}

	public String getCarryOutSituat() {
		return this.carryOutSituat;
	}

	public void setCarryOutSituat(String carryOutSituat) {
		this.carryOutSituat = carryOutSituat;
	}

	public String getProcessType() {
		return this.processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getIssueSort1() {
		return this.issueSort1;
	}

	public void setIssueSort1(String issueSort1) {
		this.issueSort1 = issueSort1;
	}

	public String getIssueSort2() {
		return this.issueSort2;
	}

	public void setIssueSort2(String issueSort2) {
		this.issueSort2 = issueSort2;
	}

	public String getIssueSort3() {
		return this.issueSort3;
	}

	public void setIssueSort3(String issueSort3) {
		this.issueSort3 = issueSort3;
	}

	public String getIssueSort4() {
		return this.issueSort4;
	}

	public void setIssueSort4(String issueSort4) {
		this.issueSort4 = issueSort4;
	}

	public String getPartsName() {
		return this.partsName;
	}

	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}

	public String getQualityIndex() {
		return this.qualityIndex;
	}

	public void setQualityIndex(String qualityIndex) {
		this.qualityIndex = qualityIndex;
	}

	public String getOccId() {
		return this.occId;
	}

	public void setOccId(String occId) {
		this.occId = occId;
	}

	public String getAssessNo() {
		return this.assessNo;
	}

	public void setAssessNo(String assessNo) {
		this.assessNo = assessNo;
	}

	public String getOcaId() {
		return this.ocaId;
	}

	public void setOcaId(String ocaId) {
		this.ocaId = ocaId;
	}

	public String getProcessDifficulty() {
		return this.processDifficulty;
	}

	public void setProcessDifficulty(String processDifficulty) {
		this.processDifficulty = processDifficulty;
	}

	public String getIsClose() {
		return this.isClose;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}

	public String getCompleteDate() {
		return this.completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public String getProcessTime() {
		return this.processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

	public String getAuditType() {
		return this.auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getAuditOperid() {
		return this.auditOperid;
	}

	public void setAuditOperid(String auditOperid) {
		this.auditOperid = auditOperid;
	}

	public String getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
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

	public Integer getR7() {
		return this.r7;
	}

	public void setR7(Integer r7) {
		this.r7 = r7;
	}

	public Integer getR8() {
		return this.r8;
	}

	public void setR8(Integer r8) {
		this.r8 = r8;
	}

	public Double getR9() {
		return this.r9;
	}

	public void setR9(Double r9) {
		this.r9 = r9;
	}

	public Double getR10() {
		return this.r10;
	}

	public void setR10(Double r10) {
		this.r10 = r10;
	}

	public String getProcessDate() {
		return this.processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	public String getIsEntrust() {
		return isEntrust;
	}

	public void setIsEntrust(String isEntrust) {
		this.isEntrust = isEntrust;
	}

	public String getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}
}