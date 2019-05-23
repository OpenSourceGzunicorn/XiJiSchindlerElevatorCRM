package com.gzunicorn.hibernate.contractpayment.contractparagraphmanage;

/**
 * ContractParagraphManage entity. @author MyEclipse Persistence Tools
 */
public class ContractParagraphManage extends AbstractContractParagraphManage
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ContractParagraphManage() {
	}

	/** minimal constructor */
	public ContractParagraphManage(String jnlNo, String arfJnlNo,
			String billNo, String contractNo, String contractType,
			String companyId, String paragraphNo, Double paragraphMoney,
			String paragraphDate, String maintDivision, String operId,
			String operDate, String submitType) {
		super(jnlNo, arfJnlNo, billNo, contractNo, contractType, companyId,
				paragraphNo, paragraphMoney, paragraphDate, maintDivision,
				operId, operDate, submitType);
	}

	/** full constructor */
	public ContractParagraphManage(String jnlNo, String arfJnlNo,
			String billNo, String contractNo, String contractType,
			String companyId, String paragraphNo, Double paragraphMoney,
			String paragraphDate, String maintDivision, String operId,
			String operDate, String rem, String auditOperid,
			String auditStatus, String auditDate, String auditRem,
			String submitType, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String maintStation) {
		super(jnlNo, arfJnlNo, billNo, contractNo, contractType, companyId,
				paragraphNo, paragraphMoney, paragraphDate, maintDivision,
				operId, operDate, rem, auditOperid, auditStatus, auditDate,
				auditRem, submitType, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
				maintStation);
	}

}
