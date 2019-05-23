package com.gzunicorn.hibernate.infomanager.qualitycheckmanagement;

/**
 * AbstractQualityCheckManagement entity provides the base persistence
 * definition of the QualityCheckManagement entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractQualityCheckManagement implements
		java.io.Serializable {

	// Fields

	private String billno;
	private String maintDivision;
	private String maintStation;
	private String maintPersonnel;
	private String projectName;
	private String maintContractNo;
	private String elevatorNo;
	private String checksPeople;
	private String checksDateTime;
	private Double totalPoints;
	private String scoreLevel;
	private String submitType;
	private String supervOpinion;
	private String partMinisters;
	private String partMinistersRem;
	private String remDate;
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
	private String processStatus;
	private String salesContractNo;
	private String customerSignature;
	private Integer status;
	private Long tokenId;
	private String processName;
	private String superviseId;
	private String supervisePhone;
	private String assessRem;
	private Double deductMoney;
	private String elevatorType;
	private String personnelPhone;
	private String customerImage;

	// Constructors

	public String getCustomerImage() {
		return customerImage;
	}

	public void setCustomerImage(String customerImage) {
		this.customerImage = customerImage;
	}

	/** default constructor */
	public AbstractQualityCheckManagement() {
	}

	/** minimal constructor */
	public AbstractQualityCheckManagement(String billno, String maintDivision,
			String maintStation, String maintPersonnel, String projectName,
			String maintContractNo, String elevatorNo, String submitType,
			String operId, String operDate) {
		this.billno = billno;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.maintPersonnel = maintPersonnel;
		this.projectName = projectName;
		this.maintContractNo = maintContractNo;
		this.elevatorNo = elevatorNo;
		this.submitType = submitType;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractQualityCheckManagement(String billno, String maintDivision,
			String maintStation, String maintPersonnel, String projectName,
			String maintContractNo, String elevatorNo, String checksPeople,
			String checksDateTime, Double totalPoints, String scoreLevel,
			String submitType, String supervOpinion, String partMinisters,
			String partMinistersRem, String remDate, String operId,
			String operDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String processStatus, String salesContractNo,
			String customerSignature, Integer status, Long tokenId,
			String processName, String superviseId, String supervisePhone,
			String assessRem, Double deductMoney, String elevatorType,
			String personnelPhone,String customerImage) {
		this.customerImage=customerImage;
		this.billno = billno;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.maintPersonnel = maintPersonnel;
		this.projectName = projectName;
		this.maintContractNo = maintContractNo;
		this.elevatorNo = elevatorNo;
		this.checksPeople = checksPeople;
		this.checksDateTime = checksDateTime;
		this.totalPoints = totalPoints;
		this.scoreLevel = scoreLevel;
		this.submitType = submitType;
		this.supervOpinion = supervOpinion;
		this.partMinisters = partMinisters;
		this.partMinistersRem = partMinistersRem;
		this.remDate = remDate;
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
		this.processStatus = processStatus;
		this.salesContractNo = salesContractNo;
		this.customerSignature = customerSignature;
		this.status = status;
		this.tokenId = tokenId;
		this.processName = processName;
		this.superviseId = superviseId;
		this.supervisePhone = supervisePhone;
		this.assessRem = assessRem;
		this.deductMoney = deductMoney;
		this.elevatorType = elevatorType;
		this.personnelPhone = personnelPhone;
	}

	// Property accessors

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
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

	public String getMaintPersonnel() {
		return this.maintPersonnel;
	}

	public void setMaintPersonnel(String maintPersonnel) {
		this.maintPersonnel = maintPersonnel;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public String getElevatorNo() {
		return this.elevatorNo;
	}

	public void setElevatorNo(String elevatorNo) {
		this.elevatorNo = elevatorNo;
	}

	public String getChecksPeople() {
		return this.checksPeople;
	}

	public void setChecksPeople(String checksPeople) {
		this.checksPeople = checksPeople;
	}

	public String getChecksDateTime() {
		return this.checksDateTime;
	}

	public void setChecksDateTime(String checksDateTime) {
		this.checksDateTime = checksDateTime;
	}

	public Double getTotalPoints() {
		return this.totalPoints;
	}

	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}

	public String getScoreLevel() {
		return this.scoreLevel;
	}

	public void setScoreLevel(String scoreLevel) {
		this.scoreLevel = scoreLevel;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getSupervOpinion() {
		return this.supervOpinion;
	}

	public void setSupervOpinion(String supervOpinion) {
		this.supervOpinion = supervOpinion;
	}

	public String getPartMinisters() {
		return this.partMinisters;
	}

	public void setPartMinisters(String partMinisters) {
		this.partMinisters = partMinisters;
	}

	public String getPartMinistersRem() {
		return this.partMinistersRem;
	}

	public void setPartMinistersRem(String partMinistersRem) {
		this.partMinistersRem = partMinistersRem;
	}

	public String getRemDate() {
		return this.remDate;
	}

	public void setRemDate(String remDate) {
		this.remDate = remDate;
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

	public String getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getSalesContractNo() {
		return this.salesContractNo;
	}

	public void setSalesContractNo(String salesContractNo) {
		this.salesContractNo = salesContractNo;
	}

	public String getCustomerSignature() {
		return this.customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
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

	public String getSuperviseId() {
		return this.superviseId;
	}

	public void setSuperviseId(String superviseId) {
		this.superviseId = superviseId;
	}

	public String getSupervisePhone() {
		return this.supervisePhone;
	}

	public void setSupervisePhone(String supervisePhone) {
		this.supervisePhone = supervisePhone;
	}

	public String getAssessRem() {
		return this.assessRem;
	}

	public void setAssessRem(String assessRem) {
		this.assessRem = assessRem;
	}

	public Double getDeductMoney() {
		return this.deductMoney;
	}

	public void setDeductMoney(Double deductMoney) {
		this.deductMoney = deductMoney;
	}

	public String getElevatorType() {
		return this.elevatorType;
	}

	public void setElevatorType(String elevatorType) {
		this.elevatorType = elevatorType;
	}

	public String getPersonnelPhone() {
		return this.personnelPhone;
	}

	public void setPersonnelPhone(String personnelPhone) {
		this.personnelPhone = personnelPhone;
	}

}