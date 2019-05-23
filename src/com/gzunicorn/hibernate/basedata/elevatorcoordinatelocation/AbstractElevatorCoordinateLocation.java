package com.gzunicorn.hibernate.basedata.elevatorcoordinatelocation;

/**
 * AbstractElevatorCoordinateLocation entity provides the base persistence
 * definition of the ElevatorCoordinateLocation entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractElevatorCoordinateLocation implements
		java.io.Serializable {

	// Fields

	private String elevatorNo;
	private String elevatorLocation;
	private Double beginLongitude;
	private Double endLongitude;
	private Double beginDimension;
	private Double endDimension;
	private String enabledFlag;
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
	public AbstractElevatorCoordinateLocation() {
	}

	/** minimal constructor */
	public AbstractElevatorCoordinateLocation(String elevatorNo,
			String elevatorLocation, Double beginLongitude,
			Double endLongitude, Double beginDimension, Double endDimension,
			String enabledFlag, String operId, String operDate) {
		this.elevatorNo = elevatorNo;
		this.elevatorLocation = elevatorLocation;
		this.beginLongitude = beginLongitude;
		this.endLongitude = endLongitude;
		this.beginDimension = beginDimension;
		this.endDimension = endDimension;
		this.enabledFlag = enabledFlag;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractElevatorCoordinateLocation(String elevatorNo,
			String elevatorLocation, Double beginLongitude,
			Double endLongitude, Double beginDimension, Double endDimension,
			String enabledFlag, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		this.elevatorNo = elevatorNo;
		this.elevatorLocation = elevatorLocation;
		this.beginLongitude = beginLongitude;
		this.endLongitude = endLongitude;
		this.beginDimension = beginDimension;
		this.endDimension = endDimension;
		this.enabledFlag = enabledFlag;
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

	public String getElevatorNo() {
		return this.elevatorNo;
	}

	public void setElevatorNo(String elevatorNo) {
		this.elevatorNo = elevatorNo;
	}

	public String getElevatorLocation() {
		return this.elevatorLocation;
	}

	public void setElevatorLocation(String elevatorLocation) {
		this.elevatorLocation = elevatorLocation;
	}

	public Double getBeginLongitude() {
		return this.beginLongitude;
	}

	public void setBeginLongitude(Double beginLongitude) {
		this.beginLongitude = beginLongitude;
	}

	public Double getEndLongitude() {
		return this.endLongitude;
	}

	public void setEndLongitude(Double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public Double getBeginDimension() {
		return this.beginDimension;
	}

	public void setBeginDimension(Double beginDimension) {
		this.beginDimension = beginDimension;
	}

	public Double getEndDimension() {
		return this.endDimension;
	}

	public void setEndDimension(Double endDimension) {
		this.endDimension = endDimension;
	}

	public String getEnabledFlag() {
		return this.enabledFlag;
	}

	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
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