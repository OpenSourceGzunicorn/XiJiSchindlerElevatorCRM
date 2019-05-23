package com.gzunicorn.hibernate.mobileofficeplatform.accessoriesrequisition;

/**
 * AbstractAccessoriesRequisition entity provides the base persistence
 * definition of the AccessoriesRequisition entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractAccessoriesRequisition implements
		java.io.Serializable {

	// Fields

	private String appNo;
	private String singleNo;
	private String oldNo;
	private String oldImage;
	private String newNo;
	private String newImage;
	private String operId;
	private String operDate;
	private String maintDivision;
	private String maintStation;
	private String personInCharge;
	private String picauditRem;
	private String picauditDate;
	private String isAgree;
	private String warehouseManager;
	private String wmdate;
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
	
	private String elevatorNo;
	private String isCharges;
	private Double money1;
	private String wmRem;
	private String wmIsAgree;
	private String wmIsCharges;
	private String wmPayment;
	private String money2;
	private String expressNo;
	private String expressName;
	private String isConfirm;
	private String ghImage;
	private String jjReturn;
	private String jjResult;
	private String handleStatus;
	private String jjOperId;
	private String jjOperDate;
	private String wmuserId;
	private String ckoperid;
	private String ckdate;
	private String ckrem;
	private String ckiswc;
	private String yjaddress;
	private String yjphone;
	
	private String instock;
	private String invoicetype;
	private String invoiceImage;

	// Constructors
	public String getMoney2() {
		return money2;
	}

	public void setMoney2(String money2) {
		this.money2 = money2;
	}
	public String getYjaddress() {
		return yjaddress;
	}

	public void setYjaddress(String yjaddress) {
		this.yjaddress = yjaddress;
	}

	public String getYjphone() {
		return yjphone;
	}

	public void setYjphone(String yjphone) {
		this.yjphone = yjphone;
	}

	public String getCkoperid() {
		return ckoperid;
	}

	public void setCkoperid(String ckoperid) {
		this.ckoperid = ckoperid;
	}

	public String getCkdate() {
		return ckdate;
	}

	public void setCkdate(String ckdate) {
		this.ckdate = ckdate;
	}

	public String getCkrem() {
		return ckrem;
	}

	public void setCkrem(String ckrem) {
		this.ckrem = ckrem;
	}

	public String getCkiswc() {
		return ckiswc;
	}

	public void setCkiswc(String ckiswc) {
		this.ckiswc = ckiswc;
	}

	public String getWmuserId() {
		return wmuserId;
	}

	public void setWmuserId(String wmuserId) {
		this.wmuserId = wmuserId;
	}

	public String getJjOperId() {
		return jjOperId;
	}

	public void setJjOperId(String jjOperId) {
		this.jjOperId = jjOperId;
	}

	public String getJjOperDate() {
		return jjOperDate;
	}

	public void setJjOperDate(String jjOperDate) {
		this.jjOperDate = jjOperDate;
	}

	public String getElevatorNo() {
		return elevatorNo;
	}

	public void setElevatorNo(String elevatorNo) {
		this.elevatorNo = elevatorNo;
	}

	public String getIsCharges() {
		return isCharges;
	}

	public void setIsCharges(String isCharges) {
		this.isCharges = isCharges;
	}

	public Double getMoney1() {
		return money1;
	}

	public void setMoney1(Double money1) {
		this.money1 = money1;
	}

	public String getWmRem() {
		return wmRem;
	}

	public void setWmRem(String wmRem) {
		this.wmRem = wmRem;
	}

	public String getWmIsAgree() {
		return wmIsAgree;
	}

	public void setWmIsAgree(String wmIsAgree) {
		this.wmIsAgree = wmIsAgree;
	}

	public String getWmIsCharges() {
		return wmIsCharges;
	}

	public void setWmIsCharges(String wmIsCharges) {
		this.wmIsCharges = wmIsCharges;
	}

	public String getWmPayment() {
		return wmPayment;
	}

	public void setWmPayment(String wmPayment) {
		this.wmPayment = wmPayment;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(String isConfirm) {
		this.isConfirm = isConfirm;
	}

	public String getGhImage() {
		return ghImage;
	}

	public void setGhImage(String ghImage) {
		this.ghImage = ghImage;
	}

	public String getJjReturn() {
		return jjReturn;
	}

	public void setJjReturn(String jjReturn) {
		this.jjReturn = jjReturn;
	}

	public String getJjResult() {
		return jjResult;
	}

	public void setJjResult(String jjResult) {
		this.jjResult = jjResult;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}

	/** default constructor */
	public AbstractAccessoriesRequisition() {
	}

	/** minimal constructor */
	public AbstractAccessoriesRequisition(String appNo, String singleNo,
			String oldNo, String oldImage, String operId, String operDate,
			String maintDivision, String maintStation) {
		this.appNo = appNo;
		this.singleNo = singleNo;
		this.oldNo = oldNo;
		this.oldImage = oldImage;
		this.operId = operId;
		this.operDate = operDate;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
	}

	/** full constructor */
	public AbstractAccessoriesRequisition(String appNo, String singleNo,
			String oldNo, String oldImage, String newNo, String newImage,
			String operId, String operDate, String maintDivision,
			String maintStation, String personInCharge, String picauditRem,
			String picauditDate, String isAgree, String warehouseManager,
			String wmdate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9, Double r10,
			String elevatorNo,String isCharges,Double money1,String wmRem,String wmIsAgree,
			String wmIsCharges,String wmPayment,String money2,String expressNo,String expressName,
			String isConfirm,String ghImage,String jjReturn,String jjResult,String jjOperId,String jjOperDate,
			String handleStatus,String wmuserId,String ckoperid,String ckdate,String ckrem,String ckiswc,
			String yjaddress,String yjphone
			) {
		this.yjaddress=yjaddress;
		this.yjphone=yjphone;
		this.ckoperid=ckoperid;
		this.ckdate=ckdate;
		this.ckrem=ckrem;
		this.ckiswc=ckiswc;
		this.wmuserId=wmuserId;
		this.jjOperId=jjOperId;
		this.jjOperDate=jjOperDate;
		this.elevatorNo=elevatorNo;
		this.isCharges=isCharges;
		this.money1=money1;
		this.wmRem=wmRem;
		this.wmIsAgree=wmIsAgree;
		this.wmIsCharges=wmIsCharges;
		this.wmPayment=wmPayment;
		this.money2=money2;
		this.expressNo=expressNo;
		this.expressName=expressName;
		this.isConfirm=isConfirm;
		this.ghImage=ghImage;
		this.jjReturn=jjReturn;
		this.jjResult=jjResult;
		this.handleStatus=handleStatus;
		this.appNo = appNo;
		this.singleNo = singleNo;
		this.oldNo = oldNo;
		this.oldImage = oldImage;
		this.newNo = newNo;
		this.newImage = newImage;
		this.operId = operId;
		this.operDate = operDate;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.personInCharge = personInCharge;
		this.picauditRem = picauditRem;
		this.picauditDate = picauditDate;
		this.isAgree = isAgree;
		this.warehouseManager = warehouseManager;
		this.wmdate = wmdate;
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

	public String getAppNo() {
		return this.appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public String getSingleNo() {
		return this.singleNo;
	}

	public void setSingleNo(String singleNo) {
		this.singleNo = singleNo;
	}

	public String getOldNo() {
		return this.oldNo;
	}

	public void setOldNo(String oldNo) {
		this.oldNo = oldNo;
	}

	public String getOldImage() {
		return this.oldImage;
	}

	public void setOldImage(String oldImage) {
		this.oldImage = oldImage;
	}

	public String getNewNo() {
		return this.newNo;
	}

	public void setNewNo(String newNo) {
		this.newNo = newNo;
	}

	public String getNewImage() {
		return this.newImage;
	}

	public void setNewImage(String newImage) {
		this.newImage = newImage;
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

	public String getPersonInCharge() {
		return this.personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}

	public String getPicauditRem() {
		return this.picauditRem;
	}

	public void setPicauditRem(String picauditRem) {
		this.picauditRem = picauditRem;
	}

	public String getPicauditDate() {
		return this.picauditDate;
	}

	public void setPicauditDate(String picauditDate) {
		this.picauditDate = picauditDate;
	}

	public String getIsAgree() {
		return this.isAgree;
	}

	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}

	public String getWarehouseManager() {
		return this.warehouseManager;
	}

	public void setWarehouseManager(String warehouseManager) {
		this.warehouseManager = warehouseManager;
	}

	public String getWmdate() {
		return this.wmdate;
	}

	public void setWmdate(String wmdate) {
		this.wmdate = wmdate;
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

	public String getInstock() {
		return instock;
	}

	public void setInstock(String instock) {
		this.instock = instock;
	}

	public String getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}

	public String getInvoiceImage() {
		return invoiceImage;
	}

	public void setInvoiceImage(String invoiceImage) {
		this.invoiceImage = invoiceImage;
	}

}