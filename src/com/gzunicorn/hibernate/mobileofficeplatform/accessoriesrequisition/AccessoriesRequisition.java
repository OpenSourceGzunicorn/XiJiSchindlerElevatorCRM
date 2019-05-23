package com.gzunicorn.hibernate.mobileofficeplatform.accessoriesrequisition;

/**
 * AccessoriesRequisition entity. @author MyEclipse Persistence Tools
 */
public class AccessoriesRequisition extends AbstractAccessoriesRequisition
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public AccessoriesRequisition() {
	}

	/** minimal constructor */
	public AccessoriesRequisition(String appNo, String singleNo, String oldNo,
			String oldImage, String operId, String operDate,
			String maintDivision, String maintStation) {
		super(appNo, singleNo, oldNo, oldImage, operId, operDate,
				maintDivision, maintStation);
	}

	/** full constructor */
	public AccessoriesRequisition(String appNo, String singleNo, String oldNo,
			String oldImage, String newNo, String newImage, String operId,
			String operDate, String maintDivision, String maintStation,
			String personInCharge, String picauditRem, String picauditDate,
			String isAgree, String warehouseManager, String wmdate, String r1,
			String r2, String r3, String r4, String r5, Double r6, Integer r7,
			Integer r8, Double r9, Double r10,
			String elevatorNo,String isCharges,Double money1,String wmRem,String wmIsAgree,
			String wmIsCharges,String wmPayment,String money2,String expressNo,String expressName,
			String isConfirm,String ghImage,String jjReturn,String jjResult,String jjOperId,String jjOperDate,
			String handleStatus,
			String wmuserId,String ckoperid,String ckdate,String ckrem,String ckiswc,
			String yjaddress,String yjphone) {
		super(appNo, singleNo, oldNo, oldImage, newNo, newImage, operId,
				operDate, maintDivision, maintStation, personInCharge,
				picauditRem, picauditDate, isAgree, warehouseManager, wmdate,
				r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				elevatorNo,isCharges,money1,wmRem,wmIsAgree,
				wmIsCharges,wmPayment,money2,expressNo,expressName,
				isConfirm,ghImage,jjReturn,jjResult,jjOperId,jjOperDate,handleStatus,
				wmuserId,ckoperid,ckdate,ckrem,ckiswc,yjaddress,yjphone);
	}

}
