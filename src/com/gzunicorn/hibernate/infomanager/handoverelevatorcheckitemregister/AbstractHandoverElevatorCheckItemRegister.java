package com.gzunicorn.hibernate.infomanager.handoverelevatorcheckitemregister;

import java.util.HashSet;
import java.util.Set;

import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;

/**
 * AbstractHandoverElevatorCheckItemRegister entity provides the base
 * persistence definition of the HandoverElevatorCheckItemRegister entity. @author
 * MyEclipse Persistence Tools
 */

public abstract class AbstractHandoverElevatorCheckItemRegister implements
		java.io.Serializable {

	// Fields

	private String jnlno;
	private ElevatorTransferCaseRegister elevatorTransferCaseRegister;
	private String examType;
	private String checkItem;
	private String issueCoding;
	private String issueContents;
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
	private String isRecheck;
	private String isDelete;
	private String deleteRem;
	private String rem;
	private String isyzg;
	
	private Set handoverElevatorCheckFileinfos = new HashSet(0);

	// Constructors

	/** default constructor */
	public AbstractHandoverElevatorCheckItemRegister() {
	}

	/** minimal constructor */
	public AbstractHandoverElevatorCheckItemRegister(String jnlno,
			String examType, String checkItem, String issueCoding,
			String issueContents) {
		this.jnlno = jnlno;
		this.examType = examType;
		this.checkItem = checkItem;
		this.issueCoding = issueCoding;
		this.issueContents = issueContents;
	}

	/** full constructor */
	public AbstractHandoverElevatorCheckItemRegister(String jnlno,
			ElevatorTransferCaseRegister elevatorTransferCaseRegister,
			String examType, String checkItem, String issueCoding,
			String issueContents, String r1, String r2, String r3, String r4,
			String r5, Double r6, Double r7, Double r8, Integer r9,
			Integer r10, String isRecheck, String isDelete, String deleteRem,
			String rem,String isyzg, Set handoverElevatorCheckFileinfos) {
		this.isyzg=isyzg;
		this.jnlno = jnlno;
		this.elevatorTransferCaseRegister = elevatorTransferCaseRegister;
		this.examType = examType;
		this.checkItem = checkItem;
		this.issueCoding = issueCoding;
		this.issueContents = issueContents;
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
		this.isRecheck = isRecheck;
		this.isDelete = isDelete;
		this.deleteRem = deleteRem;
		this.rem = rem;
		this.handoverElevatorCheckFileinfos = handoverElevatorCheckFileinfos;
	}

	// Property accessors
	public String getIsyzg() {
		return isyzg;
	}

	public void setIsyzg(String isyzg) {
		this.isyzg = isyzg;
	}

	public String getJnlno() {
		return this.jnlno;
	}

	public void setJnlno(String jnlno) {
		this.jnlno = jnlno;
	}

	public ElevatorTransferCaseRegister getElevatorTransferCaseRegister() {
		return this.elevatorTransferCaseRegister;
	}

	public void setElevatorTransferCaseRegister(
			ElevatorTransferCaseRegister elevatorTransferCaseRegister) {
		this.elevatorTransferCaseRegister = elevatorTransferCaseRegister;
	}

	public String getExamType() {
		return this.examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getCheckItem() {
		return this.checkItem;
	}

	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}

	public String getIssueCoding() {
		return this.issueCoding;
	}

	public void setIssueCoding(String issueCoding) {
		this.issueCoding = issueCoding;
	}

	public String getIssueContents() {
		return this.issueContents;
	}

	public void setIssueContents(String issueContents) {
		this.issueContents = issueContents;
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

	public String getIsRecheck() {
		return this.isRecheck;
	}

	public void setIsRecheck(String isRecheck) {
		this.isRecheck = isRecheck;
	}

	public String getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getDeleteRem() {
		return this.deleteRem;
	}

	public void setDeleteRem(String deleteRem) {
		this.deleteRem = deleteRem;
	}

	public String getRem() {
		return this.rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public Set getHandoverElevatorCheckFileinfos() {
		return this.handoverElevatorCheckFileinfos;
	}

	public void setHandoverElevatorCheckFileinfos(
			Set handoverElevatorCheckFileinfos) {
		this.handoverElevatorCheckFileinfos = handoverElevatorCheckFileinfos;
	}

}