package com.gzunicorn.hibernate.hotlinemanagement.calloutprocess;

/**
 * CalloutProcess entity. @author MyEclipse Persistence Tools
 */
public class CalloutProcess extends AbstractCalloutProcess implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public CalloutProcess() {
	}

	/** minimal constructor */
	public CalloutProcess(String calloutMasterNo, String assignObject2) {
		super(calloutMasterNo, assignObject2);
	}

	/** full constructor */
	public CalloutProcess(String calloutMasterNo, String assignObject2,
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
		super(calloutMasterNo, assignObject2, turnSendTime, turnSendId,
				turnSendTel, assignTime, assignUser, assignUserTel,
				arriveDateTime, arriveLongitude, arriveLatitude, deviceId,
				hmtId, faultCode, faultStatus, isStop, completeTime,
				fninishLongitude, fninishLatitude, newFittingName, num, hftId,
				isToll, money, processDesc, serviceRem, r1, r2, r3, r4, r5, r6,
				r7, r8, r9, r10, arriveLocation, fninishLocation, stopTime,
				stopLongitude, stopLatitude, stopLocation,
				isgzbgs,stopRem,
				arriveLongitudeGPS,arriveLatitudeGPS,stopLongitudeGPS,stopLatitudeGPS,
				fninishLongitudeGPS,fninishLatitudeGPS,
				customerSignature,customerImage,arriveDate,arriveTime
				);
	}

}
