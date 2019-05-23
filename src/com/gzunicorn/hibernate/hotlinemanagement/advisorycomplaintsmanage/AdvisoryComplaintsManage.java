package com.gzunicorn.hibernate.hotlinemanagement.advisorycomplaintsmanage;

/**
 * AdvisoryComplaintsManage entity. @author MyEclipse Persistence Tools
 */
public class AdvisoryComplaintsManage extends AbstractAdvisoryComplaintsManage
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public AdvisoryComplaintsManage() {
	}

	/** minimal constructor */
	public AdvisoryComplaintsManage(String processSingleNo, String submitType,
			String processType, String auditType) {
		super(processSingleNo, submitType, processType, auditType);
	}

	/** full constructor */
	public AdvisoryComplaintsManage(String processSingleNo,
			String messageSource, String receivePer, String receiveDate,
			String feedbackPer, String feedbackTel, String problemDesc,
			String submitType, String processPer, String projectName,
			String contractNo, String infoNo, String elevatorNo, String octId,
			String factoryName, String dutyDepar, String carryOutSituat,
			String processType, String issueSort1, String issueSort2,
			String issueSort3, String issueSort4, String partsName,
			String qualityIndex, String occId, String assessNo, String ocaId,
			String processDifficulty, String isClose, String completeDate,
			String processTime, String auditType, String auditOperid,
			String auditDate, String r1, String r2, String r3, String r4,
			String r5, Double r6, Integer r7, Integer r8, Double r9,
			Double r10, String processDate, String isEntrust, String dispatchType) {
		super(processSingleNo, messageSource, receivePer, receiveDate,
				feedbackPer, feedbackTel, problemDesc, submitType, processPer,
				projectName, contractNo, infoNo, elevatorNo, octId,
				factoryName, dutyDepar, carryOutSituat, processType,
				issueSort1, issueSort2, issueSort3, issueSort4, partsName,
				qualityIndex, occId, assessNo, ocaId, processDifficulty,
				isClose, completeDate, processTime, auditType, auditOperid,
				auditDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, processDate,isEntrust,dispatchType);
	}

}
