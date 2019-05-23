package com.gzunicorn.hibernate.viewmanager.compact_view;

/**
 * AbstractCompactViewId entity provides the base persistence definition of the
 * CompactViewId entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCompactViewId implements java.io.Serializable {

	// Fields

	private String billno;
	private String maintContractNo;
	private String busType;
	private Integer num;
	private String maintDivision;
	private String comName;
	private String maintStation;
	private String storageName;
	private String companyId;
	private Double contractTotal;
	private String contractSdate;
	private String contractEdate;
	private String userid;

	// Constructors

	/** default constructor */
	public AbstractCompactViewId() {
	}

	/** minimal constructor */
	public AbstractCompactViewId(String billno, String maintContractNo,
			String busType, String maintDivision, String maintStation,
			String storageName, String companyId, Double contractTotal,
			String contractSdate, String contractEdate, String userid) {
		this.billno = billno;
		this.maintContractNo = maintContractNo;
		this.busType = busType;
		this.maintDivision = maintDivision;
		this.maintStation = maintStation;
		this.storageName = storageName;
		this.companyId = companyId;
		this.contractTotal = contractTotal;
		this.contractSdate = contractSdate;
		this.contractEdate = contractEdate;
		this.userid = userid;
	}

	/** full constructor */
	public AbstractCompactViewId(String billno, String maintContractNo,
			String busType, Integer num, String maintDivision, String comName,
			String maintStation, String storageName, String companyId,
			Double contractTotal, String contractSdate, String contractEdate,
			String userid) {
		this.billno = billno;
		this.maintContractNo = maintContractNo;
		this.busType = busType;
		this.num = num;
		this.maintDivision = maintDivision;
		this.comName = comName;
		this.maintStation = maintStation;
		this.storageName = storageName;
		this.companyId = companyId;
		this.contractTotal = contractTotal;
		this.contractSdate = contractSdate;
		this.contractEdate = contractEdate;
		this.userid = userid;
	}

	// Property accessors

	public String getBillno() {
		return this.billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getMaintContractNo() {
		return this.maintContractNo;
	}

	public void setMaintContractNo(String maintContractNo) {
		this.maintContractNo = maintContractNo;
	}

	public String getBusType() {
		return this.busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getMaintDivision() {
		return this.maintDivision;
	}

	public void setMaintDivision(String maintDivision) {
		this.maintDivision = maintDivision;
	}

	public String getComName() {
		return this.comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getMaintStation() {
		return this.maintStation;
	}

	public void setMaintStation(String maintStation) {
		this.maintStation = maintStation;
	}

	public String getStorageName() {
		return this.storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Double getContractTotal() {
		return this.contractTotal;
	}

	public void setContractTotal(Double contractTotal) {
		this.contractTotal = contractTotal;
	}

	public String getContractSdate() {
		return this.contractSdate;
	}

	public void setContractSdate(String contractSdate) {
		this.contractSdate = contractSdate;
	}

	public String getContractEdate() {
		return this.contractEdate;
	}

	public void setContractEdate(String contractEdate) {
		this.contractEdate = contractEdate;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractCompactViewId))
			return false;
		AbstractCompactViewId castOther = (AbstractCompactViewId) other;

		return ((this.getBillno() == castOther.getBillno()) || (this
				.getBillno() != null && castOther.getBillno() != null && this
				.getBillno().equals(castOther.getBillno())))
				&& ((this.getMaintContractNo() == castOther
						.getMaintContractNo()) || (this.getMaintContractNo() != null
						&& castOther.getMaintContractNo() != null && this
						.getMaintContractNo().equals(
								castOther.getMaintContractNo())))
				&& ((this.getBusType() == castOther.getBusType()) || (this
						.getBusType() != null && castOther.getBusType() != null && this
						.getBusType().equals(castOther.getBusType())))
				&& ((this.getNum() == castOther.getNum()) || (this.getNum() != null
						&& castOther.getNum() != null && this.getNum().equals(
						castOther.getNum())))
				&& ((this.getMaintDivision() == castOther.getMaintDivision()) || (this
						.getMaintDivision() != null
						&& castOther.getMaintDivision() != null && this
						.getMaintDivision()
						.equals(castOther.getMaintDivision())))
				&& ((this.getComName() == castOther.getComName()) || (this
						.getComName() != null && castOther.getComName() != null && this
						.getComName().equals(castOther.getComName())))
				&& ((this.getMaintStation() == castOther.getMaintStation()) || (this
						.getMaintStation() != null
						&& castOther.getMaintStation() != null && this
						.getMaintStation().equals(castOther.getMaintStation())))
				&& ((this.getStorageName() == castOther.getStorageName()) || (this
						.getStorageName() != null
						&& castOther.getStorageName() != null && this
						.getStorageName().equals(castOther.getStorageName())))
				&& ((this.getCompanyId() == castOther.getCompanyId()) || (this
						.getCompanyId() != null
						&& castOther.getCompanyId() != null && this
						.getCompanyId().equals(castOther.getCompanyId())))
				&& ((this.getContractTotal() == castOther.getContractTotal()) || (this
						.getContractTotal() != null
						&& castOther.getContractTotal() != null && this
						.getContractTotal()
						.equals(castOther.getContractTotal())))
				&& ((this.getContractSdate() == castOther.getContractSdate()) || (this
						.getContractSdate() != null
						&& castOther.getContractSdate() != null && this
						.getContractSdate()
						.equals(castOther.getContractSdate())))
				&& ((this.getContractEdate() == castOther.getContractEdate()) || (this
						.getContractEdate() != null
						&& castOther.getContractEdate() != null && this
						.getContractEdate()
						.equals(castOther.getContractEdate())))
				&& ((this.getUserid() == castOther.getUserid()) || (this
						.getUserid() != null && castOther.getUserid() != null && this
						.getUserid().equals(castOther.getUserid())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBillno() == null ? 0 : this.getBillno().hashCode());
		result = 37
				* result
				+ (getMaintContractNo() == null ? 0 : this.getMaintContractNo()
						.hashCode());
		result = 37 * result
				+ (getBusType() == null ? 0 : this.getBusType().hashCode());
		result = 37 * result
				+ (getNum() == null ? 0 : this.getNum().hashCode());
		result = 37
				* result
				+ (getMaintDivision() == null ? 0 : this.getMaintDivision()
						.hashCode());
		result = 37 * result
				+ (getComName() == null ? 0 : this.getComName().hashCode());
		result = 37
				* result
				+ (getMaintStation() == null ? 0 : this.getMaintStation()
						.hashCode());
		result = 37
				* result
				+ (getStorageName() == null ? 0 : this.getStorageName()
						.hashCode());
		result = 37 * result
				+ (getCompanyId() == null ? 0 : this.getCompanyId().hashCode());
		result = 37
				* result
				+ (getContractTotal() == null ? 0 : this.getContractTotal()
						.hashCode());
		result = 37
				* result
				+ (getContractSdate() == null ? 0 : this.getContractSdate()
						.hashCode());
		result = 37
				* result
				+ (getContractEdate() == null ? 0 : this.getContractEdate()
						.hashCode());
		result = 37 * result
				+ (getUserid() == null ? 0 : this.getUserid().hashCode());
		return result;
	}

}