package com.gzunicorn.hibernate.contractpaymentmanage.contractticketmanage;

import java.util.Set;

/**
 * ContractTicketManage entity. @author MyEclipse Persistence Tools
 */
public class ContractTicketManage extends AbstractContractTicketManage
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ContractTicketManage() {
	}

	/** minimal constructor */
	public ContractTicketManage(String jnlNo, String billNo,
			String entrustContractNo, String companyId, String invoiceNo,
			String invoiceType, Double invoiceMoney, String invoiceDate,
			String maintDivision, String operId, String operDate,
			String submitType) {
		super(jnlNo, billNo, entrustContractNo, companyId, invoiceNo,
				invoiceType, invoiceMoney, invoiceDate, maintDivision, operId,
				operDate, submitType);
	}

	/** full constructor */
	public ContractTicketManage(String jnlNo, String billNo,
			String entrustContractNo, String companyId, String invoiceNo,
			String invoiceType, Double invoiceMoney, String invoiceDate,
			String maintDivision, String operId, String operDate, String rem,
			String informDate, String auditOperid, String auditStatus,
			String auditDate, String auditRem, String submitType, String r1,
			String r2, String r3, String r4, String r5, Double r6, Double r7,
			Double r8, Integer r9, Integer r10, Set contractPaymentManages) {
		super(jnlNo, billNo, entrustContractNo, companyId, invoiceNo,
				invoiceType, invoiceMoney, invoiceDate, maintDivision, operId,
				operDate, rem, informDate, auditOperid, auditStatus, auditDate,
				auditRem, submitType, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				contractPaymentManages);
	}

}
