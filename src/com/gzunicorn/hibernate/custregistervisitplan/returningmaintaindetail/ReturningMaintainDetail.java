package com.gzunicorn.hibernate.custregistervisitplan.returningmaintaindetail;

/**
 * ReturningMaintainDetail entity. @author MyEclipse Persistence Tools
 */
public class ReturningMaintainDetail extends AbstractReturningMaintainDetail
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ReturningMaintainDetail() {
	}

	/** minimal constructor */
	public ReturningMaintainDetail(String billno, String wbBillno,
			String maintContractNo, String contractSdate, String contractEdate) {
		super(billno, wbBillno, maintContractNo, contractSdate, contractEdate);
	}

	/** full constructor */
	public ReturningMaintainDetail(String billno, String wbBillno,
			String maintContractNo, String contractSdate, String contractEdate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Integer r7, Integer r8, Double r9, Double r10) {
		super(billno, wbBillno, maintContractNo, contractSdate, contractEdate,
				r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
