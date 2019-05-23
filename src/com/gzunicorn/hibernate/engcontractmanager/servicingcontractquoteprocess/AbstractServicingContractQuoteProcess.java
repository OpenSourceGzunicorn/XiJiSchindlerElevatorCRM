package com.gzunicorn.hibernate.engcontractmanager.servicingcontractquoteprocess;

import com.gzunicorn.hibernate.engcontractmanager.servicingcontractquotemaster.ServicingContractQuoteMaster;

/**
 * AbstractServicingContractQuoteProcess entity provides the base persistence
 * definition of the ServicingContractQuoteProcess entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractServicingContractQuoteProcess implements
		java.io.Serializable {

	// Fields

	private Integer itemId;
	private ServicingContractQuoteMaster servicingContractQuoteMaster;
	private Integer taskId;
	private String taskName;
	private Long tokenId;
	private String userId;
	private String date1;
	private String time1;
	private String approveResult;
	private String approveRem;
	private String approveRem2;
	private String approveRem3;
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
	public AbstractServicingContractQuoteProcess() {
	}

	/** minimal constructor */
	public AbstractServicingContractQuoteProcess(
			ServicingContractQuoteMaster servicingContractQuoteMaster,
			Integer taskId, String taskName, Long tokenId, String userId,
			String date1, String time1) {
		this.servicingContractQuoteMaster = servicingContractQuoteMaster;
		this.taskId = taskId;
		this.taskName = taskName;
		this.tokenId = tokenId;
		this.userId = userId;
		this.date1 = date1;
		this.time1 = time1;
	}

	/** full constructor */
	public AbstractServicingContractQuoteProcess(
			ServicingContractQuoteMaster servicingContractQuoteMaster,
			Integer taskId, String taskName, Long tokenId, String userId,
			String date1, String time1, String approveResult,
			String approveRem, String approveRem2, String approveRem3,
			String r1, String r2, String r3, String r4, String r5, Double r6,
			Double r7, Double r8, Integer r9, Integer r10) {
		this.servicingContractQuoteMaster = servicingContractQuoteMaster;
		this.taskId = taskId;
		this.taskName = taskName;
		this.tokenId = tokenId;
		this.userId = userId;
		this.date1 = date1;
		this.time1 = time1;
		this.approveResult = approveResult;
		this.approveRem = approveRem;
		this.approveRem2 = approveRem2;
		this.approveRem3 = approveRem3;
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

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public ServicingContractQuoteMaster getServicingContractQuoteMaster() {
		return this.servicingContractQuoteMaster;
	}

	public void setServicingContractQuoteMaster(
			ServicingContractQuoteMaster servicingContractQuoteMaster) {
		this.servicingContractQuoteMaster = servicingContractQuoteMaster;
	}

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Long getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDate1() {
		return this.date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getTime1() {
		return this.time1;
	}

	public void setTime1(String time1) {
		this.time1 = time1;
	}

	public String getApproveResult() {
		return this.approveResult;
	}

	public void setApproveResult(String approveResult) {
		this.approveResult = approveResult;
	}

	public String getApproveRem() {
		return this.approveRem;
	}

	public void setApproveRem(String approveRem) {
		this.approveRem = approveRem;
	}

	public String getApproveRem2() {
		return this.approveRem2;
	}

	public void setApproveRem2(String approveRem2) {
		this.approveRem2 = approveRem2;
	}

	public String getApproveRem3() {
		return this.approveRem3;
	}

	public void setApproveRem3(String approveRem3) {
		this.approveRem3 = approveRem3;
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