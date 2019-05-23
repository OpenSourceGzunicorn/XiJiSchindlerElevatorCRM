package com.gzunicorn.hibernate.hotlinemanagement.smshistory;

/**
 * AbstractSmsHistory entity provides the base persistence definition of the
 * SmsHistory entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractSmsHistory implements java.io.Serializable {

	// Fields

	private Integer id;
	private String smsTel;
	private String smsContent;
	private String smsSendTime;
	private Integer flag;

	// Constructors

	/** default constructor */
	public AbstractSmsHistory() {
	}

	/** full constructor */
	public AbstractSmsHistory(String smsTel, String smsContent,
			String smsSendTime, Integer flag) {
		this.smsTel = smsTel;
		this.smsContent = smsContent;
		this.smsSendTime = smsSendTime;
		this.flag = flag;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}