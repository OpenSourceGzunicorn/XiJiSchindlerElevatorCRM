package com.gzunicorn.hibernate.engcontractmanager.maintcontractquotedetail;

/**
 * MaintContractQuoteDetail entity. @author MyEclipse Persistence Tools
 */
public class MaintContractQuoteDetail extends AbstractMaintContractQuoteDetail
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public MaintContractQuoteDetail() {
	}

	/** minimal constructor */
	public MaintContractQuoteDetail(String billNo, String elevatorNo,
			String elevatorType, Integer floor, Integer stage, Integer door,
			Double high, Integer num, Double elevatorAge,
			String salesContractNo, Double standardQuote, Double finallyQuote) {
		super(billNo, elevatorNo, elevatorType, floor, stage, door, high, num,
				elevatorAge, salesContractNo, standardQuote, finallyQuote);
	}

	/** full constructor */
	public MaintContractQuoteDetail(String billNo, String elevatorNo,
			String elevatorType, Integer floor, Integer stage, Integer door,
			Double high, Integer num, Double elevatorAge,
			String salesContractNo, String projectName, String projectAddress,
			Double standardQuote, Double finallyQuote, String rem, String r1,
			String r2, String r3, String r4, String r5, Double r6, Double r7,
			Double r8, Integer r9, Integer r10,
			String contractPeriod,String elevatorParam,String signWay,
			String weight, String speed,String standardQuoteDis, Double jyMoney) {
		super(billNo, elevatorNo, elevatorType, floor, stage, door, high, num,
				elevatorAge, salesContractNo, projectName, projectAddress,
				standardQuote, finallyQuote, rem, r1, r2, r3, r4, r5, r6, r7,
				r8, r9, r10,contractPeriod,elevatorParam,signWay, weight,speed,standardQuoteDis,jyMoney);
	}

}
