package com.gzunicorn.hibernate.proshr;

/**
 * ProcessSetBranch entity. @author MyEclipse Persistence Tools
 */
public class ProcessSetBranch extends AbstractProcessSetBranch implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ProcessSetBranch() {
	}

	/** minimal constructor */
	public ProcessSetBranch(String processDefine, String processDefineId,
			String comId) {
		super(processDefine, processDefineId, comId);
	}

	/** full constructor */
	public ProcessSetBranch(String processDefine, String processDefineId,
			String comId, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(processDefine, processDefineId, comId, r1, r2, r3, r4, r5, r6,
				r7, r8, r9, r10);
	}

}
