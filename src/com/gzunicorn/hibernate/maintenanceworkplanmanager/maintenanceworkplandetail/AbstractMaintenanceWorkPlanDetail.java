package com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail;

import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanmaster.MaintenanceWorkPlanMaster;

/**
 * AbstractMaintenanceWorkPlanDetail entity provides the base persistence
 * definition of the MaintenanceWorkPlanDetail entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractMaintenanceWorkPlanDetail implements
		java.io.Serializable {

	// Fields

	private Integer numno;
	private MaintenanceWorkPlanMaster maintenanceWorkPlanMaster;
	private String maintPersonnel;
	private String week;
	private String maintDate;
	private String maintType;
	private String maintDateTime;
	private String releasedTime;
	private String singleno;
	private String receivingTime;
	private String isTransfer;
	private String transferDate;
	private String receivingPerson;
	private String receivingPhone;
	private String receivingDate;
	private String maintStartTime;
	private Double beginLongitude;
	private Double beginDimension;
	private String maintStartAddres;
	private String maintEndTime;
	private Double endLongitude;
	private Double endDimension;
	private String maintEndAddres;
	private Double usedDuration;
	private String auditCircu;
	private String rem;
	private String fittingsReplace;
	private String isInvoice;
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
	private String handleStatus;
	private String byAuditOperid;
	private String byAuditDate;
	private Double startDistance;
	private Double endDistance;
	private Double distanceScore;
	private Double dateScore;
	private Double maintScore;
	private String djOperId2;
	private String djOperDate2;
	
	private String stopTime;
	private Double stopLongitude;
	private Double stopDimension;
	private String stopAddres;
	private String restartTime;
	private Double restartLongitude;
	private Double restartDimension;
	private String restartAddres;
	private Double stopDistance;
	private Double restartDistance;
	
	private Double beginLongitudeGPS;
	private Double beginDimensionGPS;
	private Double endLongitudeGPS;
	private Double endDimensionGPS;
	private Double stopLongitudeGPS;
	private Double stopDimensionGPS;
	private Double restartLongitudeGPS;
	private Double restartDimensionGPS;
	private String customerSignature;
	private String customerImage;

	public String getCustomerSignature() {
		return customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}

	public String getCustomerImage() {
		return customerImage;
	}

	public void setCustomerImage(String customerImage) {
		this.customerImage = customerImage;
	}

	public Double getBeginLongitudeGPS() {
		return beginLongitudeGPS;
	}

	public void setBeginLongitudeGPS(Double beginLongitudeGPS) {
		this.beginLongitudeGPS = beginLongitudeGPS;
	}

	public Double getBeginDimensionGPS() {
		return beginDimensionGPS;
	}

	public void setBeginDimensionGPS(Double beginDimensionGPS) {
		this.beginDimensionGPS = beginDimensionGPS;
	}

	public Double getEndLongitudeGPS() {
		return endLongitudeGPS;
	}

	public void setEndLongitudeGPS(Double endLongitudeGPS) {
		this.endLongitudeGPS = endLongitudeGPS;
	}

	public Double getEndDimensionGPS() {
		return endDimensionGPS;
	}

	public void setEndDimensionGPS(Double endDimensionGPS) {
		this.endDimensionGPS = endDimensionGPS;
	}

	public Double getStopLongitudeGPS() {
		return stopLongitudeGPS;
	}

	public void setStopLongitudeGPS(Double stopLongitudeGPS) {
		this.stopLongitudeGPS = stopLongitudeGPS;
	}

	public Double getStopDimensionGPS() {
		return stopDimensionGPS;
	}

	public void setStopDimensionGPS(Double stopDimensionGPS) {
		this.stopDimensionGPS = stopDimensionGPS;
	}

	public Double getRestartLongitudeGPS() {
		return restartLongitudeGPS;
	}

	public void setRestartLongitudeGPS(Double restartLongitudeGPS) {
		this.restartLongitudeGPS = restartLongitudeGPS;
	}

	public Double getRestartDimensionGPS() {
		return restartDimensionGPS;
	}

	public void setRestartDimensionGPS(Double restartDimensionGPS) {
		this.restartDimensionGPS = restartDimensionGPS;
	}

	public Double getRestartLongitude() {
		return restartLongitude;
	}

	public void setRestartLongitude(Double restartLongitude) {
		this.restartLongitude = restartLongitude;
	}

	public Double getStopDistance() {
		return stopDistance;
	}

	public void setStopDistance(Double stopDistance) {
		this.stopDistance = stopDistance;
	}

	public Double getRestartDistance() {
		return restartDistance;
	}

	public void setRestartDistance(Double restartDistance) {
		this.restartDistance = restartDistance;
	}
	
	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public Double getStopLongitude() {
		return stopLongitude;
	}

	public void setStopLongitude(Double stopLongitude) {
		this.stopLongitude = stopLongitude;
	}

	public Double getStopDimension() {
		return stopDimension;
	}

	public void setStopDimension(Double stopDimension) {
		this.stopDimension = stopDimension;
	}

	public String getStopAddres() {
		return stopAddres;
	}

	public void setStopAddres(String stopAddres) {
		this.stopAddres = stopAddres;
	}

	public String getRestartTime() {
		return restartTime;
	}

	public void setRestartTime(String restartTime) {
		this.restartTime = restartTime;
	}


	public Double getRestartDimension() {
		return restartDimension;
	}

	public void setRestartDimension(Double restartDimension) {
		this.restartDimension = restartDimension;
	}

	public String getRestartAddres() {
		return restartAddres;
	}

	public void setRestartAddres(String restartAddres) {
		this.restartAddres = restartAddres;
	}

	public String getDjOperId2() {
		return djOperId2;
	}

	public void setDjOperId2(String djOperId2) {
		this.djOperId2 = djOperId2;
	}

	public String getDjOperDate2() {
		return djOperDate2;
	}

	public void setDjOperDate2(String djOperDate2) {
		this.djOperDate2 = djOperDate2;
	}

	// Constructors
	public Double getUsedDuration() {
		return usedDuration;
	}

	public void setUsedDuration(Double usedDuration) {
		this.usedDuration = usedDuration;
	}
	
	public Double getStartDistance() {
		return startDistance;
	}

	public void setStartDistance(Double startDistance) {
		this.startDistance = startDistance;
	}

	public Double getEndDistance() {
		return endDistance;
	}

	public void setEndDistance(Double endDistance) {
		this.endDistance = endDistance;
	}

	public Double getDistanceScore() {
		return distanceScore;
	}

	public void setDistanceScore(Double distanceScore) {
		this.distanceScore = distanceScore;
	}

	public Double getDateScore() {
		return dateScore;
	}

	public void setDateScore(Double dateScore) {
		this.dateScore = dateScore;
	}

	public Double getMaintScore() {
		return maintScore;
	}

	public void setMaintScore(Double maintScore) {
		this.maintScore = maintScore;
	}

	/** default constructor */
	public AbstractMaintenanceWorkPlanDetail() {
	}

	/** minimal constructor */
	public AbstractMaintenanceWorkPlanDetail(
			MaintenanceWorkPlanMaster maintenanceWorkPlanMaster,
			String maintPersonnel, String week, String maintDate,
			String maintType, String maintDateTime, String operId,
			String operDate) {
		this.maintenanceWorkPlanMaster = maintenanceWorkPlanMaster;
		this.maintPersonnel = maintPersonnel;
		this.week = week;
		this.maintDate = maintDate;
		this.maintType = maintType;
		this.maintDateTime = maintDateTime;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractMaintenanceWorkPlanDetail(
			MaintenanceWorkPlanMaster maintenanceWorkPlanMaster,
			String maintPersonnel, String week, String maintDate,
			String maintType, String maintDateTime, String releasedTime,
			String singleno, String receivingTime, String isTransfer,
			String transferDate, String receivingPerson, String receivingPhone,
			String receivingDate, String maintStartTime, Double beginLongitude,
			Double beginDimension, String maintStartAddres,
			String maintEndTime, Double endLongitude, Double endDimension,
			String maintEndAddres, Double usedDuration, String auditCircu,
			String rem, String fittingsReplace, String isInvoice,
			String operId, String operDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String handleStatus, String byAuditOperid,
			String byAuditDate,Double startDistance, Double endDistance,
			Double distanceScore, Double dateScore, Double maintScore,
			String djOperId2,String djOperDate2,
			String stopTime, Double stopLongitude,Double stopDimension, String stopAddres,
			String restartTime, Double restartLongitude,Double restartDimension, String restartAddres,
			Double stopDistance,Double restartDistance,
			Double beginLongitudeGPS,Double beginDimensionGPS,Double endLongitudeGPS,Double endDimensionGPS,
			Double stopLongitudeGPS,Double stopDimensionGPS,Double restartLongitudeGPS,Double restartDimensionGPS,
			String customerSignature,String customerImage
			) {
		this.customerSignature=customerSignature;
		this.customerImage=customerImage;
		this.beginLongitudeGPS=beginLongitudeGPS;
		this.beginDimensionGPS=beginDimensionGPS;
		this.endLongitudeGPS=endLongitudeGPS;
		this.endDimensionGPS=endDimensionGPS;
		this.stopLongitudeGPS=stopLongitudeGPS;
		this.stopDimensionGPS=stopDimensionGPS;
		this.restartLongitudeGPS=restartLongitudeGPS;
		this.restartDimensionGPS=restartDimensionGPS;
		this.stopDistance=stopDistance;
		this.restartDistance=restartDistance;
		this.stopTime=stopTime;
		this.stopLongitude=stopLongitude;
		this.stopDimension=stopDimension;
		this.stopAddres=stopAddres;
		this.restartTime=restartTime;
		this.restartLongitude=restartLongitude;
		this.restartDimension=restartDimension;
		this.restartAddres=restartAddres;
		this.djOperId2=djOperId2;
		this.djOperDate2=djOperDate2;
		this.maintenanceWorkPlanMaster = maintenanceWorkPlanMaster;
		this.maintPersonnel = maintPersonnel;
		this.week = week;
		this.maintDate = maintDate;
		this.maintType = maintType;
		this.maintDateTime = maintDateTime;
		this.releasedTime = releasedTime;
		this.singleno = singleno;
		this.receivingTime = receivingTime;
		this.isTransfer = isTransfer;
		this.transferDate = transferDate;
		this.receivingPerson = receivingPerson;
		this.receivingPhone = receivingPhone;
		this.receivingDate = receivingDate;
		this.maintStartTime = maintStartTime;
		this.beginLongitude = beginLongitude;
		this.beginDimension = beginDimension;
		this.maintStartAddres = maintStartAddres;
		this.maintEndTime = maintEndTime;
		this.endLongitude = endLongitude;
		this.endDimension = endDimension;
		this.maintEndAddres = maintEndAddres;
		this.usedDuration = usedDuration;
		this.auditCircu = auditCircu;
		this.rem = rem;
		this.fittingsReplace = fittingsReplace;
		this.isInvoice = isInvoice;
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
		this.handleStatus = handleStatus;
		this.byAuditOperid = byAuditOperid;
		this.byAuditDate = byAuditDate;
		this.startDistance=startDistance;
		this.endDistance=endDistance;
		this.distanceScore=distanceScore;
		this.dateScore=dateScore;
		this.maintScore=maintScore;
	}

	// Property accessors

	public Integer getNumno() {
		return this.numno;
	}

	public void setNumno(Integer numno) {
		this.numno = numno;
	}

	public MaintenanceWorkPlanMaster getMaintenanceWorkPlanMaster() {
		return this.maintenanceWorkPlanMaster;
	}

	public void setMaintenanceWorkPlanMaster(
			MaintenanceWorkPlanMaster maintenanceWorkPlanMaster) {
		this.maintenanceWorkPlanMaster = maintenanceWorkPlanMaster;
	}

	public String getMaintPersonnel() {
		return this.maintPersonnel;
	}

	public void setMaintPersonnel(String maintPersonnel) {
		this.maintPersonnel = maintPersonnel;
	}

	public String getWeek() {
		return this.week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getMaintDate() {
		return this.maintDate;
	}

	public void setMaintDate(String maintDate) {
		this.maintDate = maintDate;
	}

	public String getMaintType() {
		return this.maintType;
	}

	public void setMaintType(String maintType) {
		this.maintType = maintType;
	}

	public String getMaintDateTime() {
		return this.maintDateTime;
	}

	public void setMaintDateTime(String maintDateTime) {
		this.maintDateTime = maintDateTime;
	}

	public String getReleasedTime() {
		return this.releasedTime;
	}

	public void setReleasedTime(String releasedTime) {
		this.releasedTime = releasedTime;
	}

	public String getSingleno() {
		return this.singleno;
	}

	public void setSingleno(String singleno) {
		this.singleno = singleno;
	}

	public String getReceivingTime() {
		return this.receivingTime;
	}

	public void setReceivingTime(String receivingTime) {
		this.receivingTime = receivingTime;
	}

	public String getIsTransfer() {
		return this.isTransfer;
	}

	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}

	public String getTransferDate() {
		return this.transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public String getReceivingPerson() {
		return this.receivingPerson;
	}

	public void setReceivingPerson(String receivingPerson) {
		this.receivingPerson = receivingPerson;
	}

	public String getReceivingPhone() {
		return this.receivingPhone;
	}

	public void setReceivingPhone(String receivingPhone) {
		this.receivingPhone = receivingPhone;
	}

	public String getReceivingDate() {
		return this.receivingDate;
	}

	public void setReceivingDate(String receivingDate) {
		this.receivingDate = receivingDate;
	}

	public String getMaintStartTime() {
		return this.maintStartTime;
	}

	public void setMaintStartTime(String maintStartTime) {
		this.maintStartTime = maintStartTime;
	}

	public Double getBeginLongitude() {
		return this.beginLongitude;
	}

	public void setBeginLongitude(Double beginLongitude) {
		this.beginLongitude = beginLongitude;
	}

	public Double getBeginDimension() {
		return this.beginDimension;
	}

	public void setBeginDimension(Double beginDimension) {
		this.beginDimension = beginDimension;
	}

	public String getMaintStartAddres() {
		return this.maintStartAddres;
	}

	public void setMaintStartAddres(String maintStartAddres) {
		this.maintStartAddres = maintStartAddres;
	}

	public String getMaintEndTime() {
		return this.maintEndTime;
	}

	public void setMaintEndTime(String maintEndTime) {
		this.maintEndTime = maintEndTime;
	}

	public Double getEndLongitude() {
		return this.endLongitude;
	}

	public void setEndLongitude(Double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public Double getEndDimension() {
		return this.endDimension;
	}

	public void setEndDimension(Double endDimension) {
		this.endDimension = endDimension;
	}

	public String getMaintEndAddres() {
		return this.maintEndAddres;
	}

	public void setMaintEndAddres(String maintEndAddres) {
		this.maintEndAddres = maintEndAddres;
	}

	public String getAuditCircu() {
		return this.auditCircu;
	}

	public void setAuditCircu(String auditCircu) {
		this.auditCircu = auditCircu;
	}

	public String getRem() {
		return this.rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getFittingsReplace() {
		return this.fittingsReplace;
	}

	public void setFittingsReplace(String fittingsReplace) {
		this.fittingsReplace = fittingsReplace;
	}

	public String getIsInvoice() {
		return this.isInvoice;
	}

	public void setIsInvoice(String isInvoice) {
		this.isInvoice = isInvoice;
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

	public String getHandleStatus() {
		return this.handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}

	public String getByAuditOperid() {
		return this.byAuditOperid;
	}

	public void setByAuditOperid(String byAuditOperid) {
		this.byAuditOperid = byAuditOperid;
	}

	public String getByAuditDate() {
		return this.byAuditDate;
	}

	public void setByAuditDate(String byAuditDate) {
		this.byAuditDate = byAuditDate;
	}

}