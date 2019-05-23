package com.gzunicorn.hibernate.basedata.invoicetype;

/**
 * InvoiceType entity. @author MyEclipse Persistence Tools
 */
public class InvoiceType extends AbstractInvoiceType implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public InvoiceType() {
	}

	/** minimal constructor */
	public InvoiceType(String inTypeId, String inTypeName, String enabledFlag,
			String operId, String operDate) {
		super(inTypeId, inTypeName, enabledFlag, operId, operDate);
	}

	/** full constructor */
	public InvoiceType(String inTypeId, String inTypeName, String enabledFlag,
			String rem, String operId, String operDate, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10) {
		super(inTypeId, inTypeName, enabledFlag, rem, operId, operDate, r1, r2,
				r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
