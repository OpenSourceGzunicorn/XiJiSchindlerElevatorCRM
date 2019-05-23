package com.gzunicorn.hibernate.hotlinemanagement.calloutprocess;

/**
 * AbstractCalloutProcess entity provides the base persistence definition of the
 * CalloutProcess entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCalloutProcess implements java.io.Serializable {

	// Fields

	private String calloutMasterNo;
	private String assignObject2;
	private String turnSendTime;
	private String turnSendId;
	private String turnSendTel;
	private String assignTime;
	private String assignUser;
	private String assignUserTel;
	private String arriveDateTime;
	private Double arriveLongitude;
	private Double arriveLatitude;
	private String deviceId;
	private String hmtId;
	private String faultCode;
	private String faultStatus;
	private String isStop;
	private String completeTime;
	private Double fninishLongitude;
	private Double fninishLatitude;
	private String newFittingName;
	private Integer num;
	private String hftId;
	private String isToll;
	private Double money;
	private String processDesc;
	private String serviceRem;
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
	private String arriveLocation;
	private String fninishLocation;
	private String stopTime;
	private Double stopLongitude;
	private Double stopLatitude;
	private String stopLocation;
	private String isgzbgs;
	private String stopRem;
	
	private Double arriveLongitudeGPS;
	private Double arriveLatitudeGPS;
	private Double stopLongitudeGPS;
	private Double stopLatitudeGPS;
	private Double fninishLongitudeGPS;
	private Double fninishLatitudeGPS;
	
	private String customerSignature;
	private String customerImage;
	private String arriveDate;
	private String arriveTime;

	// Constructors

	public String getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getCustomerImage() {
		return customerImage;
	}

	public void setCustomerImage(String customerImage) {
		this.customerImage = customerImage;
	}

	public Double getArriveLongitudeGPS() {
		return arriveLongitudeGPS;
	}

	public void setArriveLongitudeGPS(Double arriveLongitudeGPS) {
		this.arriveLongitudeGPS = arriveLongitudeGPS;
	}

	public Double getArriveLatitudeGPS() {
		return arriveLatitudeGPS;
	}

	public void setArriveLatitudeGPS(Double arriveLatitudeGPS) {
		this.arriveLatitudeGPS = arriveLatitudeGPS;
	}

	public Double getStopLongitudeGPS() {
		return stopLongitudeGPS;
	}

	public void setStopLongitudeGPS(Double stopLongitudeGPS) {
		this.stopLongitudeGPS = stopLongitudeGPS;
	}

	public Double getStopLatitudeGPS() {
		return stopLatitudeGPS;
	}

	public void setStopLatitudeGPS(Double stopLatitudeGPS) {
		this.stopLatitudeGPS = stopLatitudeGPS;
	}

	public Double getFninishLongitudeGPS() {
		return fninishLongitudeGPS;
	}

	public void setFninishLongitudeGPS(Double fninishLongitudeGPS) {
		this.fninishLongitudeGPS = fninishLongitudeGPS;
	}

	public Double getFninishLatitudeGPS() {
		return fninishLatitudeGPS;
	}

	public void setFninishLatitudeGPS(Double fninishLatitudeGPS) {
		this.fninishLatitudeGPS = fninishLatitudeGPS;
	}

	public String getStopRem() {
		return stopRem;
	}

	public void setStopRem(String stopRem) {
		this.stopRem = stopRem;
	}

	/** default constructor */
	public AbstractCalloutProcess() {
	}

	/** minimal constructor */
	public AbstractCalloutProcess(String calloutMasterNo, String assignObject2) {
		this.calloutMasterNo = calloutMasterNo;
		this.assignObject2 = assignObject2;
	}

	/** full constructor */
	public AbstractCalloutProcess(String calloutMasterNo, String assignObject2,
			String turnSendTime, String turnSendId, String turnSendTel,
			String assignTime, String assignUser, String assignUserTel,
			String arriveDateTime, Double arriveLongitude,
			Double arriveLatitude, String deviceId, String hmtId,
			String faultCode, String faultStatus, String isStop,
			String completeTime, Double fninishLongitude,
			Double fninishLatitude, String newFittingName, Integer num,
			String hftId, String isToll, Double money, String processDesc,
			String serviceRem, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9,
			Double r10, String arriveLocation, String fninishLocation,
			String stopTime, Double stopLongitude, Double stopLatitude,
			String stopLocation,String isgzbgs,String stopRem,
			Double arriveLongitudeGPS, Double arriveLatitudeGPS,Double stopLongitudeGPS, Double stopLatitudeGPS,
			Double fninishLongitudeGPS, Double fninishLatitudeGPS,
			String customerSignature,String customerImage,String arriveDate,String arriveTime
			) {
		this.arriveDate=arriveDate;
		this.arriveTime=arriveTime;
		this.customerSignature=customerSignature;
		this.customerImage=customerImage;
		this.arriveLongitudeGPS=arriveLongitudeGPS;
		this.arriveLatitudeGPS=arriveLatitudeGPS;
		this.stopLongitudeGPS=stopLongitudeGPS;
		this.stopLatitudeGPS=stopLatitudeGPS;
		this.fninishLongitudeGPS=fninishLongitudeGPS;
		this.fninishLatitudeGPS=fninishLatitudeGPS;
		this.calloutMasterNo = calloutMasterNo;
		this.assignObject2 = assignObject2;
		this.turnSendTime = turnSendTime;
		this.turnSendId = turnSendId;
		this.turnSendTel = turnSendTel;
		this.assignTime = assignTime;
		this.assignUser = assignUser;
		this.assignUserTel = assignUserTel;
		this.arriveDateTime = arriveDateTime;
		this.arriveLongitude = arriveLongitude;
		this.arriveLatitude = arriveLatitude;
		this.deviceId = deviceId;
		this.hmtId = hmtId;
		this.faultCode = faultCode;
		this.faultStatus = faultStatus;
		this.isStop = isStop;
		this.completeTime = completeTime;
		this.fninishLongitude = fninishLongitude;
		this.fninishLatitude = fninishLatitude;
		this.newFittingName = newFittingName;
		this.num = num;
		this.hftId = hftId;
		this.isToll = isToll;
		this.money = money;
		this.processDesc = processDesc;
		this.serviceRem = serviceRem;
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
		this.arriveLocation = arriveLocation;
		this.fninishLocation = fninishLocation;
		this.stopTime = stopTime;
		this.stopLongitude = stopLongitude;
		this.stopLatitude = stopLatitude;
		this.stopLocation = stopLocation;
		this.customerSignature = customerSignature;
		this.isgzbgs = isgzbgs;
		this.stopRem=stopRem;
	}

	// Property accessors

	public String getCalloutMasterNo() {
		return this.calloutMasterNo;
	}

	public void setCalloutMasterNo(String calloutMasterNo) {
		this.calloutMasterNo = calloutMasterNo;
	}

	public String getAssignObject2() {
		return this.assignObject2;
	}

	public void setAssignObject2(String assignObject2) {
		this.assignObject2 = assignObject2;
	}

	public String getTurnSendTime() {
		return this.turnSendTime;
	}

	public void setTurnSendTime(String turnSendTime) {
		this.turnSendTime = turnSendTime;
	}

	public String getTurnSendId() {
		return this.turnSendId;
	}

	public void setTurnSendId(String turnSendId) {
		this.turnSendId = turnSendId;
	}

	public String getTurnSendTel() {
		return this.turnSendTel;
	}

	public void setTurnSendTel(String turnSendTel) {
		this.turnSendTel = turnSendTel;
	}

	public String getAssignTime() {
		return this.assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}

	public String getAssignUser() {
		return this.assignUser;
	}

	public void setAssignUser(String assignUser) {
		this.assignUser = assignUser;
	}

	public String getAssignUserTel() {
		return this.assignUserTel;
	}

	public void setAssignUserTel(String assignUserTel) {
		this.assignUserTel = assignUserTel;
	}

	public String getArriveDateTime() {
		return this.arriveDateTime;
	}

	public void setArriveDateTime(String arriveDateTime) {
		this.arriveDateTime = arriveDateTime;
	}

	public Double getArriveLongitude() {
		return this.arriveLongitude;
	}

	public void setArriveLongitude(Double arriveLongitude) {
		this.arriveLongitude = arriveLongitude;
	}

	public Double getArriveLatitude() {
		return this.arriveLatitude;
	}

	public void setArriveLatitude(Double arriveLatitude) {
		this.arriveLatitude = arriveLatitude;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getHmtId() {
		return this.hmtId;
	}

	public void setHmtId(String hmtId) {
		this.hmtId = hmtId;
	}

	public String getFaultCode() {
		return this.faultCode;
	}

	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	public String getFaultStatus() {
		return this.faultStatus;
	}

	public void setFaultStatus(String faultStatus) {
		this.faultStatus = faultStatus;
	}

	public String getIsStop() {
		return this.isStop;
	}

	public void setIsStop(String isStop) {
		this.isStop = isStop;
	}

	public String getCompleteTime() {
		return this.completeTime;
	}

	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}

	public Double getFninishLongitude() {
		return this.fninishLongitude;
	}

	public void setFninishLongitude(Double fninishLongitude) {
		this.fninishLongitude = fninishLongitude;
	}

	public Double getFninishLatitude() {
		return this.fninishLatitude;
	}

	public void setFninishLatitude(Double fninishLatitude) {
		this.fninishLatitude = fninishLatitude;
	}

	public String getNewFittingName() {
		return this.newFittingName;
	}

	public void setNewFittingName(String newFittingName) {
		this.newFittingName = newFittingName;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getHftId() {
		return this.hftId;
	}

	public void setHftId(String hftId) {
		this.hftId = hftId;
	}

	public String getIsToll() {
		return this.isToll;
	}

	public void setIsToll(String isToll) {
		this.isToll = isToll;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getProcessDesc() {
		return this.processDesc;
	}

	public void setProcessDesc(String processDesc) {
		this.processDesc = processDesc;
	}

	public String getServiceRem() {
		return this.serviceRem;
	}

	public void setServiceRem(String serviceRem) {
		this.serviceRem = serviceRem;
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

	public String getArriveLocation() {
		return this.arriveLocation;
	}

	public void setArriveLocation(String arriveLocation) {
		this.arriveLocation = arriveLocation;
	}

	public String getFninishLocation() {
		return this.fninishLocation;
	}

	public void setFninishLocation(String fninishLocation) {
		this.fninishLocation = fninishLocation;
	}

	public String getStopTime() {
		return this.stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public Double getStopLongitude() {
		return this.stopLongitude;
	}

	public void setStopLongitude(Double stopLongitude) {
		this.stopLongitude = stopLongitude;
	}

	public Double getStopLatitude() {
		return this.stopLatitude;
	}

	public void setStopLatitude(Double stopLatitude) {
		this.stopLatitude = stopLatitude;
	}

	public String getStopLocation() {
		return this.stopLocation;
	}

	public void setStopLocation(String stopLocation) {
		this.stopLocation = stopLocation;
	}

	public String getCustomerSignature() {
		return this.customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}

	public String getIsgzbgs() {
		return this.isgzbgs;
	}

	public void setIsgzbgs(String isgzbgs) {
		this.isgzbgs = isgzbgs;
	}

}