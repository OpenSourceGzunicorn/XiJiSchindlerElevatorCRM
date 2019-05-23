package com.gzunicorn.hibernate.basedata;

/**
 * Fileinfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Fileinfo extends AbstractFileinfo implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Fileinfo() {
	}

	/** minimal constructor */
	public Fileinfo(String materSid, String busTable, String fileTitle,
			String oldFileName, String newFileName, String filePath,
			Double fileSize, String uploadDate, String uploader) {
		super(materSid, busTable, fileTitle, oldFileName, newFileName,
				filePath, fileSize, uploadDate, uploader);
	}

	/** full constructor */
	public Fileinfo(String materSid, String busTable, String fileTitle,
			String oldFileName, String newFileName, String filePath,
			String fileFormat, Double fileSize, String uploadDate,
			String uploader, String remarks, String ext1, String ext2,
			String ext3, String ext4, String ext5, String ext6, String ext7,
			String ext8, String ext9, String ext10) {
		super(materSid, busTable, fileTitle, oldFileName, newFileName,
				filePath, fileFormat, fileSize, uploadDate, uploader, remarks,
				ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8, ext9, ext10);
	}

}
