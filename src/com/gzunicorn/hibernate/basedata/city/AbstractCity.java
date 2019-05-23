package com.gzunicorn.hibernate.basedata.city;

import com.gzunicorn.hibernate.basedata.province.Province;

/**
 * AbstractCity entity provides the base persistence definition of the City
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCity implements java.io.Serializable {

	// Fields

	private String cityId;
	private Province province;
	private String cityName;
	private String enabledFlag;
	private String rem;

	// Constructors

	/** default constructor */
	public AbstractCity() {
	}

	/** minimal constructor */
	public AbstractCity(String cityId, Province province, String cityName,
			String enabledFlag) {
		this.cityId = cityId;
		this.province = province;
		this.cityName = cityName;
		this.enabledFlag = enabledFlag;
	}

	/** full constructor */
	public AbstractCity(String cityId, Province province, String cityName,
			String enabledFlag, String rem) {
		this.cityId = cityId;
		this.province = province;
		this.cityName = cityName;
		this.enabledFlag = enabledFlag;
		this.rem = rem;
	}

	// Property accessors

	public String getCityId() {
		return this.cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public Province getProvince() {
		return this.province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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