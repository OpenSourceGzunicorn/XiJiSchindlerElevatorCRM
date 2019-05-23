package com.gzunicorn.hibernate.custregistervisitplan.customervisitplandetail;

import java.util.Set;

import com.gzunicorn.hibernate.custregistervisitplan.customervisitplanmaster.CustomerVisitPlanMaster;

/**
 * CustomerVisitPlanDetail entity. @author MyEclipse Persistence Tools
 */
public class CustomerVisitPlanDetail extends AbstractCustomerVisitPlanDetail
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public CustomerVisitPlanDetail() {
	}

	/** minimal constructor */
	public CustomerVisitPlanDetail(String jnlno,
			String billno, String visitDate,
			String visitStaff) {
		super(jnlno, billno, visitDate, visitStaff);
	}

	/** full constructor */
	public CustomerVisitPlanDetail(String jnlno,
			String billno, String visitDate,
			String visitStaff, String rem, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String fkOperId, String fkOperDate,
			String visitPosition, String submitType, String auditOperid,
			String auditStatus, String auditDate, String auditRem,
			String bhRem, String bhDate, Set customerVisitDispatchings,
			Set customerVisitFeedbacks,
			String visitPeople,String visitPeoplePhone,
			String managerFollow,String managerFollowDate,String managerFollowRem,
			String fMinisterFollow,String fMinisterFollowDate,String fMinisterFollowRem,
			String zMinisterFollow,String zMinisterFollowDate,String zMinisterFollowRem
			) {
		super(jnlno, billno, visitDate, visitStaff, rem, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10, fkOperId, fkOperDate,
				visitPosition, submitType, auditOperid, auditStatus, auditDate,
				auditRem, bhRem, bhDate, customerVisitDispatchings,
				customerVisitFeedbacks, visitPeople, visitPeoplePhone,
				managerFollow,managerFollowDate,managerFollowRem,
				fMinisterFollow,fMinisterFollowDate,fMinisterFollowRem,
				zMinisterFollow,zMinisterFollowDate,zMinisterFollowRem);
	}

}
