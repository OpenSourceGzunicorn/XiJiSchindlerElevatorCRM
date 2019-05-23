package com.gzunicorn.hibernate.custregistervisitplan.custreturnregisterlssue;

import com.gzunicorn.hibernate.custregistervisitplan.custreturnregisterdetail.CustReturnRegisterDetail;

/**
 * CustReturnRegisterLssue entity. @author MyEclipse Persistence Tools
 */
public class CustReturnRegisterLssue extends AbstractCustReturnRegisterLssue
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public CustReturnRegisterLssue() {
	}

	/** minimal constructor */
	public CustReturnRegisterLssue(
			CustReturnRegisterDetail custReturnRegisterDetail,
			String lssueSort, String lssueDetail) {
		super(custReturnRegisterDetail, lssueSort, lssueDetail);
	}

	/** full constructor */
	public CustReturnRegisterLssue(
			CustReturnRegisterDetail custReturnRegisterDetail,
			String lssueSort, String lssueDetail, String r1, String r2,
			String r3, String r4, String r5, Double r6, Integer r7, Integer r8,
			Double r9, Double r10) {
		super(custReturnRegisterDetail, lssueSort, lssueDetail, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10);
	}

}
