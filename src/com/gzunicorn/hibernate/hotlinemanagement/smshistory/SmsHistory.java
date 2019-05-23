package com.gzunicorn.hibernate.hotlinemanagement.smshistory;

/**
 * SmsHistory entity. @author MyEclipse Persistence Tools
 */
public class SmsHistory extends AbstractSmsHistory implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public SmsHistory() {
	}

	/** full constructor */
	public SmsHistory(String smsTel, String smsContent, String smsSendTime,
			Integer flag) {
		super(smsTel, smsContent, smsSendTime, flag);
	}

}
