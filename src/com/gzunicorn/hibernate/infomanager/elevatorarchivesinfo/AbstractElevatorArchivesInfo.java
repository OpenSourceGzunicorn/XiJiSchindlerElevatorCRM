package com.gzunicorn.hibernate.infomanager.elevatorarchivesinfo;

/**
 * AbstractElevatorArchivesInfo entity provides the base persistence definition
 * of the ElevatorArchivesInfo entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractElevatorArchivesInfo implements
		java.io.Serializable {

	// Fields

	private Integer numno;
	private String salesContractNo;
	private String maintContractNo;
	private String projectName;
	private String projectAddress;
	private String maintDivision;
	private String maintStation;
	private String elevatorNo;
	private String elevatorType;
	private String elevatorParam;
	private Integer floor;
	private Integer stage;
	private Integer door;
	private Double high;
	private String detailConfig;
	private String deliveryDate;
	private String certificatDate;
	private String customerDate;
	private String confirmDate;
	private String rem;
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

	// Constructors

	/** default constructor */
	public AbstractElevatorArchivesInfo() {
	}

	/** minimal constructor */
	public AbstractElevatorArchivesInfo(Integer numno) {
		this.numno = numno;
	}

	/** full constructor */
	public AbstractElevatorArchivesInfo(Integer numno, String salesContractNo,
			String maintContractNo, String projectName, String projectAddress,
			String maintDivision, String maintStation, String elevatorNo,
			String elevatorType, String elevatorParam, Integer floor,
			Integer stage, Integer door, Double high, String detailConfig,
			String deliveryDate, String certificatDate, String customerDate,
			String confirmDate, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		this.numno = numno;
		this.salesContractNo = salesContractNo;
		this.maintContractNo = maintContractNo;
		this.projectName = projectName;
		this.projectAddress = projectAddress;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.elevatorParam = elevatorParam;
		this.floor = floor;
		this.stage = stage;
		this.door = door;
		this.high = high;
		this.detailConfig = detailConfig;
		this.deliveryDate = deliveryDate;
		this.certificatDate = certificatDate;
		this.customerDate = customerDate;
		this.confirmDate = confirmDate;
		this.rem = rem;
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
	}

	// Property accessors

	public Integer getNumno() {
		return this.numno;
	}

	public void setNumno(Integer numno) {
		this.numno = numno;
	}

	public String getSalesContractNo() {
		return this.salesContractNo;
	}

	public void setSalesContractNo(String salesContractNo) {
		this.salesContractNo = salesContractNo;
	}

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectAddress() {
		return this.projectAddress;
	}

	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
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

	public String getElevatorParam() {
		return this.elevatorParam;
	}

	public void setElevatorParam(String elevatorParam) {
		this.elevatorParam = elevatorParam;
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

	public String getDetailConfig() {
		return this.detailConfig;
	}

	public void setDetailConfig(String detailConfig) {
		this.detailConfig = detailConfig;
	}

	public String getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getCertificatDate() {
		return this.certificatDate;
	}

	public void setCertificatDate(String certificatDate) {
		this.certificatDate = certificatDate;
	}

	public String getCustomerDate() {
		return this.customerDate;
	}

	public void setCustomerDate(String customerDate) {
		this.customerDate = customerDate;
	}

	public String getConfirmDate() {
		return this.confirmDate;
	}

	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getRem() {
		return this.rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
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

}