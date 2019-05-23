package com.gzunicorn.hibernate.custregistervisitplan.LostElevatorMaintain;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractLostElevatorMaintain entity provides the base persistence definition
 * of the LostElevatorMaintain entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractLostElevatorMaintain implements
		java.io.Serializable {

	// Fields

	private String billno;
	private String contacts;
	private String contactPhone;
	private String companyId;
	private Integer reOrder;
	private String reMark;
	private String enabledFlag;
	private String rem;
	private String operId;
	private String operDate;
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
	private Set lostElevatorMaintainDetails = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractLostElevatorMaintain() {
	}

	/** minimal constructor */
	public AbstractLostElevatorMaintain(String contacts, String contactPhone,
			String companyId, Integer reOrder, String reMark,
			String enabledFlag, String operId, String operDate) {
		this.contacts = contacts;
		this.contactPhone = contactPhone;
		this.companyId = companyId;
		this.reOrder = reOrder;
		this.reMark = reMark;
		this.enabledFlag = enabledFlag;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractLostElevatorMaintain(String contacts, String contactPhone,
			String companyId, Integer reOrder, String reMark,
			String enabledFlag, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Integer r7, Integer r8, Double r9, Double r10,
			Set lostElevatorMaintainDetails) {
		this.contacts = contacts;
		this.contactPhone = contactPhone;
		this.companyId = companyId;
		this.reOrder = reOrder;
		this.reMark = reMark;
		this.enabledFlag = enabledFlag;
		this.rem = rem;
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
		this.lostElevatorMaintainDetails = lostElevatorMaintainDetails;
	}

	// Property accessors

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getContacts() {
		return this.contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getReOrder() {
		return this.reOrder;
	}

	public void setReOrder(Integer reOrder) {
		this.reOrder = reOrder;
	}

	public String getReMark() {
		return this.reMark;
	}

	public void setReMark(String reMark) {
		this.reMark = reMark;
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

	public Set getLostElevatorMaintainDetails() {
		return this.lostElevatorMaintainDetails;
	}

	public void setLostElevatorMaintainDetails(Set lostElevatorMaintainDetails) {
		this.lostElevatorMaintainDetails = lostElevatorMaintainDetails;
	}

}