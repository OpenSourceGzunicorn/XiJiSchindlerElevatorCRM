package com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanmaster;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractMaintenanceWorkPlanMaster entity provides the base persistence
 * definition of the MaintenanceWorkPlanMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractMaintenanceWorkPlanMaster implements
		java.io.Serializable {

	// Fields

	private String billno;
	private Integer rowid;
	private String elevatorNo;
	private String checkflag;
	private String checkoperid;
	private String checkdate;
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
	private String submitType;
	private String maintPersonnel;
	private String zpdate;
	private String zpoperdate;
	private String zpoperid;
	private Set maintenanceWorkPlanDetails = new HashSet(0);
	private String operid;
	private String operdate;
	private String maintLogic;

	// Constructors

	public String getMaintLogic() {
		return maintLogic;
	}

	public void setMaintLogic(String maintLogic) {
		this.maintLogic = maintLogic;
	}

	public String getOperid() {
		return operid;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

	public String getOperdate() {
		return operdate;
	}

	public void setOperdate(String operdate) {
		this.operdate = operdate;
	}

	/** default constructor */
	public AbstractMaintenanceWorkPlanMaster() {
	}

	/** minimal constructor */
	public AbstractMaintenanceWorkPlanMaster(String billno, String elevatorNo) {
		this.billno = billno;
		this.elevatorNo = elevatorNo;
	}

	/** full constructor */
	public AbstractMaintenanceWorkPlanMaster(String billno, Integer rowid,
			String elevatorNo, String checkflag, String checkoperid,
			String checkdate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String submitType, String maintPersonnel,
			String zpdate, String zpoperdate, String zpoperid,
			Set maintenanceWorkPlanDetails,String operid, String operdate,
			String maintLogic) {
		this.maintLogic=maintLogic;
		this.billno = billno;
		this.rowid = rowid;
		this.elevatorNo = elevatorNo;
		this.checkflag = checkflag;
		this.checkoperid = checkoperid;
		this.checkdate = checkdate;
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
		this.submitType = submitType;
		this.maintPersonnel = maintPersonnel;
		this.zpdate = zpdate;
		this.zpoperdate = zpoperdate;
		this.zpoperid = zpoperid;
		this.maintenanceWorkPlanDetails = maintenanceWorkPlanDetails;
		this.operid=operid;
		this.operdate=operdate;
	}

	// Property accessors

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public Integer getRowid() {
		return this.rowid;
	}

	public void setRowid(Integer rowid) {
		this.rowid = rowid;
	}

	public String getElevatorNo() {
		return this.elevatorNo;
	}

	public void setElevatorNo(String elevatorNo) {
		this.elevatorNo = elevatorNo;
	}

	public String getCheckflag() {
		return this.checkflag;
	}

	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}

	public String getCheckoperid() {
		return this.checkoperid;
	}

	public void setCheckoperid(String checkoperid) {
		this.checkoperid = checkoperid;
	}

	public String getCheckdate() {
		return this.checkdate;
	}

	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
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

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getMaintPersonnel() {
		return this.maintPersonnel;
	}

	public void setMaintPersonnel(String maintPersonnel) {
		this.maintPersonnel = maintPersonnel;
	}

	public String getZpdate() {
		return this.zpdate;
	}

	public void setZpdate(String zpdate) {
		this.zpdate = zpdate;
	}

	public String getZpoperdate() {
		return this.zpoperdate;
	}

	public void setZpoperdate(String zpoperdate) {
		this.zpoperdate = zpoperdate;
	}

	public String getZpoperid() {
		return this.zpoperid;
	}

	public void setZpoperid(String zpoperid) {
		this.zpoperid = zpoperid;
	}

	public Set getMaintenanceWorkPlanDetails() {
		return this.maintenanceWorkPlanDetails;
	}

	public void setMaintenanceWorkPlanDetails(Set maintenanceWorkPlanDetails) {
		this.maintenanceWorkPlanDetails = maintenanceWorkPlanDetails;
	}

}