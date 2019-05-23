package com.gzunicorn.hibernate.custregistervisitplan.customervisitdispatching;

import com.gzunicorn.hibernate.custregistervisitplan.customervisitplandetail.CustomerVisitPlanDetail;

/**
 * CustomerVisitDispatching entity. @author MyEclipse Persistence Tools
 */
public class CustomerVisitDispatching extends AbstractCustomerVisitDispatching
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public CustomerVisitDispatching() {
	}

	/** minimal constructor */
	public CustomerVisitDispatching(
			CustomerVisitPlanDetail customerVisitPlanDetail, String transfeRem) {
		super(customerVisitPlanDetail, transfeRem);
	}

	/** full constructor */
	public CustomerVisitDispatching(
			CustomerVisitPlanDetail customerVisitPlanDetail, String transferId,
			String transferDate, String transfeRem, String visitPosition,
			String visitStaff, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(customerVisitPlanDetail, transferId, transferDate, transfeRem,
				visitPosition, visitStaff, r1, r2, r3, r4, r5, r6, r7, r8, r9,
				r10);
	}

}
