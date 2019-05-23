package com.gzunicorn.hibernate.hotlinemanagement.calloutsms;

/**
 * AbstractCalloutSms entity provides the base persistence definition of the
 * CalloutSms entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCalloutSms implements java.io.Serializable {

	// Fields

	private String calloutMasterNo;
	private String smsTel;
	private String smsContent;
	private String smsSendTime;
	private String smsTel2;
	private String smsContent2;
	private String smsSendTime2;
	private String smsTel3;
	private String smsSendTime3;
	private String serviceRating;
	private String otherRem;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private Double r6;
	private Integer r7;
	private Integer r8;
	private Double r9;
	private Double r10;

	// Constructors

	/** default constructor */
	public AbstractCalloutSms() {
	}

	/** full constructor */
	public AbstractCalloutSms(String smsTel, String smsContent,
			String smsSendTime, String smsTel2, String smsContent2,
			String smsSendTime2, String smsTel3, String smsSendTime3,
			String serviceRating, String otherRem, String r1, String r2,
			String r3, String r4, String r5, Double r6, Integer r7, Integer r8,
			Double r9, Double r10) {
		this.smsTel = smsTel;
		this.smsContent = smsContent;
		this.smsSendTime = smsSendTime;
		this.smsTel2 = smsTel2;
		this.smsContent2 = smsContent2;
		this.smsSendTime2 = smsSendTime2;
		this.smsTel3 = smsTel3;
		this.smsSendTime3 = smsSendTime3;
		this.serviceRating = serviceRating;
		this.otherRem = otherRem;
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
	}

	// Property accessors

	public String getCalloutMasterNo() {
		return this.calloutMasterNo;
	}

	public void setCalloutMasterNo(String calloutMasterNo) {
		this.calloutMasterNo = calloutMasterNo;
	}

	public String getSmsTel() {
		return this.smsTel;
	}

	public void setSmsTel(String smsTel) {
		this.smsTel = smsTel;
	}

	public String getSmsContent() {
		return this.smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String getSmsSendTime() {
		return this.smsSendTime;
	}

	public void setSmsSendTime(String smsSendTime) {
		this.smsSendTime = smsSendTime;
	}

	public String getSmsTel2() {
		return this.smsTel2;
	}

	public void setSmsTel2(String smsTel2) {
		this.smsTel2 = smsTel2;
	}

	public String getSmsContent2() {
		return this.smsContent2;
	}

	public void setSmsContent2(String smsContent2) {
		this.smsContent2 = smsContent2;
	}

	public String getSmsSendTime2() {
		return this.smsSendTime2;
	}

	public void setSmsSendTime2(String smsSendTime2) {
		this.smsSendTime2 = smsSendTime2;
	}

	public String getSmsTel3() {
		return this.smsTel3;
	}

	public void setSmsTel3(String smsTel3) {
		this.smsTel3 = smsTel3;
	}

	public String getSmsSendTime3() {
		return this.smsSendTime3;
	}

	public void setSmsSendTime3(String smsSendTime3) {
		this.smsSendTime3 = smsSendTime3;
	}

	public String getServiceRating() {
		return this.serviceRating;
	}

	public void setServiceRating(String serviceRating) {
		this.serviceRating = serviceRating;
	}

	public String getOtherRem() {
		return this.otherRem;
	}

	public void setOtherRem(String otherRem) {
		this.otherRem = otherRem;
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

	public Integer getR7() {
		return this.r7;
	}

	public void setR7(Integer r7) {
		this.r7 = r7;
	}

	public Integer getR8() {
		return this.r8;
	}

	public void setR8(Integer r8) {
		this.r8 = r8;
	}

	public Double getR9() {
		return this.r9;
	}

	public void setR9(Double r9) {
		this.r9 = r9;
	}

	public Double getR10() {
		return this.r10;
	}

	public void setR10(Double r10) {
		this.r10 = r10;
	}

}