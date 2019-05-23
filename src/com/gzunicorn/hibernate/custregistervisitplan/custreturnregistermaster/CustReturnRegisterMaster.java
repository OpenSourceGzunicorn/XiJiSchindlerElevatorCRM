package com.gzunicorn.hibernate.custregistervisitplan.custreturnregistermaster;

import java.util.Set;

/**
 * CustReturnRegisterMaster entity. @author MyEclipse Persistence Tools
 */
public class CustReturnRegisterMaster extends AbstractCustReturnRegisterMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public CustReturnRegisterMaster() {
	}

	/** minimal constructor */
	public CustReturnRegisterMaster(String billno, String companyId,
			String contacts, String contactPhone, String ministerHandle,
			String isProblem, String operId, String operDate,
			String maintDivision) {
		super(billno, companyId, contacts, contactPhone, ministerHandle,
				isProblem, operId, operDate, maintDivision);
	}

	/** full constructor */
	public CustReturnRegisterMaster(String billno, String companyId,
			String contacts, String contactPhone, String ministerHandle,
			String isProblem, String handleId, String handleDate,
			String handleResult, String returnResult, String rem,
			String operId, String operDate, String maintDivision, String r1,
			String r2, String r3, String r4, String r5, Double r6, Integer r7,
			Integer r8, Double r9, Double r10, String returnRem,
			Set custReturnRegisterDetails,String workisdisplay) {
		super(billno, companyId, contacts, contactPhone, ministerHandle,
				isProblem, handleId, handleDate, handleResult, returnResult,
				rem, operId, operDate, maintDivision, r1, r2, r3, r4, r5, r6,
				r7, r8, r9, r10, returnRem, custReturnRegisterDetails,workisdisplay);
	}

}
