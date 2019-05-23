package com.gzunicorn.hibernate.custregistervisitplan.customervisitfeedback;

import com.gzunicorn.hibernate.custregistervisitplan.customervisitplandetail.CustomerVisitPlanDetail;

/**
 * CustomerVisitFeedback entity. @author MyEclipse Persistence Tools
 */
public class CustomerVisitFeedback extends AbstractCustomerVisitFeedback
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public CustomerVisitFeedback() {
	}

	/** minimal constructor */
	public CustomerVisitFeedback(Integer numno,
			CustomerVisitPlanDetail customerVisitPlanDetail,
			String visitProject, String realVisitDate, String visitContent) {
		super(numno, customerVisitPlanDetail, visitProject, realVisitDate,
				visitContent);
	}

	/** full constructor */
	public CustomerVisitFeedback(Integer numno,
			CustomerVisitPlanDetail customerVisitPlanDetail,
			String visitProject, String realVisitDate, String visitContent,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10,
			String maintContractNo) {
		super(numno, customerVisitPlanDetail, visitProject, realVisitDate,
				visitContent, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				maintContractNo);
	}

}
