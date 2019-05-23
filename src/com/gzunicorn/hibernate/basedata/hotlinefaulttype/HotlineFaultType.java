package com.gzunicorn.hibernate.basedata.hotlinefaulttype;

/**
 * HotlineFaultType entity. @author MyEclipse Persistence Tools
 */
public class HotlineFaultType extends AbstractHotlineFaultType implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public HotlineFaultType() {
	}

	/** minimal constructor */
	public HotlineFaultType(String hftId, String hftDesc, String enabledFlag,
			String operId, String operDate) {
		super(hftId, hftDesc, enabledFlag, operId, operDate);
	}

	/** full constructor */
	public HotlineFaultType(String hftId, String hftDesc, String enabledFlag,
			String rem, String operId, String operDate, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10) {
		super(hftId, hftDesc, enabledFlag, rem, operId, operDate, r1, r2, r3,
				r4, r5, r6, r7, r8, r9, r10);
	}

}
