package com.gzunicorn.hibernate.hotlinemanagement.calloutsms;

/**
 * CalloutSms entity. @author MyEclipse Persistence Tools
 */
public class CalloutSms extends AbstractCalloutSms implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public CalloutSms() {
	}

	/** full constructor */
	public CalloutSms(String smsTel, String smsContent, String smsSendTime,
			String smsTel2, String smsContent2, String smsSendTime2,
			String smsTel3, String smsSendTime3, String serviceRating,
			String otherRem, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9, Double r10) {
		super(smsTel, smsContent, smsSendTime, smsTel2, smsContent2,
				smsSendTime2, smsTel3, smsSendTime3, serviceRating, otherRem,
				r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
