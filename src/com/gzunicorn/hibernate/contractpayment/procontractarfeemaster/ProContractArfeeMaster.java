package com.gzunicorn.hibernate.contractpayment.procontractarfeemaster;

/**
 * ProContractArfeeMaster entity. @author MyEclipse Persistence Tools
 */
public class ProContractArfeeMaster extends AbstractProContractArfeeMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ProContractArfeeMaster() {
	}

	/** minimal constructor */
	public ProContractArfeeMaster(String jnlNo, String billNo,
			String contractNo, String contractType, String companyId,
			String recName, Double preMoney, String preDate,
			String maintDivision, String operId, String operDate,
			String submitType) {
		super(jnlNo, billNo, contractNo, contractType, companyId, recName,
				preMoney, preDate, maintDivision, operId, operDate, submitType);
	}

	/** full constructor */
	public ProContractArfeeMaster(String jnlNo, String billNo,
			String contractNo, String contractType, String companyId,
			String recName, Double preMoney, String preDate,
			String maintDivision, String operId, String operDate, String rem,
			String auditOperid, String auditStatus, String auditDate,
			String auditRem, String submitType, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10, String maintStation, String maintScope,
			String warnRem,String modifyId, String modifyDate, String oldPreDate,Double oldPreMoney) {
		super(jnlNo, billNo, contractNo, contractType, companyId, recName,
				preMoney, preDate, maintDivision, operId, operDate, rem,
				auditOperid, auditStatus, auditDate, auditRem, submitType, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10, maintStation, maintScope,
				warnRem, modifyId, modifyDate, oldPreDate, oldPreMoney);
	}

}
