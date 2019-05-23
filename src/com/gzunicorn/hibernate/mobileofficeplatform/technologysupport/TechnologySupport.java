package com.gzunicorn.hibernate.mobileofficeplatform.technologysupport;

/**
 * TechnologySupport entity. @author MyEclipse Persistence Tools
 */
public class TechnologySupport extends AbstractTechnologySupport implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TechnologySupport() {
	}

	/** minimal constructor */
	public TechnologySupport(String billno, String singleNo,
			String maintDivision, String maintStation, String hmtId,
			String faultCode, String faultStatus, String assignUser,
			String assignUserTel, String operDate) {
		super(billno, singleNo, maintDivision, maintStation, hmtId, faultCode,
				faultStatus, assignUser, assignUserTel, operDate);
	}

	/** full constructor */
	public TechnologySupport(String billno, String singleNo,String elevatorNo,
			String maintDivision, String maintStation, String hmtId,
			String faultCode, String faultStatus, String msprocessPeople,
			String msprocessDate, String msprocessRem, String msisResolve,
			String mmprocessPeople, String mmprocessDate, String mmprocessRem,
			String mmisResolve, String tsprocessPeople, String tsprocessDate,
			String tsprocessRem, String assignUser, String assignUserTel,
			String operDate, String proStatus,String gzImage, String r1, String r2, String r3,
			String r4, String r5, Double r6, Integer r7, Integer r8, Double r9,
			Double r10) {
		super(billno, singleNo,elevatorNo, maintDivision, maintStation, hmtId, faultCode,
				faultStatus, msprocessPeople, msprocessDate, msprocessRem,
				msisResolve, mmprocessPeople, mmprocessDate, mmprocessRem,
				mmisResolve, tsprocessPeople, tsprocessDate, tsprocessRem,
				assignUser, assignUserTel, operDate, proStatus,gzImage, r1, r2, r3, r4,
				r5, r6, r7, r8, r9, r10);
	}

}
