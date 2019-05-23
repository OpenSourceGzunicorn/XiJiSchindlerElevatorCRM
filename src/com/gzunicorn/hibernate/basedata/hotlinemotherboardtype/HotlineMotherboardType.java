package com.gzunicorn.hibernate.basedata.hotlinemotherboardtype;

/**
 * HotlineMotherboardType entity. @author MyEclipse Persistence Tools
 */
public class HotlineMotherboardType extends AbstractHotlineMotherboardType
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public HotlineMotherboardType() {
	}

	/** minimal constructor */
	public HotlineMotherboardType(String hmtId, String hmtName,
			String enabledFlag, String operId, String operDate) {
		super(hmtId, hmtName, enabledFlag, operId, operDate);
	}

	/** full constructor */
	public HotlineMotherboardType(String hmtId, String hmtName,
			String enabledFlag, String rem, String operId, String operDate,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(hmtId, hmtName, enabledFlag, rem, operId, operDate, r1, r2, r3,
				r4, r5, r6, r7, r8, r9, r10);
	}

}
