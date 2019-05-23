package com.gzunicorn.hibernate.costmanagement.projectfittingcosts;

import com.gzunicorn.hibernate.costmanagement.freefittingcostsmanage.FreeFittingCostsManage;

/**
 * ProjectFittingCosts entity. @author MyEclipse Persistence Tools
 */
public class ProjectFittingCosts extends AbstractProjectFittingCosts implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ProjectFittingCosts() {
	}

	/** minimal constructor */
	public ProjectFittingCosts(FreeFittingCostsManage freeFittingCostsManage,
			String maintContractNo, String busType, String projectName) {
		super(freeFittingCostsManage, maintContractNo, busType, projectName);
	}

	/** full constructor */
	public ProjectFittingCosts(FreeFittingCostsManage freeFittingCostsManage,
			String maintContractNo, String busType, String projectName,
			String fittingName, Integer fittingNum, Double costs, String rem,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(freeFittingCostsManage, maintContractNo, busType, projectName,
				fittingName, fittingNum, costs, rem, r1, r2, r3, r4, r5, r6,
				r7, r8, r9, r10);
	}

}
