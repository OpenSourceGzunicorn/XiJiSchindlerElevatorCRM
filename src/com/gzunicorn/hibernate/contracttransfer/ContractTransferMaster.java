package com.gzunicorn.hibernate.contracttransfer;

/**
 * ContractTransferMaster entity. @author MyEclipse Persistence Tools
 */
public class ContractTransferMaster extends AbstractContractTransferMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ContractTransferMaster() {
	}

	/** minimal constructor */
	public ContractTransferMaster(String billNo, String companyId,
			String maintContractNo, String salesContractNo, String elevatorNo,
			String maintDivision, String maintStation, String contractSdate,
			String contractEdate, String submitType, String operId,
			String operDate) {
		super(billNo, companyId, maintContractNo, salesContractNo, elevatorNo,
				maintDivision, maintStation, contractSdate, contractEdate,
				submitType, operId, operDate);
	}

	/** full constructor */
	public ContractTransferMaster(String billNo, String companyId,
			String maintContractNo, String salesContractNo, String elevatorNo,
			String maintDivision, String maintStation, String contractSdate,
			String contractEdate, String submitType, String operId,
			String operDate, String transfeSubmitType, String transfeId,
			String transfeDate, String transferRem, String auditOperid,
			String auditStatus, String auditDate, String auditRem,
			String transferComplete, String workisdisplay, String r1,
			String r2, String r3, String r4, String r5, Double r6, Double r7,
			Double r8, Integer r9, Integer r10,
			String isTrans, String wbgTransfeId, String auditOperid2, 
			String auditStatus2,String auditDate2, String auditRem2, String isTransId, String isTransDate) {
		super(billNo, companyId, maintContractNo, salesContractNo, elevatorNo,
				maintDivision, maintStation, contractSdate, contractEdate,
				submitType, operId, operDate, transfeSubmitType, transfeId,
				transfeDate, transferRem, auditOperid, auditStatus, auditDate,
				auditRem, transferComplete, workisdisplay, r1, r2, r3, r4, r5,
				r6, r7, r8, r9, r10,
				isTrans, wbgTransfeId, auditOperid2, 
				auditStatus2, auditDate2, auditRem2, isTransId, isTransDate);
	}

}
