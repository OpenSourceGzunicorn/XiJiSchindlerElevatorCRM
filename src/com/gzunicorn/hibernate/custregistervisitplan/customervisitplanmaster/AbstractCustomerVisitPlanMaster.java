package com.gzunicorn.hibernate.custregistervisitplan.customervisitplanmaster;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractCustomerVisitPlanMaster entity provides the base persistence
 * definition of the CustomerVisitPlanMaster entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractCustomerVisitPlanMaster implements
		java.io.Serializable {

	// Fields

	private String billno;
	private String companyId;
	private String companyName;
	private String principalName;
	private String principalPhone;
	private String custLevel;
	private String maintDivision;
	private String maintStation;
	private String rem;
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
	private String maintContractNo;
	private Set customerVisitPlanDetails = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractCustomerVisitPlanMaster() {
	}

	/** minimal constructor */
	public AbstractCustomerVisitPlanMaster(String billno, String companyId,
			String companyName, String principalName, String principalPhone,
			String custLevel, String maintDivision, String maintStation,
			String operId, String operDate) {
		this.billno = billno;
		this.companyId = companyId;
		this.companyName = companyName;
		this.principalName = principalName;
		this.principalPhone = principalPhone;
		this.custLevel = custLevel;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractCustomerVisitPlanMaster(String billno, String companyId,
			String companyName, String principalName, String principalPhone,
			String custLevel, String maintDivision, String maintStation,
			String rem, String operId, String operDate, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10, String maintContractNo,
			Set customerVisitPlanDetails) {
		this.billno = billno;
		this.companyId = companyId;
		this.companyName = companyName;
		this.principalName = principalName;
		this.principalPhone = principalPhone;
		this.custLevel = custLevel;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
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
		this.maintContractNo = maintContractNo;
		this.customerVisitPlanDetails = customerVisitPlanDetails;
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

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPrincipalName() {
		return this.principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getPrincipalPhone() {
		return this.principalPhone;
	}

	public void setPrincipalPhone(String principalPhone) {
		this.principalPhone = principalPhone;
	}

	public String getCustLevel() {
		return this.custLevel;
	}

	public void setCustLevel(String custLevel) {
		this.custLevel = custLevel;
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

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public Set getCustomerVisitPlanDetails() {
		return this.customerVisitPlanDetails;
	}

	public void setCustomerVisitPlanDetails(Set customerVisitPlanDetails) {
		this.customerVisitPlanDetails = customerVisitPlanDetails;
	}

}