package com.gzunicorn.hibernate.basedata.personnelmanage;



/**
 * AbstractPersonnelManageMaster entity provides the base persistence definition of the PersonnelManageMaster entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractPersonnelManageMaster  implements java.io.Serializable {


    // Fields    

     private String billno;
     private String name;
     private String sex;
     private String education;
     private String idCardNo;
     private String familyAddress;
     private String phone;
     private String contractProperties;
     private String contractNo;
     private String startDate;
     private String endDate;
     private String years;
     private String sector;
     private String workAddress;
     private String jobTitle;
     private String level;
     private String enabledFlag;
     private String rem;
     private String operId;
     private String operDate;
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
     private String phone2;
     private String jobLeval;
     private String hubie;
     private String probation;
     private Double probationgz;
     private String iszhuwai;
     private String birthDate;
     private String ygid;
     private String probationEdate;
     private String maintStation;

	public String getProbationEdate() {
		return probationEdate;
	}

	public void setProbationEdate(String probationEdate) {
		this.probationEdate = probationEdate;
	}

	public String getMaintStation() {
		return maintStation;
	}

	public void setMaintStation(String maintStation) {
		this.maintStation = maintStation;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getJobLeval() {
		return jobLeval;
	}

	public void setJobLeval(String jobLeval) {
		this.jobLeval = jobLeval;
	}

	public String getHubie() {
		return hubie;
	}

	public void setHubie(String hubie) {
		this.hubie = hubie;
	}

	public String getProbation() {
		return probation;
	}

	public void setProbation(String probation) {
		this.probation = probation;
	}

	public Double getProbationgz() {
		return probationgz;
	}

	public void setProbationgz(Double probationgz) {
		this.probationgz = probationgz;
	}

	public String getIszhuwai() {
		return iszhuwai;
	}

	public void setIszhuwai(String iszhuwai) {
		this.iszhuwai = iszhuwai;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getYgid() {
		return ygid;
	}

	public void setYgid(String ygid) {
		this.ygid = ygid;
	}

	/** default constructor */
    public AbstractPersonnelManageMaster() {
    }

	/** minimal constructor */
    public AbstractPersonnelManageMaster(String billno, String name, String education, String idCardNo, String familyAddress, String phone, String contractProperties, String contractNo, String startDate, String sector, String workAddress, String jobTitle, String level, String enabledFlag, String operId, String operDate) {
        this.billno = billno;
        this.name = name;
        this.education = education;
        this.idCardNo = idCardNo;
        this.familyAddress = familyAddress;
        this.phone = phone;
        this.contractProperties = contractProperties;
        this.contractNo = contractNo;
        this.startDate = startDate;
        this.sector = sector;
        this.workAddress = workAddress;
        this.jobTitle = jobTitle;
        this.level = level;
        this.enabledFlag = enabledFlag;
        this.operId = operId;
        this.operDate = operDate;
    }
    
    /** full constructor */
    public AbstractPersonnelManageMaster(String billno, String name, String sex, String education, String idCardNo,
    		String familyAddress, String phone, String contractProperties, String contractNo, String startDate, 
    		String endDate, String years, String sector, String workAddress, String jobTitle, String level, 
    		String enabledFlag, String rem, String operId, String operDate, String r1, String r2, String r3, 
    		String r4, String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10,
    		String phone2,String jobLeval,String hubie,String probation,
    		Double probationgz,String iszhuwai,String birthDate,String ygid,String probationEdate,String maintStation) {
    	this.probationEdate=probationEdate;
    	this.maintStation=maintStation;
    	this.ygid=ygid;
    	this.phone2=phone2;
    	this.jobLeval=jobLeval;
    	this.hubie=hubie;
    	this.probation=probation;
    	this.probationgz=probationgz;
    	this.iszhuwai=iszhuwai;
    	this.birthDate=birthDate;
        this.billno = billno;
        this.name = name;
        this.sex = sex;
        this.education = education;
        this.idCardNo = idCardNo;
        this.familyAddress = familyAddress;
        this.phone = phone;
        this.contractProperties = contractProperties;
        this.contractNo = contractNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.years = years;
        this.sector = sector;
        this.workAddress = workAddress;
        this.jobTitle = jobTitle;
        this.level = level;
        this.enabledFlag = enabledFlag;
        this.rem = rem;
        this.operId = operId;
        this.operDate = operDate;
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

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEducation() {
        return this.education;
    }
    
    public void setEducation(String education) {
        this.education = education;
    }

    public String getIdCardNo() {
        return this.idCardNo;
    }
    
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getFamilyAddress() {
        return this.familyAddress;
    }
    
    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContractProperties() {
        return this.contractProperties;
    }
    
    public void setContractProperties(String contractProperties) {
        this.contractProperties = contractProperties;
    }

    public String getContractNo() {
        return this.contractNo;
    }
    
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getYears() {
        return this.years;
    }
    
    public void setYears(String years) {
        this.years = years;
    }

    public String getSector() {
        return this.sector;
    }
    
    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getWorkAddress() {
        return this.workAddress;
    }
    
    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLevel() {
        return this.level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }

    public String getEnabledFlag() {
        return this.enabledFlag;
    }
    
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getRem() {
        return this.rem;
    }
    
    public void setRem(String rem) {
        this.rem = rem;
    }

    public String getOperId() {
        return this.operId;
    }
    
    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getOperDate() {
        return this.operDate;
    }
    
    public void setOperDate(String operDate) {
        this.operDate = operDate;
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