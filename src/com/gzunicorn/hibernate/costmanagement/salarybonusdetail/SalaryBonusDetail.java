package com.gzunicorn.hibernate.costmanagement.salarybonusdetail;

import com.gzunicorn.hibernate.costmanagement.salarybonusmanage.SalaryBonusManage;

/**
 * SalaryBonusDetail entity. @author MyEclipse Persistence Tools
 */
public class SalaryBonusDetail extends AbstractSalaryBonusDetail implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public SalaryBonusDetail() {
	}

	/** minimal constructor */
	public SalaryBonusDetail(String maintContractNo, String busType,
			String projectName, Integer num, Double money) {
		super(maintContractNo, busType, projectName, num, money);
	}

	/** full constructor */
	public SalaryBonusDetail(SalaryBonusManage salaryBonusManage,
			String maintContractNo, String busType, String projectName,
			Integer num, Double money, String rem, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10) {
		super(salaryBonusManage, maintContractNo, busType, projectName, num,
				money, rem, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
