package com.gzunicorn.hibernate.basedata.traininghistory;

/**
 * TrainingHistory entity. @author MyEclipse Persistence Tools
 */
public class TrainingHistory extends AbstractTrainingHistory implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public TrainingHistory() {
	}

	/** minimal constructor */
	public TrainingHistory(Integer numNo, String billno, String straDate,
			String etraDate, String traCourse, Double lesson,
			Double theoreticalResults, Double practicalResults,
			Double perforResults, String traAssess, Double totalScore,
			String rating) {
		super(numNo, billno, straDate, etraDate, traCourse, lesson,
				theoreticalResults, practicalResults, perforResults, traAssess,
				totalScore, rating);
	}

	/** full constructor */
	public TrainingHistory(Integer numNo, String billno, String straDate,
			String etraDate, String traCourse, Double lesson,
			Double theoreticalResults, Double practicalResults,
			Double perforResults, String traAssess, Double totalScore,
			String rating, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		super(numNo, billno, straDate, etraDate, traCourse, lesson,
				theoreticalResults, practicalResults, perforResults, traAssess,
				totalScore, rating, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
	}

}
