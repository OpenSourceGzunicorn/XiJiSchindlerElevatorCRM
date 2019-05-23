package com.gzunicorn.hibernate.basedata.province;

import com.gzunicorn.hibernate.basedata.country.Country;

/**
 * Province entity. @author MyEclipse Persistence Tools
 */
public class Province extends AbstractProvince implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Province() {
	}

	/** minimal constructor */
	public Province(String provinceId, Country country, String provinceName,
			String enabledFlag) {
		super(provinceId, country, provinceName, enabledFlag);
	}

	/** full constructor */
	public Province(String provinceId, Country country, String provinceName,
			String enabledFlag, String rem) {
		super(provinceId, country, provinceName, enabledFlag, rem);
	}

}
