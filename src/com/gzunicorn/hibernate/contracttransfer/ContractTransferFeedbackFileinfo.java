package com.gzunicorn.hibernate.contracttransfer;

/**
 * ContractTransferFeedbackFileinfo entity. @author MyEclipse Persistence Tools
 */
public class ContractTransferFeedbackFileinfo extends
		AbstractContractTransferFeedbackFileinfo implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ContractTransferFeedbackFileinfo() {
	}

	/** minimal constructor */
	public ContractTransferFeedbackFileinfo(String oldFileName,
			String newFileName, String filePath, String fileFormat,
			Double fileSize, String uploadDate, String uploader) {
		super(oldFileName, newFileName, filePath, fileFormat, fileSize,
				uploadDate, uploader);
	}

	/** full constructor */
	public ContractTransferFeedbackFileinfo(String jnlno, String oldFileName,
			String newFileName, String filePath, String fileFormat,
			Double fileSize, String uploadDate, String uploader,
			String remarks, String ext2, String ext3, String ext4, String ext5,
			String ext6, String ext7, String ext8, String ext9, String ext10) {
		super(jnlno, oldFileName, newFileName, filePath, fileFormat, fileSize,
				uploadDate, uploader, remarks, ext2, ext3, ext4, ext5, ext6,
				ext7, ext8, ext9, ext10);
	}

}
