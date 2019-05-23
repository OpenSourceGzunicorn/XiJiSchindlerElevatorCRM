package com.gzunicorn.hibernate.engcontractmanager.outsourcecontractdetail;

import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractmaster.OutsourceContractMaster;

/**
 * OutsourceContractDetail entity. @author MyEclipse Persistence Tools
 */
public class OutsourceContractDetail extends AbstractOutsourceContractDetail
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public OutsourceContractDetail() {
	}

	/** minimal constructor */
	public OutsourceContractDetail(
			OutsourceContractMaster outsourceContractMaster, Integer wgRowid,
			String elevatorNo, String salesContractNo, String contents) {
		super(outsourceContractMaster, wgRowid, elevatorNo, salesContractNo,
				contents);
	}

	/** full constructor */
	public OutsourceContractDetail(
			OutsourceContractMaster outsourceContractMaster, Integer wgRowid,
			String elevatorNo, String salesContractNo, String projectName,
			String contents, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(outsourceContractMaster, wgRowid, elevatorNo, salesContractNo,
				projectName, contents, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
