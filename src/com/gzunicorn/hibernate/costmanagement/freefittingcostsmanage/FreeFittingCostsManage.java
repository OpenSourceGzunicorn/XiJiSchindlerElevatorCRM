package com.gzunicorn.hibernate.costmanagement.freefittingcostsmanage;

import java.util.Set;

/**
 * FreeFittingCostsManage entity. @author MyEclipse Persistence Tools
 */
public class FreeFittingCostsManage extends AbstractFreeFittingCostsManage
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public FreeFittingCostsManage() {
	}

	/** minimal constructor */
	public FreeFittingCostsManage(String billno, String maintDivision,
			String maintStation, String costsDate, Double fittingTotal,
			String operId, String operDate) {
		super(billno, maintDivision, maintStation, costsDate, fittingTotal,
				operId, operDate);
	}

	/** full constructor */
	public FreeFittingCostsManage(String billno, String maintDivision,
			String maintStation, String costsDate, Double fittingTotal,
			String operId, String operDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, Set projectFittingCostses) {
		super(billno, maintDivision, maintStation, costsDate, fittingTotal,
				operId, operDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				projectFittingCostses);
	}

}
