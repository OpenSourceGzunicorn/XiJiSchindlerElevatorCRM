package com.gzunicorn.hibernate.basedata.principal;

/**
 * Principal entity. @author MyEclipse Persistence Tools
 */
public class Principal extends AbstractPrincipal implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public Principal() {
	}

	/** minimal constructor */
	public Principal(String principalId, String principalName, String phone,
			String enabledFlag) {
		super(principalId, principalName, phone, enabledFlag);
	}

	/** full constructor */
	public Principal(String principalId, String principalName, String phone,
			String custLevel, String enabledFlag, String rem, String r1,
			String r2, String r3, String r4, String r5) {
		super(principalId, principalName, phone, custLevel, enabledFlag, rem,
				r1, r2, r3, r4, r5);
	}

}
