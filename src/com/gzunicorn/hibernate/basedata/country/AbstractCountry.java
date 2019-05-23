package com.gzunicorn.hibernate.basedata.country;

/**
 * AbstractCountry entity provides the base persistence definition of the
 * Country entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCountry implements java.io.Serializable {

	// Fields

	private String countryId;
	private String countryName;
	private String enabledFlag;
	private String rem;

	// Constructors

	/** default constructor */
	public AbstractCountry() {
	}

	/** minimal constructor */
	public AbstractCountry(String countryId, String countryName,
			String enabledFlag) {
		this.countryId = countryId;
		this.countryName = countryName;
		this.enabledFlag = enabledFlag;
	}

	/** full constructor */
	public AbstractCountry(String countryId, String countryName,
			String enabledFlag, String rem) {
		this.countryId = countryId;
		this.countryName = countryName;
		this.enabledFlag = enabledFlag;
		this.rem = rem;
	}

	// Property accessors

	public String getCountryId() {
		return this.countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getEnabledFlag() {
		return this.enabledFlag;
	}

	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}

	public String getRem() {
		return this.rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

}