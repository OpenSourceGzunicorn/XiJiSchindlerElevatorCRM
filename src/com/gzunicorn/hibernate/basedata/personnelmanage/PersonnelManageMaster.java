package com.gzunicorn.hibernate.basedata.personnelmanage;



/**
 * PersonnelManageMaster entity. @author MyEclipse Persistence Tools
 */
public class PersonnelManageMaster extends AbstractPersonnelManageMaster 
		implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public PersonnelManageMaster() {
    }

	/** minimal constructor */
    public PersonnelManageMaster(String billno, String name, String education, String idCardNo, String familyAddress, 
    		String phone, String contractProperties, String contractNo, String startDate, String sector, String workAddress,
    		String jobTitle, String level, String enabledFlag, String operId, String operDate) {
        super(billno, name, education, idCardNo, familyAddress, phone, contractProperties, contractNo, startDate, sector, 
        		workAddress, jobTitle, level, enabledFlag, operId, operDate);        
    }
    
    /** full constructor */
    public PersonnelManageMaster(String billno, String name, String sex, String education, String idCardNo, 
    		String familyAddress, String phone, String contractProperties, String contractNo, String startDate, 
    		String endDate, String years, String sector, String workAddress, String jobTitle, String level, 
    		String enabledFlag, String rem, String operId, String operDate, String r1, String r2, String r3, 
    		String r4, String r5, Double r6, Double r7, Double r8, Integer r9, Integer r10,
    		String phone2,String jobLeval,String hubie,String probation,
    		Double probationgz,String iszhuwai,String birthDate,String ygid,String probationEdate,String maintStation) {
        super(billno, name, sex, education, idCardNo, familyAddress, phone, contractProperties, contractNo, 
        		startDate, endDate, years, sector, workAddress, jobTitle, level, enabledFlag, rem, operId, 
        		operDate, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10,
        		phone2,jobLeval,hubie,probation,probationgz,iszhuwai,birthDate,ygid,probationEdate,maintStation);        
    }
   
}
