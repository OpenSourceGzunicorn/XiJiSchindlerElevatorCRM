package com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail;

/**
 * AbstractMaintContractDetail entity provides the base persistence definition
 * of the MaintContractDetail entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractMaintContractDetail implements
		java.io.Serializable {

	// Fields

	private Integer rowid;
	private String billNo;
	private String signWay;
	private String elevatorNo;
	private String elevatorType;
	private Integer floor;
	private Integer stage;
	private Integer door;
	private Double high;
	private String elevatorParam;
	private String annualInspectionDate;
	private String salesContractNo;
	private String projectName;
	private String mainSdate;
	private String mainEdate;
	private String realityEdate;
	private String shippedDate;
	private String issueDate;
	private String tranCustDate;
	private String mainConfirmDate;
	private String assignedMainStation;
	private String assignedSignFlag;
	private String assignedSign;
	private String assignedSignDate;
	private String returnReason;
	private String maintPersonnel;
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
	private String maintAddress;
	private String isSurrender;
	private String surrenderUser;
	private String surrenderDate;
	private String delayEDate;
	private String elevatorNature;
	private String elevatorStatus;
	private String isCertificate;
	private String certificateDate;
	private String aidateoperid;
	private String aidateopertime;
	private String assignedRem;

	// Constructors

	public String getAidateoperid() {
		return aidateoperid;
	}

	public void setAidateoperid(String aidateoperid) {
		this.aidateoperid = aidateoperid;
	}

	public String getAidateopertime() {
		return aidateopertime;
	}

	public void setAidateopertime(String aidateopertime) {
		this.aidateopertime = aidateopertime;
	}

	public String getIsCertificate() {
		return isCertificate;
	}

	public void setIsCertificate(String isCertificate) {
		this.isCertificate = isCertificate;
	}

	public String getCertificateDate() {
		return certificateDate;
	}

	public void setCertificateDate(String certificateDate) {
		this.certificateDate = certificateDate;
	}

	public String getElevatorStatus() {
		return elevatorStatus;
	}

	public void setElevatorStatus(String elevatorStatus) {
		this.elevatorStatus = elevatorStatus;
	}

	public String getElevatorNature() {
		return elevatorNature;
	}

	public void setElevatorNature(String elevatorNature) {
		this.elevatorNature = elevatorNature;
	}

	public String getDelayEDate() {
		return delayEDate;
	}

	public void setDelayEDate(String delayEDate) {
		this.delayEDate = delayEDate;
	}

	/** default constructor */
	public AbstractMaintContractDetail() {
	}

	/** minimal constructor */
	public AbstractMaintContractDetail(String elevatorNo, String elevatorType,
			Integer floor, Integer stage, Integer door, Double high,
			String elevatorParam, String annualInspectionDate,
			String salesContractNo, String mainSdate, String mainEdate) {
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.floor = floor;
		this.stage = stage;
		this.door = door;
		this.high = high;
		this.elevatorParam = elevatorParam;
		this.annualInspectionDate = annualInspectionDate;
		this.salesContractNo = salesContractNo;
		this.mainSdate = mainSdate;
		this.mainEdate = mainEdate;
	}

	/** full constructor */
	public AbstractMaintContractDetail(String billNo, String signWay,
			String elevatorNo, String elevatorType, Integer floor,
			Integer stage, Integer door, Double high, String elevatorParam,
			String annualInspectionDate, String salesContractNo,
			String projectName, String mainSdate, String mainEdate,
			String realityEdate, String shippedDate, String issueDate,
			String tranCustDate, String mainConfirmDate,
			String assignedMainStation, String assignedSignFlag,
			String assignedSign, String assignedSignDate, String returnReason,
			String maintPersonnel, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String maintAddress, String isSurrender,
			String surrenderUser, String surrenderDate,String delayEDate,
			String elevatorNature,String elevatorStatus,String isCertificate,String certificateDate,
			String aidateoperid,String aidateopertime) {
		this.aidateoperid=aidateoperid;
		this.aidateopertime=aidateopertime;
		this.isCertificate=isCertificate;
		this.certificateDate=certificateDate;
		this.elevatorStatus=elevatorStatus;
		this.elevatorNature=elevatorNature;
		this.delayEDate=delayEDate;
		this.billNo = billNo;
		this.signWay = signWay;
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.floor = floor;
		this.stage = stage;
		this.door = door;
		this.high = high;
		this.elevatorParam = elevatorParam;
		this.annualInspectionDate = annualInspectionDate;
		this.salesContractNo = salesContractNo;
		this.projectName = projectName;
		this.mainSdate = mainSdate;
		this.mainEdate = mainEdate;
		this.realityEdate = realityEdate;
		this.shippedDate = shippedDate;
		this.issueDate = issueDate;
		this.tranCustDate = tranCustDate;
		this.mainConfirmDate = mainConfirmDate;
		this.assignedMainStation = assignedMainStation;
		this.assignedSignFlag = assignedSignFlag;
		this.assignedSign = assignedSign;
		this.assignedSignDate = assignedSignDate;
		this.returnReason = returnReason;
		this.maintPersonnel = maintPersonnel;
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
		this.maintAddress = maintAddress;
		this.isSurrender = isSurrender;
		this.surrenderUser = surrenderUser;
		this.surrenderDate = surrenderDate;
	}

	// Property accessors

	public Integer getRowid() {
		return this.rowid;
	}

	public void setRowid(Integer rowid) {
		this.rowid = rowid;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getSignWay() {
		return this.signWay;
	}

	public void setSignWay(String signWay) {
		this.signWay = signWay;
	}

	public String getElevatorNo() {
		return this.elevatorNo;
	}

	public void setElevatorNo(String elevatorNo) {
		this.elevatorNo = elevatorNo;
	}

	public String getElevatorType() {
		return this.elevatorType;
	}

	public void setElevatorType(String elevatorType) {
		this.elevatorType = elevatorType;
	}

	public Integer getFloor() {
		return this.floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Integer getStage() {
		return this.stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public Integer getDoor() {
		return this.door;
	}

	public void setDoor(Integer door) {
		this.door = door;
	}

	public Double getHigh() {
		return this.high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public String getElevatorParam() {
		return this.elevatorParam;
	}

	public void setElevatorParam(String elevatorParam) {
		this.elevatorParam = elevatorParam;
	}

	public String getAnnualInspectionDate() {
		return this.annualInspectionDate;
	}

	public void setAnnualInspectionDate(String annualInspectionDate) {
		this.annualInspectionDate = annualInspectionDate;
	}

	public String getSalesContractNo() {
		return this.salesContractNo;
	}

	public void setSalesContractNo(String salesContractNo) {
		this.salesContractNo = salesContractNo;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getMainSdate() {
		return this.mainSdate;
	}

	public void setMainSdate(String mainSdate) {
		this.mainSdate = mainSdate;
	}

	public String getMainEdate() {
		return this.mainEdate;
	}

	public void setMainEdate(String mainEdate) {
		this.mainEdate = mainEdate;
	}

	public String getRealityEdate() {
		return this.realityEdate;
	}

	public void setRealityEdate(String realityEdate) {
		this.realityEdate = realityEdate;
	}

	public String getShippedDate() {
		return this.shippedDate;
	}

	public void setShippedDate(String shippedDate) {
		this.shippedDate = shippedDate;
	}

	public String getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getTranCustDate() {
		return this.tranCustDate;
	}

	public void setTranCustDate(String tranCustDate) {
		this.tranCustDate = tranCustDate;
	}

	public String getMainConfirmDate() {
		return this.mainConfirmDate;
	}

	public void setMainConfirmDate(String mainConfirmDate) {
		this.mainConfirmDate = mainConfirmDate;
	}

	public String getAssignedMainStation() {
		return this.assignedMainStation;
	}

	public void setAssignedMainStation(String assignedMainStation) {
		this.assignedMainStation = assignedMainStation;
	}

	public String getAssignedSignFlag() {
		return this.assignedSignFlag;
	}

	public void setAssignedSignFlag(String assignedSignFlag) {
		this.assignedSignFlag = assignedSignFlag;
	}

	public String getAssignedSign() {
		return this.assignedSign;
	}

	public void setAssignedSign(String assignedSign) {
		this.assignedSign = assignedSign;
	}

	public String getAssignedSignDate() {
		return this.assignedSignDate;
	}

	public void setAssignedSignDate(String assignedSignDate) {
		this.assignedSignDate = assignedSignDate;
	}

	public String getReturnReason() {
		return this.returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getMaintPersonnel() {
		return this.maintPersonnel;
	}

	public void setMaintPersonnel(String maintPersonnel) {
		this.maintPersonnel = maintPersonnel;
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

	public String getMaintAddress() {
		return this.maintAddress;
	}

	public void setMaintAddress(String maintAddress) {
		this.maintAddress = maintAddress;
	}

	public String getIsSurrender() {
		return this.isSurrender;
	}

	public void setIsSurrender(String isSurrender) {
		this.isSurrender = isSurrender;
	}

	public String getSurrenderUser() {
		return this.surrenderUser;
	}

	public void setSurrenderUser(String surrenderUser) {
		this.surrenderUser = surrenderUser;
	}

	public String getSurrenderDate() {
		return this.surrenderDate;
	}

	public void setSurrenderDate(String surrenderDate) {
		this.surrenderDate = surrenderDate;
	}

	public String getAssignedRem() {
		return assignedRem;
	}

	public void setAssignedRem(String assignedRem) {
		this.assignedRem = assignedRem;
	}

}