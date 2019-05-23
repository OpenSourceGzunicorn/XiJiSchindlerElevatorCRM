package com.gzunicorn.hibernate.engcontractmanager.entrustcontractdetail;

import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;

/**
 * AbstractEntrustContractDetail entity provides the base persistence definition
 * of the EntrustContractDetail entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractEntrustContractDetail implements
		java.io.Serializable {

	// Fields

	private Integer rowid;
	private EntrustContractMaster entrustContractMaster;
	private Integer maintRowid;
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
	private String mainSdate;
	private String mainEdate;

	// Constructors

	/** default constructor */
	public AbstractEntrustContractDetail() {
	}

	/** minimal constructor */
	public AbstractEntrustContractDetail(
			EntrustContractMaster entrustContractMaster, Integer maintRowid,
			String elevatorNo, String elevatorType, Integer floor,
			Integer stage, Integer door, Double high, String elevatorParam,
			String salesContractNo) {
		this.entrustContractMaster = entrustContractMaster;
		this.maintRowid = maintRowid;
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.floor = floor;
		this.stage = stage;
		this.door = door;
		this.high = high;
		this.elevatorParam = elevatorParam;
		this.salesContractNo = salesContractNo;
	}

	/** full constructor */
	public AbstractEntrustContractDetail(
			EntrustContractMaster entrustContractMaster, Integer maintRowid,
			String elevatorNo, String elevatorType, Integer floor,
			Integer stage, Integer door, Double high, String elevatorParam,
			String annualInspectionDate, String salesContractNo,
			String projectName, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String maintAddress, String mainSdate, String mainEdate) {
		this.entrustContractMaster = entrustContractMaster;
		this.maintRowid = maintRowid;
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
		this.mainSdate = mainSdate;
		this.mainEdate = mainEdate;
	}

	// Property accessors

	public Integer getRowid() {
		return this.rowid;
	}

	public void setRowid(Integer rowid) {
		this.rowid = rowid;
	}

	public EntrustContractMaster getEntrustContractMaster() {
		return this.entrustContractMaster;
	}

	public void setEntrustContractMaster(
			EntrustContractMaster entrustContractMaster) {
		this.entrustContractMaster = entrustContractMaster;
	}

	public Integer getMaintRowid() {
		return this.maintRowid;
	}

	public void setMaintRowid(Integer maintRowid) {
		this.maintRowid = maintRowid;
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

}