package com.gzunicorn.hibernate.custregistervisitplan.custreturnregistermaster;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractCustReturnRegisterMaster entity provides the base persistence
 * definition of the CustReturnRegisterMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractCustReturnRegisterMaster implements
		java.io.Serializable {

	// Fields

	private String billno;
	private String companyId;
	private String contacts;
	private String contactPhone;
	private String ministerHandle;
	private String isProblem;
	private String handleId;
	private String handleDate;
	private String handleResult;
	private String returnResult;
	private String rem;
	private String operId;
	private String operDate;
	private String maintDivision;
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
	private String returnRem;
	private Set custReturnRegisterDetails = new HashSet(0);
	private String workisdisplay;
	private String startResult;

	// Constructors

	public String getWorkisdisplay() {
		return workisdisplay;
	}

	public void setWorkisdisplay(String workisdisplay) {
		this.workisdisplay = workisdisplay;
	}

	/** default constructor */
	public AbstractCustReturnRegisterMaster() {
	}

	/** minimal constructor */
	public AbstractCustReturnRegisterMaster(String billno, String companyId,
			String contacts, String contactPhone, String ministerHandle,
			String isProblem, String operId, String operDate,
			String maintDivision) {
		this.billno = billno;
		this.companyId = companyId;
		this.contacts = contacts;
		this.contactPhone = contactPhone;
		this.ministerHandle = ministerHandle;
		this.isProblem = isProblem;
		this.operId = operId;
		this.operDate = operDate;
		this.maintDivision = maintDivision;
	}

	/** full constructor */
	public AbstractCustReturnRegisterMaster(String billno, String companyId,
			String contacts, String contactPhone, String ministerHandle,
			String isProblem, String handleId, String handleDate,
			String handleResult, String returnResult, String rem,
			String operId, String operDate, String maintDivision, String r1,
			String r2, String r3, String r4, String r5, Double r6, Integer r7,
			Integer r8, Double r9, Double r10, String returnRem,
			Set custReturnRegisterDetails,String workisdisplay) {
		this.billno = billno;
		this.companyId = companyId;
		this.contacts = contacts;
		this.contactPhone = contactPhone;
		this.ministerHandle = ministerHandle;
		this.isProblem = isProblem;
		this.handleId = handleId;
		this.handleDate = handleDate;
		this.handleResult = handleResult;
		this.returnResult = returnResult;
		this.rem = rem;
		this.operId = operId;
		this.operDate = operDate;
		this.maintDivision = maintDivision;
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
		this.returnRem = returnRem;
		this.custReturnRegisterDetails = custReturnRegisterDetails;
		this.workisdisplay=workisdisplay;
	}

	// Property accessors

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

	public String getMinisterHandle() {
		return this.ministerHandle;
	}

	public void setMinisterHandle(String ministerHandle) {
		this.ministerHandle = ministerHandle;
	}

	public String getIsProblem() {
		return this.isProblem;
	}

	public void setIsProblem(String isProblem) {
		this.isProblem = isProblem;
	}

	public String getHandleId() {
		return this.handleId;
	}

	public void setHandleId(String handleId) {
		this.handleId = handleId;
	}

	public String getHandleDate() {
		return this.handleDate;
	}

	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}

	public String getHandleResult() {
		return this.handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	public String getReturnResult() {
		return this.returnResult;
	}

	public void setReturnResult(String returnResult) {
		this.returnResult = returnResult;
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

	public String getMaintDivision() {
		return this.maintDivision;
	}

	public void setMaintDivision(String maintDivision) {
		this.maintDivision = maintDivision;
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

	public String getReturnRem() {
		return this.returnRem;
	}

	public void setReturnRem(String returnRem) {
		this.returnRem = returnRem;
	}

	public Set getCustReturnRegisterDetails() {
		return this.custReturnRegisterDetails;
	}

	public void setCustReturnRegisterDetails(Set custReturnRegisterDetails) {
		this.custReturnRegisterDetails = custReturnRegisterDetails;
	}

	public String getStartResult() {
		return startResult;
	}

	public void setStartResult(String startResult) {
		this.startResult = startResult;
	}
	
}