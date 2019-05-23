package com.gzunicorn.hibernate.basedata;

/**
 * Class1 entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Class1 extends AbstractClass1 implements java.io.Serializable {



	// Constructors

	/** default constructor */
	public Class1() {
	}

	/** minimal constructor */
	public Class1(String class1id, String enabledFlag) {
		super(class1id, enabledFlag);
	}

	/** full constructor 
	 * @param r1 */
	public Class1(String class1id, String class1name, Integer levelid,
			String rem1, String enabledFlag, String r1) {
		super(class1id, class1name, levelid, rem1, enabledFlag,r1);
	}

}
