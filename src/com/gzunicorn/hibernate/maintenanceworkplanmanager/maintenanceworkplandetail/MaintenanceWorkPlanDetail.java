package com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail;

import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanmaster.MaintenanceWorkPlanMaster;

/**
 * MaintenanceWorkPlanDetail entity. @author MyEclipse Persistence Tools
 */
public class MaintenanceWorkPlanDetail extends
		AbstractMaintenanceWorkPlanDetail implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintenanceWorkPlanDetail() {
	}

	/** minimal constructor */
	public MaintenanceWorkPlanDetail(
			MaintenanceWorkPlanMaster maintenanceWorkPlanMaster,
			String maintPersonnel, String week, String maintDate,
			String maintType, String maintDateTime, String operId,
			String operDate) {
		super(maintenanceWorkPlanMaster, maintPersonnel, week, maintDate,
				maintType, maintDateTime, operId, operDate);
	}

	/** full constructor */
	public MaintenanceWorkPlanDetail(
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
			String byAuditDate,Double startDistance, Double endDistance,Double distanceScore, 
			Double dateScore, Double maintScore,String djOperId2,String djOperDate2,
			String stopTime, Double stopLongitude,Double stopDimension, String stopAddres,
			String restartTime, Double restartLongitude,Double restartDimension, String restartAddres,
			Double stopDistance,Double restartDistance,
			Double beginLongitudeGPS,Double beginDimensionGPS,Double endLongitudeGPS,Double endDimensionGPS,
			Double stopLongitudeGPS,Double stopDimensionGPS,Double restartLongitudeGPS,Double restartDimensionGPS,
			String customerSignature,String customerImage
			) {
		super(maintenanceWorkPlanMaster, maintPersonnel, week, maintDate,
				maintType, maintDateTime, releasedTime, singleno,
				receivingTime, isTransfer, transferDate, receivingPerson,
				receivingPhone, receivingDate, maintStartTime, beginLongitude,
				beginDimension, maintStartAddres, maintEndTime, endLongitude,
				endDimension, maintEndAddres, usedDuration, auditCircu, rem,
				fittingsReplace, isInvoice, operId, operDate, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10, handleStatus, byAuditOperid,
				byAuditDate, startDistance,endDistance,distanceScore,dateScore,maintScore,
				djOperId2,djOperDate2,
				 stopTime,  stopLongitude, stopDimension,  stopAddres,
				 restartTime,  restartLongitude, restartDimension,  restartAddres,
				  stopDistance, restartDistance,
				   beginLongitudeGPS, beginDimensionGPS, endLongitudeGPS, endDimensionGPS,
				   stopLongitudeGPS, stopDimensionGPS, restartLongitudeGPS, restartDimensionGPS,
				   customerSignature,customerImage
				);
	}

}
