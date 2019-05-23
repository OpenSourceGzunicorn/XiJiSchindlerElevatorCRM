package com.gzunicorn.hibernate.basedata.elevatorsales;

/**
 * AbstractElevatorSalesInfo entity provides the base persistence definition of
 * the ElevatorSalesInfo entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractElevatorSalesInfo implements java.io.Serializable {

	// Fields

	private String elevatorNo;
	private String elevatorType;
	private String salesContractNo;
	private String salesContractName;
	private String salesContractType;
	private String inspectDate;
	private String elevatorParam;
	private Integer floor;
	private Integer stage;
	private Integer door;
	private String weight;
	private String speed;
	private Double high;
	private String seriesName;
	private String useUnit;
	private String dealer;
	private String deliveryAddress;
	private String department;
	private String operationName;
	private String operationPhone;
	private String enabledFlag;
	private String rem;
	private String operId;
	private String operDate;
	private String seriesId;
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
	private String configuring;
	private String isOutsideFootball;
	private String maintenanceClause;
	private String warranty;

	// Constructors

	/** default constructor */
	public AbstractElevatorSalesInfo() {
	}

	/** minimal constructor */
	public AbstractElevatorSalesInfo(String elevatorNo, String elevatorType,
			String salesContractNo, String salesContractName,
			String salesContractType, String inspectDate, String elevatorParam,
			Integer floor, Integer stage, Integer door, String weight,
			String speed, Double high, String seriesName, String enabledFlag,
			String operId, String operDate) {
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.salesContractNo = salesContractNo;
		this.salesContractName = salesContractName;
		this.salesContractType = salesContractType;
		this.inspectDate = inspectDate;
		this.elevatorParam = elevatorParam;
		this.floor = floor;
		this.stage = stage;
		this.door = door;
		this.weight = weight;
		this.speed = speed;
		this.high = high;
		this.seriesName = seriesName;
		this.enabledFlag = enabledFlag;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractElevatorSalesInfo(String elevatorNo, String elevatorType,
			String salesContractNo, String salesContractName,
			String salesContractType, String inspectDate, String elevatorParam,
			Integer floor, Integer stage, Integer door, String weight,
			String speed, Double high, String seriesName, String useUnit,
			String dealer, String deliveryAddress, String department,
			String operationName, String operationPhone, String enabledFlag,
			String rem, String operId, String operDate, String seriesId,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10, String configuring,
			String isOutsideFootball, String maintenanceClause, String warranty) {
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.salesContractNo = salesContractNo;
		this.salesContractName = salesContractName;
		this.salesContractType = salesContractType;
		this.inspectDate = inspectDate;
		this.elevatorParam = elevatorParam;
		this.floor = floor;
		this.stage = stage;
		this.door = door;
		this.weight = weight;
		this.speed = speed;
		this.high = high;
		this.seriesName = seriesName;
		this.useUnit = useUnit;
		this.dealer = dealer;
		this.deliveryAddress = deliveryAddress;
		this.department = department;
		this.operationName = operationName;
		this.operationPhone = operationPhone;
		this.enabledFlag = enabledFlag;
		this.rem = rem;
		this.operId = operId;
		this.operDate = operDate;
		this.seriesId = seriesId;
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
		this.configuring = configuring;
		this.isOutsideFootball = isOutsideFootball;
		this.maintenanceClause = maintenanceClause;
		this.warranty = warranty;
	}

	// Property accessors

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

	public String getSalesContractNo() {
		return this.salesContractNo;
	}

	public void setSalesContractNo(String salesContractNo) {
		this.salesContractNo = salesContractNo;
	}

	public String getSalesContractName() {
		return this.salesContractName;
	}

	public void setSalesContractName(String salesContractName) {
		this.salesContractName = salesContractName;
	}

	public String getSalesContractType() {
		return this.salesContractType;
	}

	public void setSalesContractType(String salesContractType) {
		this.salesContractType = salesContractType;
	}

	public String getInspectDate() {
		return this.inspectDate;
	}

	public void setInspectDate(String inspectDate) {
		this.inspectDate = inspectDate;
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

	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSpeed() {
		return this.speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public Double getHigh() {
		return this.high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public String getSeriesName() {
		return this.seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getUseUnit() {
		return this.useUnit;
	}

	public void setUseUnit(String useUnit) {
		this.useUnit = useUnit;
	}

	public String getDealer() {
		return this.dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getDeliveryAddress() {
		return this.deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getOperationName() {
		return this.operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationPhone() {
		return this.operationPhone;
	}

	public void setOperationPhone(String operationPhone) {
		this.operationPhone = operationPhone;
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

	public String getSeriesId() {
		return this.seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
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

	public String getConfiguring() {
		return this.configuring;
	}

	public void setConfiguring(String configuring) {
		this.configuring = configuring;
	}

	public String getIsOutsideFootball() {
		return this.isOutsideFootball;
	}

	public void setIsOutsideFootball(String isOutsideFootball) {
		this.isOutsideFootball = isOutsideFootball;
	}

	public String getMaintenanceClause() {
		return this.maintenanceClause;
	}

	public void setMaintenanceClause(String maintenanceClause) {
		this.maintenanceClause = maintenanceClause;
	}

	public String getWarranty() {
		return this.warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

}