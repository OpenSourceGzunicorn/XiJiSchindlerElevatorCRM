package com.gzunicorn.hibernate.basedata.city;

import com.gzunicorn.hibernate.basedata.province.Province;

/**
 * City entity. @author MyEclipse Persistence Tools
 */
public class City extends AbstractCity implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public City() {
	}

	/** minimal constructor */
	public City(String cityId, Province province, String cityName,
			String enabledFlag) {
		super(cityId, province, cityName, enabledFlag);
	}

	/** full constructor */
	public City(String cityId, Province province, String cityName,
			String enabledFlag, String rem) {
		super(cityId, province, cityName, enabledFlag, rem);
	}

}
