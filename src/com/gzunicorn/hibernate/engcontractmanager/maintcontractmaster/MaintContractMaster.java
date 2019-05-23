package com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster;

/**
 * MaintContractMaster entity. @author MyEclipse Persistence Tools
 */
public class MaintContractMaster extends AbstractMaintContractMaster implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintContractMaster() {
	}

	/** minimal constructor */
	public MaintContractMaster(String billNo, String companyId,
			String companyId2, String maintContractNo, String contractNatureOf,
			String mainMode, String contractPeriod, String contractSdate,
			String contractEdate, Double contractTotal, String attn,
			String maintDivision, String maintStation, String operId,
			String operDate, String contractStatus, String submitType) {
		super(billNo, companyId, companyId2, maintContractNo, contractNatureOf,
				mainMode, contractPeriod, contractSdate, contractEdate,
				contractTotal, attn, maintDivision, maintStation, operId,
				operDate, contractStatus, submitType);
	}

	/** full constructor */
	public MaintContractMaster(String billNo, String quoteBillNo,
			String companyId, String companyId2, String maintContractNo,
			String contractNatureOf, String mainMode, String contractPeriod,
			String contractSdate, String contractEdate, Double contractTotal,
			Double otherFee, String contractTerms, String paymentMethod,
			String attn, String maintDivision, String maintStation,
			String auditOperid, String auditStatus, String auditDate,
			String auditRem, String operId, String operDate, String taskUserId,
			String taskSubFlag, String taskSubDate, String taskRem,
			String surrenderUser, String surrenderDate, String surrenderRem,
			String contractStatus, String submitType, String r1, String r2,
			String r3, String r4, String r5, Double r6, Double r7, Double r8,
			Integer r9, Integer r10, String historyBillNo,
			String warningStatus, String histContractNo,String histContractStatus,
			String workisdisplay,String workisdisplay2,
			String paymentMethodRem,String contractContentRem,String rem,
			String modifyId,String modifyDate, Double oldContractTotal,Double oldOtherFee,
			String quoteSignWay,String xqType,String customizeRem) {
		super(billNo, quoteBillNo, companyId, companyId2, maintContractNo,
				contractNatureOf, mainMode, contractPeriod, contractSdate,
				contractEdate, contractTotal, otherFee, contractTerms,
				paymentMethod, attn, maintDivision, maintStation, auditOperid,
				auditStatus, auditDate, auditRem, operId, operDate, taskUserId,
				taskSubFlag, taskSubDate, taskRem, surrenderUser,
				surrenderDate, surrenderRem, contractStatus, submitType, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10, historyBillNo,
				warningStatus, histContractNo,histContractStatus,workisdisplay,workisdisplay2,
				paymentMethodRem,contractContentRem,rem, modifyId, modifyDate, oldContractTotal, oldOtherFee,
				quoteSignWay,xqType,customizeRem);
	}

}
