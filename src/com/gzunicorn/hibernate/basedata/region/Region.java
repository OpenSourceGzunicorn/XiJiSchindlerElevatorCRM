package com.gzunicorn.hibernate.basedata.region;

import com.gzunicorn.hibernate.basedata.city.City;

/**
 * Region entity. @author MyEclipse Persistence Tools
 */
public class Region extends AbstractRegion implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Region() {
	}

	/** minimal constructor */
	public Region(String regionId, City city, String regionName,
			String enabledFlag) {
		super(regionId, city, regionName, enabledFlag);
	}

	/** full constructor */
	public Region(String regionId, City city, String regionName,
			String enabledFlag, String rem) {
		super(regionId, city, regionName, enabledFlag, rem);
	}

}
