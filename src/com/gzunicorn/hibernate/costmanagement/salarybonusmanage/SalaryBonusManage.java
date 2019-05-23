package com.gzunicorn.hibernate.costmanagement.salarybonusmanage;

import java.util.Set;

/**
 * SalaryBonusManage entity. @author MyEclipse Persistence Tools
 */
public class SalaryBonusManage extends AbstractSalaryBonusManage implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public SalaryBonusManage() {
	}

	/** minimal constructor */
	public SalaryBonusManage(String billno, String maintDivision,
			String maintStation, String costsDate, Double totalAmount,
			String operId, String operDate) {
		super(billno, maintDivision, maintStation, costsDate, totalAmount,
				operId, operDate);
	}

	/** full constructor */
	public SalaryBonusManage(String billno, String maintDivision,
			String maintStation, String costsDate, Double totalAmount,
			String operId, String operDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, Set salaryBonusDetails) {
		super(billno, maintDivision, maintStation, costsDate, totalAmount,
				operId, operDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				salaryBonusDetails);
	}

}
