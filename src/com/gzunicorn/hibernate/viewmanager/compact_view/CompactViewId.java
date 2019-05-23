package com.gzunicorn.hibernate.viewmanager.compact_view;

/**
 * CompactViewId entity. @author MyEclipse Persistence Tools
 */
public class CompactViewId extends AbstractCompactViewId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public CompactViewId() {
	}

	/** minimal constructor */
	public CompactViewId(String billno, String maintContractNo, String busType,
			String maintDivision, String maintStation, String storageName,
			String companyId, Double contractTotal, String contractSdate,
			String contractEdate, String userid) {
		super(billno, maintContractNo, busType, maintDivision, maintStation,
				storageName, companyId, contractTotal, contractSdate,
				contractEdate, userid);
	}

	/** full constructor */
	public CompactViewId(String billno, String maintContractNo, String busType,
			Integer num, String maintDivision, String comName,
			String maintStation, String storageName, String companyId,
			Double contractTotal, String contractSdate, String contractEdate,
			String userid) {
		super(billno, maintContractNo, busType, num, maintDivision, comName,
				maintStation, storageName, companyId, contractTotal,
				contractSdate, contractEdate, userid);
	}

}
