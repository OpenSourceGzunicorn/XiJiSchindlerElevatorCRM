package com.gzunicorn.hibernate.basedata.traininghistory;

/**
 * AbstractTrainingHistory entity provides the base persistence definition of
 * the TrainingHistory entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTrainingHistory implements java.io.Serializable {

	// Fields

	private Integer numNo;
	private String billno;
	private String straDate;
	private String etraDate;
	private String traCourse;
	private Double lesson;
	private Double theoreticalResults;
	private Double practicalResults;
	private Double perforResults;
	private String traAssess;
	private Double totalScore;
	private String rating;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private Double r6;
	private Double r7;
	private Double r8;
	private Integer r9;
	private Integer r10;

	// Constructors

	/** default constructor */
	public AbstractTrainingHistory() {
	}

	/** minimal constructor */
	public AbstractTrainingHistory(Integer numNo, String billno,
			String straDate, String etraDate, String traCourse, Double lesson,
			Double theoreticalResults, Double practicalResults,
			Double perforResults, String traAssess, Double totalScore,
			String rating) {
		this.numNo = numNo;
		this.billno = billno;
		this.straDate = straDate;
		this.etraDate = etraDate;
		this.traCourse = traCourse;
		this.lesson = lesson;
		this.theoreticalResults = theoreticalResults;
		this.practicalResults = practicalResults;
		this.perforResults = perforResults;
		this.traAssess = traAssess;
		this.totalScore = totalScore;
		this.rating = rating;
	}

	/** full constructor */
	public AbstractTrainingHistory(Integer numNo, String billno,
			String straDate, String etraDate, String traCourse, Double lesson,
			Double theoreticalResults, Double practicalResults,
			Double perforResults, String traAssess, Double totalScore,
			String rating, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10) {
		this.numNo = numNo;
		this.billno = billno;
		this.straDate = straDate;
		this.etraDate = etraDate;
		this.traCourse = traCourse;
		this.lesson = lesson;
		this.theoreticalResults = theoreticalResults;
		this.practicalResults = practicalResults;
		this.perforResults = perforResults;
		this.traAssess = traAssess;
		this.totalScore = totalScore;
		this.rating = rating;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.r4 = r4;
		this.r5 = r5;
		this.r6 = r6;
		this.r7 = r7;
		this.r8 = r8;
		this.r9 = r9;
		this.r10 = r10;
	}

	// Property accessors

	public Integer getNumNo() {
		return this.numNo;
	}

	public void setNumNo(Integer numNo) {
		this.numNo = numNo;
	}

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getStraDate() {
		return this.straDate;
	}

	public void setStraDate(String straDate) {
		this.straDate = straDate;
	}

	public String getEtraDate() {
		return this.etraDate;
	}

	public void setEtraDate(String etraDate) {
		this.etraDate = etraDate;
	}

	public String getTraCourse() {
		return this.traCourse;
	}

	public void setTraCourse(String traCourse) {
		this.traCourse = traCourse;
	}

	public Double getLesson() {
		return this.lesson;
	}

	public void setLesson(Double lesson) {
		this.lesson = lesson;
	}

	public Double getTheoreticalResults() {
		return this.theoreticalResults;
	}

	public void setTheoreticalResults(Double theoreticalResults) {
		this.theoreticalResults = theoreticalResults;
	}

	public Double getPracticalResults() {
		return this.practicalResults;
	}

	public void setPracticalResults(Double practicalResults) {
		this.practicalResults = practicalResults;
	}

	public Double getPerforResults() {
		return this.perforResults;
	}

	public void setPerforResults(Double perforResults) {
		this.perforResults = perforResults;
	}

	public String getTraAssess() {
		return this.traAssess;
	}

	public void setTraAssess(String traAssess) {
		this.traAssess = traAssess;
	}

	public Double getTotalScore() {
		return this.totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public String getRating() {
		return this.rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getR1() {
		return this.r1;
	}

	public void setR1(String r1) {
		this.r1 = r1;
	}

	public String getR2() {
		return this.r2;
	}

	public void setR2(String r2) {
		this.r2 = r2;
	}

	public String getR3() {
		return this.r3;
	}

	public void setR3(String r3) {
		this.r3 = r3;
	}

	public String getR4() {
		return this.r4;
	}

	public void setR4(String r4) {
		this.r4 = r4;
	}

	public String getR5() {
		return this.r5;
	}

	public void setR5(String r5) {
		this.r5 = r5;
	}

	public Double getR6() {
		return this.r6;
	}

	public void setR6(Double r6) {
		this.r6 = r6;
	}

	public Double getR7() {
		return this.r7;
	}

	public void setR7(Double r7) {
		this.r7 = r7;
	}

	public Double getR8() {
		return this.r8;
	}

	public void setR8(Double r8) {
		this.r8 = r8;
	}

	public Integer getR9() {
		return this.r9;
	}

	public void setR9(Integer r9) {
		this.r9 = r9;
	}

	public Integer getR10() {
		return this.r10;
	}

	public void setR10(Integer r10) {
		this.r10 = r10;
	}

}