package com.gzunicorn.hibernate.basedata.customer;

/**
 * AbstractCustomer entity provides the base persistence definition of the
 * Customer entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCustomer implements java.io.Serializable {

	// Fields

	private String companyId;
	private String companyName;
	private String address;
	private String legalPerson;
	private String client;
	private String contacts;
	private String contactPhone;
	private String principalId;
	private String principalName;
	private String principalPhone;
	private String fax;
	private String postCode;
	private String bank;
	private String accountHolder;
	private String account;
	private String taxId;
	private String enabledFlag;
	private String rem;
	private String custLevel;
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
	private String cusNature;
	private String invoiceName;
	private String qualiLevelAz;
	private String qualiLevelWb;
	private String qualiLevelWg;

	// Constructors

	/** default constructor */
	public AbstractCustomer() {
	}

	/** minimal constructor */
	public AbstractCustomer(String companyId, String companyName,
			String address, String legalPerson, String client, String contacts,
			String contactPhone, String principalId, String principalName,
			String principalPhone, String enabledFlag, String operId,
			String operDate) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.address = address;
		this.legalPerson = legalPerson;
		this.client = client;
		this.contacts = contacts;
		this.contactPhone = contactPhone;
		this.principalId = principalId;
		this.principalName = principalName;
		this.principalPhone = principalPhone;
		this.enabledFlag = enabledFlag;
		this.operId = operId;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractCustomer(String companyId, String companyName,
			String address, String legalPerson, String client, String contacts,
			String contactPhone, String principalId, String principalName,
			String principalPhone, String fax, String postCode, String bank,
			String accountHolder, String account, String taxId,
			String enabledFlag, String rem, String custLevel, String operId,
			String operDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9,
			Double r10, String cusNature, String invoiceName,
			String qualiLevelAz, String qualiLevelWb, String qualiLevelWg) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.address = address;
		this.legalPerson = legalPerson;
		this.client = client;
		this.contacts = contacts;
		this.contactPhone = contactPhone;
		this.principalId = principalId;
		this.principalName = principalName;
		this.principalPhone = principalPhone;
		this.fax = fax;
		this.postCode = postCode;
		this.bank = bank;
		this.accountHolder = accountHolder;
		this.account = account;
		this.taxId = taxId;
		this.enabledFlag = enabledFlag;
		this.rem = rem;
		this.custLevel = custLevel;
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
		this.cusNature = cusNature;
		this.invoiceName = invoiceName;
		this.qualiLevelAz = qualiLevelAz;
		this.qualiLevelWb = qualiLevelWb;
		this.qualiLevelWg = qualiLevelWg;
	}

	// Property accessors

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

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLegalPerson() {
		return this.legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getClient() {
		return this.client;
	}

	public void setClient(String client) {
		this.client = client;
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

	public String getPrincipalPhone() {
		return this.principalPhone;
	}

	public void setPrincipalPhone(String principalPhone) {
		this.principalPhone = principalPhone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccountHolder() {
		return this.accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTaxId() {
		return this.taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
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

	public String getCustLevel() {
		return this.custLevel;
	}

	public void setCustLevel(String custLevel) {
		this.custLevel = custLevel;
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

	public String getCusNature() {
		return this.cusNature;
	}

	public void setCusNature(String cusNature) {
		this.cusNature = cusNature;
	}

	public String getInvoiceName() {
		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getQualiLevelAz() {
		return this.qualiLevelAz;
	}

	public void setQualiLevelAz(String qualiLevelAz) {
		this.qualiLevelAz = qualiLevelAz;
	}

	public String getQualiLevelWb() {
		return this.qualiLevelWb;
	}

	public void setQualiLevelWb(String qualiLevelWb) {
		this.qualiLevelWb = qualiLevelWb;
	}

	public String getQualiLevelWg() {
		return this.qualiLevelWg;
	}

	public void setQualiLevelWg(String qualiLevelWg) {
		this.qualiLevelWg = qualiLevelWg;
	}

}