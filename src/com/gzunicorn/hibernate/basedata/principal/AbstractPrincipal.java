package com.gzunicorn.hibernate.basedata.principal;

/**
 * AbstractPrincipal entity provides the base persistence definition of the
 * Principal entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractPrincipal implements java.io.Serializable {

	// Fields

	private String principalId;
	private String principalName;
	private String phone;
	private String custLevel;
	private String enabledFlag;
	private String rem;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;

	// Constructors

	/** default constructor */
	public AbstractPrincipal() {
	}

	/** minimal constructor */
	public AbstractPrincipal(String principalId, String principalName,
			String phone, String enabledFlag) {
		this.principalId = principalId;
		this.principalName = principalName;
		this.phone = phone;
		this.enabledFlag = enabledFlag;
	}

	/** full constructor */
	public AbstractPrincipal(String principalId, String principalName,
			String phone, String custLevel, String enabledFlag, String rem,
			String r1, String r2, String r3, String r4, String r5) {
		this.principalId = principalId;
		this.principalName = principalName;
		this.phone = phone;
		this.custLevel = custLevel;
		this.enabledFlag = enabledFlag;
		this.rem = rem;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.r4 = r4;
		this.r5 = r5;
	}

	// Property accessors

	public String getPrincipalId() {
		return this.principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getPrincipalName() {
		return this.principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCustLevel() {
		return this.custLevel;
	}

	public void setCustLevel(String custLevel) {
		this.custLevel = custLevel;
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

}