package com.gzunicorn.hibernate.contracttransfer;

/**
 * AbstractContractTransferFeedbackFileinfo entity provides the base persistence
 * definition of the ContractTransferFeedbackFileinfo entity. @author MyEclipse
 * Persistence Tools
 */

public abstract class AbstractContractTransferFeedbackFileinfo implements
		java.io.Serializable {

	// Fields

	private Integer fileSid;
	private String jnlno;
	private String oldFileName;
	private String newFileName;
	private String filePath;
	private String fileFormat;
	private Double fileSize;
	private String uploadDate;
	private String uploader;
	private String remarks;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String ext6;
	private String ext7;
	private String ext8;
	private String ext9;
	private String ext10;

	// Constructors

	/** default constructor */
	public AbstractContractTransferFeedbackFileinfo() {
	}

	/** minimal constructor */
	public AbstractContractTransferFeedbackFileinfo(String oldFileName,
			String newFileName, String filePath, String fileFormat,
			Double fileSize, String uploadDate, String uploader) {
		this.oldFileName = oldFileName;
		this.newFileName = newFileName;
		this.filePath = filePath;
		this.fileFormat = fileFormat;
		this.fileSize = fileSize;
		this.uploadDate = uploadDate;
		this.uploader = uploader;
	}

	/** full constructor */
	public AbstractContractTransferFeedbackFileinfo(String jnlno,
			String oldFileName, String newFileName, String filePath,
			String fileFormat, Double fileSize, String uploadDate,
			String uploader, String remarks, String ext2, String ext3,
			String ext4, String ext5, String ext6, String ext7, String ext8,
			String ext9, String ext10) {
		this.jnlno = jnlno;
		this.oldFileName = oldFileName;
		this.newFileName = newFileName;
		this.filePath = filePath;
		this.fileFormat = fileFormat;
		this.fileSize = fileSize;
		this.uploadDate = uploadDate;
		this.uploader = uploader;
		this.remarks = remarks;
		this.ext2 = ext2;
		this.ext3 = ext3;
		this.ext4 = ext4;
		this.ext5 = ext5;
		this.ext6 = ext6;
		this.ext7 = ext7;
		this.ext8 = ext8;
		this.ext9 = ext9;
		this.ext10 = ext10;
	}

	// Property accessors

	public Integer getFileSid() {
		return this.fileSid;
	}

	public void setFileSid(Integer fileSid) {
		this.fileSid = fileSid;
	}

	public String getJnlno() {
		return this.jnlno;
	}

	public void setJnlno(String jnlno) {
		this.jnlno = jnlno;
	}

	public String getOldFileName() {
		return this.oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	public String getNewFileName() {
		return this.newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileFormat() {
		return this.fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public Double getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}

	public String getUploadDate() {
		return this.uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUploader() {
		return this.uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getExt2() {
		return this.ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return this.ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return this.ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return this.ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getExt6() {
		return this.ext6;
	}

	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}

	public String getExt7() {
		return this.ext7;
	}

	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}

	public String getExt8() {
		return this.ext8;
	}

	public void setExt8(String ext8) {
		this.ext8 = ext8;
	}

	public String getExt9() {
		return this.ext9;
	}

	public void setExt9(String ext9) {
		this.ext9 = ext9;
	}

	public String getExt10() {
		return this.ext10;
	}

	public void setExt10(String ext10) {
		this.ext10 = ext10;
	}

}