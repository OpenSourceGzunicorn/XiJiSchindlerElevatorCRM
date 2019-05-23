package com.gzunicorn.hibernate.basedata.province;

import com.gzunicorn.hibernate.basedata.country.Country;

/**
 * AbstractProvince entity provides the base persistence definition of the
 * Province entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractProvince implements java.io.Serializable {

	// Fields

	private String provinceId;
	private Country country;
	private String provinceName;
	private String enabledFlag;
	private String rem;

	// Constructors

	/** default constructor */
	public AbstractProvince() {
	}

	/** minimal constructor */
	public AbstractProvince(String provinceId, Country country,
			String provinceName, String enabledFlag) {
		this.provinceId = provinceId;
		this.country = country;
		this.provinceName = provinceName;
		this.enabledFlag = enabledFlag;
	}

	/** full constructor */
	public AbstractProvince(String provinceId, Country country,
			String provinceName, String enabledFlag, String rem) {
		this.provinceId = provinceId;
		this.country = country;
		this.provinceName = provinceName;
		this.enabledFlag = enabledFlag;
		this.rem = rem;
	}

	// Property accessors

	public String getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
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