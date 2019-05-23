package com.gzunicorn.hibernate.mobileofficeplatform.technologysupport;

/**
 * AbstractTechnologySupport entity provides the base persistence definition of
 * the TechnologySupport entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTechnologySupport implements java.io.Serializable {

	// Fields

	private String billno;
	private String singleNo;
	private String elevatorNo;
	private String maintDivision;
	private String maintStation;
	private String hmtId;
	private String faultCode;
	private String faultStatus;
	private String msprocessPeople;
	private String msprocessDate;
	private String msprocessRem;
	private String msisResolve;
	private String mmprocessPeople;
	private String mmprocessDate;
	private String mmprocessRem;
	private String mmisResolve;
	private String tsprocessPeople;
	private String tsprocessDate;
	private String tsprocessRem;
	private String assignUser;
	private String assignUserTel;
	private String operDate;
	private String proStatus;
	private String gzImage;
	private String r1;
	private String r2;
	private String r3;
	private String r4;
	private String r5;
	private Double r6;
	private Integer r7;
	private Integer r8;
	private Double r9;
	private Double r10;

	// Constructors
	public String getGzImage() {
		return gzImage;
	}

	public void setGzImage(String gzImage) {
		this.gzImage = gzImage;
	}
	public String getElevatorNo() {
		return elevatorNo;
	}

	public void setElevatorNo(String elevatorNo) {
		this.elevatorNo = elevatorNo;
	}

	/** default constructor */
	public AbstractTechnologySupport() {
	}

	/** minimal constructor */
	public AbstractTechnologySupport(String billno, String singleNo,
			String maintDivision, String maintStation, String hmtId,
			String faultCode, String faultStatus, String assignUser,
			String assignUserTel, String operDate) {
		this.billno = billno;
		this.singleNo = singleNo;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.hmtId = hmtId;
		this.faultCode = faultCode;
		this.faultStatus = faultStatus;
		this.assignUser = assignUser;
		this.assignUserTel = assignUserTel;
		this.operDate = operDate;
	}

	/** full constructor */
	public AbstractTechnologySupport(String billno, String singleNo,String elevatorNo,
			String maintDivision, String maintStation, String hmtId,
			String faultCode, String faultStatus, String msprocessPeople,
			String msprocessDate, String msprocessRem, String msisResolve,
			String mmprocessPeople, String mmprocessDate, String mmprocessRem,
			String mmisResolve, String tsprocessPeople, String tsprocessDate,
			String tsprocessRem, String assignUser, String assignUserTel,
			String operDate, String proStatus,String gzImage, String r1, String r2, String r3,
			String r4, String r5, Double r6, Integer r7, Integer r8, Double r9,
			Double r10) {
		this.elevatorNo=elevatorNo;
		this.gzImage=gzImage;
		this.billno = billno;
		this.singleNo = singleNo;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.hmtId = hmtId;
		this.faultCode = faultCode;
		this.faultStatus = faultStatus;
		this.msprocessPeople = msprocessPeople;
		this.msprocessDate = msprocessDate;
		this.msprocessRem = msprocessRem;
		this.msisResolve = msisResolve;
		this.mmprocessPeople = mmprocessPeople;
		this.mmprocessDate = mmprocessDate;
		this.mmprocessRem = mmprocessRem;
		this.mmisResolve = mmisResolve;
		this.tsprocessPeople = tsprocessPeople;
		this.tsprocessDate = tsprocessDate;
		this.tsprocessRem = tsprocessRem;
		this.assignUser = assignUser;
		this.assignUserTel = assignUserTel;
		this.operDate = operDate;
		this.proStatus = proStatus;
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

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getSingleNo() {
		return this.singleNo;
	}

	public void setSingleNo(String singleNo) {
		this.singleNo = singleNo;
	}

	public String getMaintDivision() {
		return this.maintDivision;
	}

	public void setMaintDivision(String maintDivision) {
		this.maintDivision = maintDivision;
	}

	public String getMaintStation() {
		return this.maintStation;
	}

	public void setMaintStation(String maintStation) {
		this.maintStation = maintStation;
	}

	public String getHmtId() {
		return this.hmtId;
	}

	public void setHmtId(String hmtId) {
		this.hmtId = hmtId;
	}

	public String getFaultCode() {
		return this.faultCode;
	}

	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	public String getFaultStatus() {
		return this.faultStatus;
	}

	public void setFaultStatus(String faultStatus) {
		this.faultStatus = faultStatus;
	}

	public String getMsprocessPeople() {
		return this.msprocessPeople;
	}

	public void setMsprocessPeople(String msprocessPeople) {
		this.msprocessPeople = msprocessPeople;
	}

	public String getMsprocessDate() {
		return this.msprocessDate;
	}

	public void setMsprocessDate(String msprocessDate) {
		this.msprocessDate = msprocessDate;
	}

	public String getMsprocessRem() {
		return this.msprocessRem;
	}

	public void setMsprocessRem(String msprocessRem) {
		this.msprocessRem = msprocessRem;
	}

	public String getMsisResolve() {
		return this.msisResolve;
	}

	public void setMsisResolve(String msisResolve) {
		this.msisResolve = msisResolve;
	}

	public String getMmprocessPeople() {
		return this.mmprocessPeople;
	}

	public void setMmprocessPeople(String mmprocessPeople) {
		this.mmprocessPeople = mmprocessPeople;
	}

	public String getMmprocessDate() {
		return this.mmprocessDate;
	}

	public void setMmprocessDate(String mmprocessDate) {
		this.mmprocessDate = mmprocessDate;
	}

	public String getMmprocessRem() {
		return this.mmprocessRem;
	}

	public void setMmprocessRem(String mmprocessRem) {
		this.mmprocessRem = mmprocessRem;
	}

	public String getMmisResolve() {
		return this.mmisResolve;
	}

	public void setMmisResolve(String mmisResolve) {
		this.mmisResolve = mmisResolve;
	}

	public String getTsprocessPeople() {
		return this.tsprocessPeople;
	}

	public void setTsprocessPeople(String tsprocessPeople) {
		this.tsprocessPeople = tsprocessPeople;
	}

	public String getTsprocessDate() {
		return this.tsprocessDate;
	}

	public void setTsprocessDate(String tsprocessDate) {
		this.tsprocessDate = tsprocessDate;
	}

	public String getTsprocessRem() {
		return this.tsprocessRem;
	}

	public void setTsprocessRem(String tsprocessRem) {
		this.tsprocessRem = tsprocessRem;
	}

	public String getAssignUser() {
		return this.assignUser;
	}

	public void setAssignUser(String assignUser) {
		this.assignUser = assignUser;
	}

	public String getAssignUserTel() {
		return this.assignUserTel;
	}

	public void setAssignUserTel(String assignUserTel) {
		this.assignUserTel = assignUserTel;
	}

	public String getOperDate() {
		return this.operDate;
	}

	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}

	public String getProStatus() {
		return this.proStatus;
	}

	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
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

	public Integer getR7() {
		return this.r7;
	}

	public void setR7(Integer r7) {
		this.r7 = r7;
	}

	public Integer getR8() {
		return this.r8;
	}

	public void setR8(Integer r8) {
		this.r8 = r8;
	}

	public Double getR9() {
		return this.r9;
	}

	public void setR9(Double r9) {
		this.r9 = r9;
	}

	public Double getR10() {
		return this.r10;
	}

	public void setR10(Double r10) {
		this.r10 = r10;
	}

}