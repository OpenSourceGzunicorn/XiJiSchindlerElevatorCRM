package com.gzunicorn.hibernate.basedata.country;

/**
 * Country entity. @author MyEclipse Persistence Tools
 */
public class Country extends AbstractCountry implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Country() {
	}

	/** minimal constructor */
	public Country(String countryId, String countryName, String enabledFlag) {
		super(countryId, countryName, enabledFlag);
	}

	/** full constructor */
	public Country(String countryId, String countryName, String enabledFlag,
			String rem) {
		super(countryId, countryName, enabledFlag, rem);
	}

}
