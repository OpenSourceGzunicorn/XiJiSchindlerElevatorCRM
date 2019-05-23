package com.gzunicorn.hibernate.engcontractmanager.maintcontractdelaymaster;

/**
 * MaintContractDelayMaster entity. @author MyEclipse Persistence Tools
 */
public class MaintContractDelayMaster extends AbstractMaintContractDelayMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintContractDelayMaster() {
	}

	/** minimal constructor */
	public MaintContractDelayMaster(String jnlno, Integer status, Long tokenId,
			String processName, String submitType, String operId,
			String operDate, String maintStation) {
		super(jnlno, status, tokenId, processName, submitType, operId,
				operDate, maintStation);
	}

	/** full constructor */
	public MaintContractDelayMaster(String jnlno, String billno,
			Integer status, Long tokenId, String processName,
			String submitType, String operId, String operDate,
			String maintStation, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String auditOperid, String auditStatus,
			String auditDate, String auditRem,
			String workisdisplay,String workisdisplay2,String rem) {
		super(jnlno, billno, status, tokenId, processName, submitType, operId,
				operDate, maintStation, r1, r2, r3, r4, r5, r6, r7, r8, r9,
				r10, auditOperid, auditStatus, auditDate, auditRem,
				workisdisplay,workisdisplay2,rem);
	}

}
