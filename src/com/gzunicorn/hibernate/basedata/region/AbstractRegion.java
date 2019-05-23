package com.gzunicorn.hibernate.basedata.region;

import com.gzunicorn.hibernate.basedata.city.City;

/**
 * AbstractRegion entity provides the base persistence definition of the Region
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractRegion implements java.io.Serializable {

	// Fields

	private String regionId;
	private City city;
	private String regionName;
	private String enabledFlag;
	private String rem;

	// Constructors

	/** default constructor */
	public AbstractRegion() {
	}

	/** minimal constructor */
	public AbstractRegion(String regionId, City city, String regionName,
			String enabledFlag) {
		this.regionId = regionId;
		this.city = city;
		this.regionName = regionName;
		this.enabledFlag = enabledFlag;
	}

	/** full constructor */
	public AbstractRegion(String regionId, City city, String regionName,
			String enabledFlag, String rem) {
		this.regionId = regionId;
		this.city = city;
		this.regionName = regionName;
		this.enabledFlag = enabledFlag;
		this.rem = rem;
	}

	// Property accessors

	public String getRegionId() {
		return this.regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public City getCity() {
		return this.city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getRegionName() {
		return this.regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
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