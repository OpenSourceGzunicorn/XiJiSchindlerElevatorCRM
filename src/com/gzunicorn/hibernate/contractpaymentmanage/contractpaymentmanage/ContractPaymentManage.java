package com.gzunicorn.hibernate.contractpaymentmanage.contractpaymentmanage;

/**
 * ContractPaymentManage entity. @author MyEclipse Persistence Tools
 */
public class ContractPaymentManage extends AbstractContractPaymentManage
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ContractPaymentManage() {
	}

	/** minimal constructor */
	public ContractPaymentManage(String jnlNo, String ctJnlNo, String billNo,
			String entrustContractNo, String companyId, String paragraphNo,
			Double paragraphMoney, String paragraphDate, String maintDivision,
			String operId, String operDate, String submitType, Integer status,
			Long tokenId, String processName) {
		super(jnlNo, ctJnlNo, billNo, entrustContractNo, companyId,
				paragraphNo, paragraphMoney, paragraphDate, maintDivision,
				operId, operDate, submitType, status, tokenId, processName);
	}

	/** full constructor */
	public ContractPaymentManage(String jnlNo, String ctJnlNo, String billNo,
			String entrustContractNo, String companyId, String paragraphNo,
			Double paragraphMoney, String paragraphDate, String maintDivision,
			String operId, String operDate, String rem, String submitType,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10, Integer status,
			Long tokenId, String processName, String bydAuditDate,
			String bydAuditEvaluate, String bydAuditRem, String hfAuditDate,
			Integer hfAuditNum, Integer hfAuditNum2, String hfAuditRem,
			String rxAuditDate, Integer rxAuditNum, Integer rxAuditNum2,
			String rxAuditRem, String jjthAuditDate, String jjthAuditEvaluate,
			String jjthAuditRem, String fbzAuditDate, String fbzAuditEvaluate,
			Double debitMoney, String fbzAuditRem, String zbzAuditDate,
			String zbzAuditRem) {
		super(jnlNo, ctJnlNo, billNo, entrustContractNo, companyId,
				paragraphNo, paragraphMoney, paragraphDate, maintDivision,
				operId, operDate, rem, submitType, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10, status, tokenId, processName, bydAuditDate,
				bydAuditEvaluate, bydAuditRem, hfAuditDate, hfAuditNum,
				hfAuditNum2, hfAuditRem, rxAuditDate, rxAuditNum, rxAuditNum2,
				rxAuditRem, jjthAuditDate, jjthAuditEvaluate, jjthAuditRem,
				fbzAuditDate, fbzAuditEvaluate, debitMoney, fbzAuditRem,
				zbzAuditDate, zbzAuditRem);
	}

}
