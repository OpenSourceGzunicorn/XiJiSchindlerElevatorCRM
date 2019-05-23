package com.gzunicorn.bean;

import java.io.InputStream;
import java.io.Serializable;

public class FileInfoBean implements Serializable{
	
	private volatile int hashValue = 0;
	private String fileName = "";
	private InputStream file = null;
	private Integer key;
	private Integer fileSize;
	
	public FileInfoBean(){
		
	}
	
	public FileInfoBean(Integer key,String fileName,
						Integer fileSize,InputStream file){
		this.key = key;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.file = file;
	}
	

	public InputStream getFile() {
		return file;
	}

	public void setFile(InputStream file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
	
	
	
	public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof FileInfoBean))
            return false;
        FileInfoBean that = (FileInfoBean) rhs;
        if (this.getKey() == null || that.getKey() == null)
        {
            return false;
        }
        if (! this.getKey().equals(that.getKey()))
        {
            return false;
        }
        
        return true;
    }

    /**
     * Implementation of the hashCode method conforming to the Bloch pattern with
     * the exception of array properties (these are very unlikely primary key types).
     * @return int
     */
    public int hashCode()
    {
        if (this.hashValue == 0)
        {
            int result = 17;
            int keyValue = this.getKey() == null ? 0 : this.getKey().hashCode();
            result = result * 37 + keyValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	
	

}
