package com.gzunicorn.hibernate.mobileofficeplatform.faultrepairentrymaster;

/**
 * AbstractFaultRepairEntryMaster entity provides the base persistence
 * definition of the FaultRepairEntryMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractFaultRepairEntryMaster implements
		java.io.Serializable {

	// Fields

	private String appNo;
	private String repairName;
	private String repairPhone;
	private String repairDesc;
	private String projectName;
	private String projectAddress;
	private String isTiring;
	private String operId;
	private String operDate;
	private String isProcess;
	private String dealId;
	private String dealDate;
	private String rem;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private String r6;
	private String r7;
	private String r8;
	private String r9;
	private String r10;

	// Constructors

	/** default constructor */
	public AbstractFaultRepairEntryMaster() {
	}

	/** minimal constructor */
	public AbstractFaultRepairEntryMaster(String appNo, String repairName,
			String repairPhone, String repairDesc) {
		this.appNo = appNo;
		this.repairName = repairName;
		this.repairPhone = repairPhone;
		this.repairDesc = repairDesc;
	}

	/** full constructor */
	public AbstractFaultRepairEntryMaster(String appNo, String repairName,
			String repairPhone, String repairDesc, String projectName,
			String projectAddress, String isTiring, String operId,
			String operDate, String isProcess, String dealId, String dealDate,String rem,
			String r1, String r2, String r3, String r4, String r5, String r6,
			String r7, String r8, String r9, String r10) {
		this.appNo = appNo;
		this.repairName = repairName;
		this.repairPhone = repairPhone;
		this.repairDesc = repairDesc;
		this.projectName = projectName;
		this.projectAddress = projectAddress;
		this.isTiring = isTiring;
		this.operId = operId;
		this.operDate = operDate;
		this.isProcess = isProcess;
		this.dealId = dealId;
		this.dealDate = dealDate;
		this.rem=rem;
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
	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}
	public String getAppNo() {
		return this.appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}


	public String getRepairName() {
		return this.repairName;
	}

	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}

	public String getRepairPhone() {
		return this.repairPhone;
	}

	public void setRepairPhone(String repairPhone) {
		this.repairPhone = repairPhone;
	}

	public String getRepairDesc() {
		return this.repairDesc;
	}

	public void setRepairDesc(String repairDesc) {
		this.repairDesc = repairDesc;
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

	public String getIsTiring() {
		return this.isTiring;
	}

	public void setIsTiring(String isTiring) {
		this.isTiring = isTiring;
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

	public String getIsProcess() {
		return this.isProcess;
	}

	public void setIsProcess(String isProcess) {
		this.isProcess = isProcess;
	}

	public String getDealId() {
		return this.dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getDealDate() {
		return this.dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
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

	public String getR6() {
		return this.r6;
	}

	public void setR6(String r6) {
		this.r6 = r6;
	}

	public String getR7() {
		return this.r7;
	}

	public void setR7(String r7) {
		this.r7 = r7;
	}

	public String getR8() {
		return this.r8;
	}

	public void setR8(String r8) {
		this.r8 = r8;
	}

	public String getR9() {
		return this.r9;
	}

	public void setR9(String r9) {
		this.r9 = r9;
	}

	public String getR10() {
		return this.r10;
	}

	public void setR10(String r10) {
		this.r10 = r10;
	}

}