package com.gzunicorn.hibernate.basedata.certificateexam;

/**
 * CertificateExam entity. @author MyEclipse Persistence Tools
 */
public class CertificateExam extends AbstractCertificateExam implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public CertificateExam() {
	}

	/** minimal constructor */
	public CertificateExam(Integer numNo, String billno, String certificateNo,
			String certificateName, String certificateProperty,
			String startDate, String endDate) {
		super(numNo, billno, certificateNo, certificateName,
				certificateProperty, startDate, endDate);
	}

	/** full constructor */
	public CertificateExam(Integer numNo, String billno, String certificateNo,
			String certificateName, String certificateProperty,
			String startDate, String endDate, String r1, String r2, String r3,
			String r4, String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10,
			String issuingAuthority,String isExpense,String isCharging,String certificateExt,String rem) {
		super(numNo, billno, certificateNo, certificateName,
				certificateProperty, startDate, endDate, r1, r2, r3, r4, r5,
				r6, r7, r8, r9, r10,
				issuingAuthority,isExpense,isCharging,certificateExt,rem);
	}

}
