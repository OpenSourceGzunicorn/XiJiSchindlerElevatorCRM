package com.gzunicorn.hibernate.proshr;

import java.util.Set;

/**
 * ProcessSetAuditMaster entity. @author MyEclipse Persistence Tools
 */
public class ProcessSetAuditMaster extends AbstractProcessSetAuditMaster
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ProcessSetAuditMaster() {
	}

	/** minimal constructor */
	public ProcessSetAuditMaster(String processDefineId, String nodeid,
			String nodename) {
		super(processDefineId, nodeid, nodename);
	}

	/** full constructor */
	public ProcessSetAuditMaster(String processDefineId, String nodeid,
			String nodename, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10) {
		super(processDefineId, nodeid, nodename, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10);
	}

}
