package com.gzunicorn.hibernate.basedata.toolreceive;

/**
 * ToolReceive entity. @author MyEclipse Persistence Tools
 */
public class ToolReceive extends AbstractToolReceive implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ToolReceive() {
	}

	/** minimal constructor */
	public ToolReceive(Integer numNo, String billno, String toolId, String toolName,
			String toolParam, Double toolnum, String operName,
			String operDate) {
		super(numNo, billno, toolId, toolName, toolParam, toolnum, operName,
				operDate);
	}

	/** full constructor */
	public ToolReceive(Integer numNo, String billno, String toolId, String toolName,
			String toolParam, Double toolnum, String operName,
			String operDate, String isCharge, String isLiquidation, String rem, 
			String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(numNo, billno, toolId, toolName, toolParam, toolnum, operName,
				operDate, isCharge, isLiquidation, rem,
				r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
