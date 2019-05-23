package com.gzunicorn.hibernate.engcontractmanager.servicingcontractquotematerialsdetail;

import com.gzunicorn.hibernate.engcontractmanager.servicingcontractquotemaster.ServicingContractQuoteMaster;

/**
 * ServicingContractQuoteMaterialsDetail entity. @author MyEclipse Persistence
 * Tools
 */
public class ServicingContractQuoteMaterialsDetail extends
		AbstractServicingContractQuoteMaterialsDetail implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ServicingContractQuoteMaterialsDetail() {
	}

	/** minimal constructor */
	public ServicingContractQuoteMaterialsDetail(String materialName,
			String materialsStandard, Double quantity, String unit,
			Double unitPrice, Double price, Double finalPrice) {
		super(materialName, materialsStandard, quantity, unit, unitPrice,
				price, finalPrice);
	}

	/** full constructor */
	public ServicingContractQuoteMaterialsDetail(
			ServicingContractQuoteMaster servicingContractQuoteMaster,
			String materialName, String materialsStandard, Double quantity,
			String unit, Double unitPrice, Double price, Double finalPrice,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		super(servicingContractQuoteMaster, materialName, materialsStandard,
				quantity, unit, unitPrice, price, finalPrice, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10);
	}

}
