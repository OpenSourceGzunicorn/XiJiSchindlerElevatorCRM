package com.gzunicorn.hibernate.custregistervisitplan.customervisitplandetail;

import java.util.HashSet;
import java.util.Set;

import com.gzunicorn.hibernate.custregistervisitplan.customervisitplanmaster.CustomerVisitPlanMaster;

/**
 * AbstractCustomerVisitPlanDetail entity provides the base persistence
 * definition of the CustomerVisitPlanDetail entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractCustomerVisitPlanDetail implements
		java.io.Serializable {

	// Fields

	private String jnlno;
	private String billno;
	private String visitDate;
	private String visitStaff;
	private String rem;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private Double r6;
	private Double r7;
	private Double r8;
	private Integer r9;
	private Integer r10;
	private String fkOperId;
	private String fkOperDate;
	private String visitPosition;
	private String submitType;
	private String auditOperid;
	private String auditStatus;
	private String auditDate;
	private String auditRem;
	private String bhRem;
	private String bhDate;
	private Set customerVisitDispatchings = new HashSet(0);
	private Set customerVisitFeedbacks = new HashSet(0);
	private String visitPeople;
	private String visitPeoplePhone;

	private String managerFollow;
	private String managerFollowDate;
	private String managerFollowRem;
	private String fMinisterFollow;
	private String fMinisterFollowDate;
	private String fMinisterFollowRem;
	private String zMinisterFollow;
	private String zMinisterFollowDate;
	private String zMinisterFollowRem;

	// Constructors
	
	public String getManagerFollow() {
		return managerFollow;
	}

	public void setManagerFollow(String managerFollow) {
		this.managerFollow = managerFollow;
	}

	public String getManagerFollowDate() {
		return managerFollowDate;
	}

	public void setManagerFollowDate(String managerFollowDate) {
		this.managerFollowDate = managerFollowDate;
	}

	public String getManagerFollowRem() {
		return managerFollowRem;
	}

	public void setManagerFollowRem(String managerFollowRem) {
		this.managerFollowRem = managerFollowRem;
	}

	public String getfMinisterFollow() {
		return fMinisterFollow;
	}

	public void setfMinisterFollow(String fMinisterFollow) {
		this.fMinisterFollow = fMinisterFollow;
	}

	public String getfMinisterFollowDate() {
		return fMinisterFollowDate;
	}

	public void setfMinisterFollowDate(String fMinisterFollowDate) {
		this.fMinisterFollowDate = fMinisterFollowDate;
	}

	public String getfMinisterFollowRem() {
		return fMinisterFollowRem;
	}

	public void setfMinisterFollowRem(String fMinisterFollowRem) {
		this.fMinisterFollowRem = fMinisterFollowRem;
	}

	public String getzMinisterFollow() {
		return zMinisterFollow;
	}

	public void setzMinisterFollow(String zMinisterFollow) {
		this.zMinisterFollow = zMinisterFollow;
	}

	public String getzMinisterFollowDate() {
		return zMinisterFollowDate;
	}

	public void setzMinisterFollowDate(String zMinisterFollowDate) {
		this.zMinisterFollowDate = zMinisterFollowDate;
	}

	public String getzMinisterFollowRem() {
		return zMinisterFollowRem;
	}

	public void setzMinisterFollowRem(String zMinisterFollowRem) {
		this.zMinisterFollowRem = zMinisterFollowRem;
	}

	public String getVisitPeople() {
		return visitPeople;
	}

	public void setVisitPeople(String visitPeople) {
		this.visitPeople = visitPeople;
	}

	public String getVisitPeoplePhone() {
		return visitPeoplePhone;
	}

	public void setVisitPeoplePhone(String visitPeoplePhone) {
		this.visitPeoplePhone = visitPeoplePhone;
	}

	/** default constructor */
	public AbstractCustomerVisitPlanDetail() {
	}

	/** minimal constructor */
	public AbstractCustomerVisitPlanDetail(String jnlno,
			String billno, String visitDate,
			String visitStaff) {
		this.jnlno = jnlno;
		this.billno = billno;
		this.visitDate = visitDate;
		this.visitStaff = visitStaff;
	}

	/** full constructor */
	public AbstractCustomerVisitPlanDetail(String jnlno,
			String billno, String visitDate,
			String visitStaff, String rem, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String fkOperId, String fkOperDate,
			String visitPosition, String submitType, String auditOperid,
			String auditStatus, String auditDate, String auditRem,
			String bhRem, String bhDate, Set customerVisitDispatchings,
			Set customerVisitFeedbacks,String visitPeople,String visitPeoplePhone,
			String managerFollow,String managerFollowDate,String managerFollowRem,
			String fMinisterFollow,String fMinisterFollowDate,String fMinisterFollowRem,
			String zMinisterFollow,String zMinisterFollowDate,String zMinisterFollowRem
			) {
		this.managerFollow=managerFollow;
		this.managerFollowDate=managerFollowDate;
		this.managerFollowRem=managerFollowRem;
		this.fMinisterFollow=fMinisterFollow;
		this.fMinisterFollowDate=fMinisterFollowDate;
		this.fMinisterFollowRem=fMinisterFollowRem;
		this.zMinisterFollow=zMinisterFollow;
		this.zMinisterFollowDate=zMinisterFollowDate;
		this.zMinisterFollowRem=zMinisterFollowRem;
		this.jnlno = jnlno;
		this.billno = billno;
		this.visitDate = visitDate;
		this.visitStaff = visitStaff;
		this.rem = rem;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.r4 = r4;
		this.r5 = r5;
		this.r6 = r6;
		this.r7 = r7;
		this.r8 = r8;
		this.r9 = r9;
		this.r10 = r10;
		this.fkOperId = fkOperId;
		this.fkOperDate = fkOperDate;
		this.visitPosition = visitPosition;
		this.submitType = submitType;
		this.auditOperid = auditOperid;
		this.auditStatus = auditStatus;
		this.auditDate = auditDate;
		this.auditRem = auditRem;
		this.bhRem = bhRem;
		this.bhDate = bhDate;
		this.customerVisitDispatchings = customerVisitDispatchings;
		this.customerVisitFeedbacks = customerVisitFeedbacks;
		this.visitPeople = visitPeople;
		this.visitPeoplePhone = visitPeoplePhone;
	}

	// Property accessors

	public String getJnlno() {
		return this.jnlno;
	}

	public void setJnlno(String jnlno) {
		this.jnlno = jnlno;
	}

	public String getVisitDate() {
		return this.visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitStaff() {
		return this.visitStaff;
	}

	public void setVisitStaff(String visitStaff) {
		this.visitStaff = visitStaff;
	}

	public String getRem() {
		return this.rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getR1() {
		return this.r1;
	}

	public void setR1(String r1) {
		this.r1 = r1;
	}

	public String getR2() {
		return this.r2;
	}

	public void setR2(String r2) {
		this.r2 = r2;
	}

	public String getR3() {
		return this.r3;
	}

	public void setR3(String r3) {
		this.r3 = r3;
	}

	public String getR4() {
		return this.r4;
	}

	public void setR4(String r4) {
		this.r4 = r4;
	}

	public String getR5() {
		return this.r5;
	}

	public void setR5(String r5) {
		this.r5 = r5;
	}

	public Double getR6() {
		return this.r6;
	}

	public void setR6(Double r6) {
		this.r6 = r6;
	}

	public Double getR7() {
		return this.r7;
	}

	public void setR7(Double r7) {
		this.r7 = r7;
	}

	public Double getR8() {
		return this.r8;
	}

	public void setR8(Double r8) {
		this.r8 = r8;
	}

	public Integer getR9() {
		return this.r9;
	}

	public void setR9(Integer r9) {
		this.r9 = r9;
	}

	public Integer getR10() {
		return this.r10;
	}

	public void setR10(Integer r10) {
		this.r10 = r10;
	}

	public String getFkOperId() {
		return this.fkOperId;
	}

	public void setFkOperId(String fkOperId) {
		this.fkOperId = fkOperId;
	}

	public String getFkOperDate() {
		return this.fkOperDate;
	}

	public void setFkOperDate(String fkOperDate) {
		this.fkOperDate = fkOperDate;
	}

	public String getVisitPosition() {
		return this.visitPosition;
	}

	public void setVisitPosition(String visitPosition) {
		this.visitPosition = visitPosition;
	}

	public String getSubmitType() {
		return this.submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getAuditOperid() {
		return this.auditOperid;
	}

	public void setAuditOperid(String auditOperid) {
		this.auditOperid = auditOperid;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditRem() {
		return this.auditRem;
	}

	public void setAuditRem(String auditRem) {
		this.auditRem = auditRem;
	}

	public String getBhRem() {
		return this.bhRem;
	}

	public void setBhRem(String bhRem) {
		this.bhRem = bhRem;
	}

	public String getBhDate() {
		return this.bhDate;
	}

	public void setBhDate(String bhDate) {
		this.bhDate = bhDate;
	}
	
	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public Set getCustomerVisitDispatchings() {
		return this.customerVisitDispatchings;
	}

	public void setCustomerVisitDispatchings(Set customerVisitDispatchings) {
		this.customerVisitDispatchings = customerVisitDispatchings;
	}

	public Set getCustomerVisitFeedbacks() {
		return this.customerVisitFeedbacks;
	}

	public void setCustomerVisitFeedbacks(Set customerVisitFeedbacks) {
		this.customerVisitFeedbacks = customerVisitFeedbacks;
	}

}