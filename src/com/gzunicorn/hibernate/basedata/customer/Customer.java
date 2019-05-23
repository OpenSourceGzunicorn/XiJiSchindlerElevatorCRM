package com.gzunicorn.hibernate.basedata.customer;

/**
 * Customer entity. @author MyEclipse Persistence Tools
 */
public class Customer extends AbstractCustomer implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Customer() {
	}

	/** minimal constructor */
	public Customer(String companyId, String companyName, String address,
			String legalPerson, String client, String contacts,
			String contactPhone, String principalId, String principalName,
			String principalPhone, String enabledFlag, String operId,
			String operDate) {
		super(companyId, companyName, address, legalPerson, client, contacts,
				contactPhone, principalId, principalName, principalPhone,
				enabledFlag, operId, operDate);
	}

	/** full constructor */
	public Customer(String companyId, String companyName, String address,
			String legalPerson, String client, String contacts,
			String contactPhone, String principalId, String principalName,
			String principalPhone, String fax, String postCode, String bank,
			String accountHolder, String account, String taxId,
			String enabledFlag, String rem, String custLevel, String operId,
			String operDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9,
			Double r10, String cusNature, String invoiceName,
			String qualiLevelAz, String qualiLevelWb, String qualiLevelWg) {
		super(companyId, companyName, address, legalPerson, client, contacts,
				contactPhone, principalId, principalName, principalPhone, fax,
				postCode, bank, accountHolder, account, taxId, enabledFlag,
				rem, custLevel, operId, operDate, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10, cusNature, invoiceName, qualiLevelAz,
				qualiLevelWb, qualiLevelWg);
	}

}
