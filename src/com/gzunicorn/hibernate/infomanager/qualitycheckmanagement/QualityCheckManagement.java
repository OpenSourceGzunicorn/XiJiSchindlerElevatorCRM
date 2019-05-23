package com.gzunicorn.hibernate.infomanager.qualitycheckmanagement;

/**
 * QualityCheckManagement entity. @author MyEclipse Persistence Tools
 */
public class QualityCheckManagement extends AbstractQualityCheckManagement
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public QualityCheckManagement() {
	}

	/** minimal constructor */
	public QualityCheckManagement(String billno, String maintDivision,
			String maintStation, String maintPersonnel, String projectName,
			String maintContractNo, String elevatorNo, String submitType,
			String operId, String operDate) {
		super(billno, maintDivision, maintStation, maintPersonnel, projectName,
				maintContractNo, elevatorNo, submitType, operId, operDate);
	}

	/** full constructor */
	public QualityCheckManagement(String billno, String maintDivision,
			String maintStation, String maintPersonnel, String projectName,
			String maintContractNo, String elevatorNo, String checksPeople,
			String checksDateTime, Double totalPoints, String scoreLevel,
			String submitType, String supervOpinion, String partMinisters,
			String partMinistersRem, String remDate, String operId,
			String operDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String processStatus, String salesContractNo,
			String customerSignature, Integer status, Long tokenId,
			String processName, String superviseId, String supervisePhone,
			String assessRem, Double deductMoney, String elevatorType,
			String personnelPhone,String customerImage) {
		super(billno, maintDivision, maintStation, maintPersonnel, projectName,
				maintContractNo, elevatorNo, checksPeople, checksDateTime,
				totalPoints, scoreLevel, submitType, supervOpinion,
				partMinisters, partMinistersRem, remDate, operId, operDate, r1,
				r2, r3, r4, r5, r6, r7, r8, r9, r10, processStatus,
				salesContractNo, customerSignature, status, tokenId,
				processName, superviseId, supervisePhone, assessRem,
				deductMoney, elevatorType, personnelPhone,customerImage);
	}

}
