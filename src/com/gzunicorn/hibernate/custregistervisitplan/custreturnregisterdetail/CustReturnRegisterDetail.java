package com.gzunicorn.hibernate.custregistervisitplan.custreturnregisterdetail;

import java.util.Set;

import com.gzunicorn.hibernate.custregistervisitplan.custreturnregistermaster.CustReturnRegisterMaster;

/**
 * CustReturnRegisterDetail entity. @author MyEclipse Persistence Tools
 */
public class CustReturnRegisterDetail extends AbstractCustReturnRegisterDetail
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public CustReturnRegisterDetail() {
	}

	/** minimal constructor */
	public CustReturnRegisterDetail(
			CustReturnRegisterMaster custReturnRegisterMaster, String wbBillno) {
		super(custReturnRegisterMaster, wbBillno);
	}

	/** full constructor */
	public CustReturnRegisterDetail(
			CustReturnRegisterMaster custReturnRegisterMaster, String wbBillno,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Integer r7, Integer r8, Double r9, Double r10, String maintContractNo, String contractSdate, String contractEdate,
			Set custReturnRegisterLssues) {
		super(custReturnRegisterMaster, wbBillno, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10,maintContractNo,contractSdate,contractEdate, custReturnRegisterLssues);
	}

}
