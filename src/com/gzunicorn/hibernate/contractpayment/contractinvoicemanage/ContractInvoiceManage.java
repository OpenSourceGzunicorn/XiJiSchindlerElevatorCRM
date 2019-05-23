package com.gzunicorn.hibernate.contractpayment.contractinvoicemanage;

import com.gzunicorn.hibernate.contractpayment.procontractarfeemaster.ProContractArfeeMaster;

/**
 * ContractInvoiceManage entity. @author MyEclipse Persistence Tools
 */
public class ContractInvoiceManage extends AbstractContractInvoiceManage
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ContractInvoiceManage() {
	}

	/** minimal constructor */
	public ContractInvoiceManage(String jnlNo,
			String arfJnlNo, String billNo,
			String contractNo, String contractType, String companyId,
			String invoiceNo, String invoiceType, Double invoiceMoney,
			String invoiceDate, String maintDivision, String operId,
			String operDate, String submitType) {
		super(jnlNo, arfJnlNo, billNo, contractNo, contractType,
				companyId, invoiceNo, invoiceType, invoiceMoney, invoiceDate,
				maintDivision, operId, operDate, submitType);
	}

	/** full constructor */
	public ContractInvoiceManage(String jnlNo,
			String arfJnlNo, String billNo,
			String contractNo, String contractType, String companyId,
			String invoiceNo, String invoiceType, Double invoiceMoney,
			String invoiceDate, String maintDivision, String operId,
			String operDate, String rem, String informDate, String auditOperid,
			String auditStatus, String auditDate, String auditRem,
			String submitType, String istbp, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String maintStation, String invoiceName,
			String maintScope, String auditOperid2,
			String auditStatus2, String auditDate2, String auditRem2,
			String auditOperid3,String auditStatus3, String auditDate3, String auditRem3,
			String auditOperid4,String auditStatus4, String auditDate4, String auditRem4,
			String workisdisplay) {
		super(jnlNo, arfJnlNo, billNo, contractNo, contractType,
				companyId, invoiceNo, invoiceType, invoiceMoney, invoiceDate,
				maintDivision, operId, operDate, rem, informDate, auditOperid,
				auditStatus, auditDate, auditRem, submitType, istbp, r1, r2,
				r3, r4, r5, r6, r7, r8, r9, r10, maintStation, invoiceName,
				maintScope, auditOperid2,
				auditStatus2, auditDate2, auditRem2, 
				auditOperid3,auditStatus3, auditDate3, auditRem3,
				auditOperid4,auditStatus4, auditDate4, auditRem4,
				workisdisplay);
	}

}
