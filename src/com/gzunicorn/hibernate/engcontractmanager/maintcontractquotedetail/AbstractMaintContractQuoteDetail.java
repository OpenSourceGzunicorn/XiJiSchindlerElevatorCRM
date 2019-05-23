package com.gzunicorn.hibernate.engcontractmanager.maintcontractquotedetail;

/**
 * AbstractMaintContractQuoteDetail entity provides the base persistence
 * definition of the MaintContractQuoteDetail entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractMaintContractQuoteDetail implements
		java.io.Serializable {

	// Fields

	private Integer rowid;
	private String billNo;
	private String elevatorNo;
	private String elevatorType;
	private Integer floor;
	private Integer stage;
	private Integer door;
	private Double high;
	private Integer num;
	private Double elevatorAge;
	private String salesContractNo;
	private String projectName;
	private String projectAddress;
	private Double standardQuote;
	private Double finallyQuote;
	private String rem;
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
	private String contractPeriod;
	private String elevatorParam;
	private String signWay;
	private String weight;
	private String speed;
	private String standardQuoteDis;
	private Double jyMoney;
	
	// Constructors

	public Double getJyMoney() {
		return jyMoney;
	}

	public void setJyMoney(Double jyMoney) {
		this.jyMoney = jyMoney;
	}

	public String getStandardQuoteDis() {
		return standardQuoteDis;
	}

	public void setStandardQuoteDis(String standardQuoteDis) {
		this.standardQuoteDis = standardQuoteDis;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getSignWay() {
		return signWay;
	}

	public void setSignWay(String signWay) {
		this.signWay = signWay;
	}

	public String getElevatorParam() {
		return elevatorParam;
	}

	public void setElevatorParam(String elevatorParam) {
		this.elevatorParam = elevatorParam;
	}

	public String getContractPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	/** default constructor */
	public AbstractMaintContractQuoteDetail() {
	}

	/** minimal constructor */
	public AbstractMaintContractQuoteDetail(String billNo, String elevatorNo,
			String elevatorType, Integer floor, Integer stage, Integer door,
			Double high, Integer num, Double elevatorAge,
			String salesContractNo, Double standardQuote, Double finallyQuote) {
		this.billNo = billNo;
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.floor = floor;
		this.stage = stage;
		this.door = door;
		this.high = high;
		this.num = num;
		this.elevatorAge = elevatorAge;
		this.salesContractNo = salesContractNo;
		this.standardQuote = standardQuote;
		this.finallyQuote = finallyQuote;
	}

	/** full constructor */
	public AbstractMaintContractQuoteDetail(String billNo, String elevatorNo,
			String elevatorType, Integer floor, Integer stage, Integer door,
			Double high, Integer num, Double elevatorAge,
			String salesContractNo, String projectName, String projectAddress,
			Double standardQuote, Double finallyQuote, String rem, String r1,
			String r2, String r3, String r4, String r5, Double r6, Double r7,
			Double r8, Integer r9, Integer r10,
			String contractPeriod,String elevatorParam,String signWay,
			String weight, String speed,String standardQuoteDis,Double jyMoney) {
		this.jyMoney=jyMoney;
		this.standardQuoteDis=standardQuoteDis;
		this.weight=weight;
		this.speed=speed;
		this.signWay=signWay;
		this.elevatorParam=elevatorParam;
		this.billNo = billNo;
		this.elevatorNo = elevatorNo;
		this.elevatorType = elevatorType;
		this.floor = floor;
		this.stage = stage;
		this.door = door;
		this.high = high;
		this.num = num;
		this.elevatorAge = elevatorAge;
		this.salesContractNo = salesContractNo;
		this.projectName = projectName;
		this.projectAddress = projectAddress;
		this.standardQuote = standardQuote;
		this.finallyQuote = finallyQuote;
		this.rem = rem;
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
		this.contractPeriod=contractPeriod;
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

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getElevatorAge() {
		return this.elevatorAge;
	}

	public void setElevatorAge(Double elevatorAge) {
		this.elevatorAge = elevatorAge;
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

	public String getProjectAddress() {
		return this.projectAddress;
	}

	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
	}

	public Double getStandardQuote() {
		return this.standardQuote;
	}

	public void setStandardQuote(Double standardQuote) {
		this.standardQuote = standardQuote;
	}

	public Double getFinallyQuote() {
		return this.finallyQuote;
	}

	public void setFinallyQuote(Double finallyQuote) {
		this.finallyQuote = finallyQuote;
	}

	public String getRem() {
		return this.rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
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