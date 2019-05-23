package com.gzunicorn.hibernate.basedata;

/**
 * AbstractClass1 entity provides the base persistence definition of the Class1
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractClass1 implements java.io.Serializable {

	// Fields

	private String class1id;
	private String class1name;
	private Integer levelid;
	private String rem1;
	private String enabledFlag;
	private String r1;

	// Constructors

	/** default constructor */
	public AbstractClass1() {
	}

	/** minimal constructor */
	public AbstractClass1(String class1id, String enabledFlag) {
		this.class1id = class1id;
		this.enabledFlag = enabledFlag;
	}

	/** full constructor */
	public AbstractClass1(String class1id, String class1name, Integer levelid,
			String rem1, String enabledFlag, String r1) {
		super();
		this.class1id = class1id;
		this.class1name = class1name;
		this.levelid = levelid;
		this.rem1 = rem1;
		this.enabledFlag = enabledFlag;
		this.r1 = r1;
	}

	public String getClass1id() {
		return this.class1id;
	}

	public void setClass1id(String class1id) {
		this.class1id = class1id;
	}

	public String getClass1name() {
		return this.class1name;
	}

	public void setClass1name(String class1name) {
		this.class1name = class1name;
	}

	public Integer getLevelid() {
		return this.levelid;
	}

	public void setLevelid(Integer levelid) {
		this.levelid = levelid;
	}

	public String getRem1() {
		return this.rem1;
	}

	public void setRem1(String rem1) {
		this.rem1 = rem1;
	}

	public String getEnabledFlag() {
		return this.enabledFlag;
	}

	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}

	public String getR1() {
		return r1;
	}

	public void setR1(String r1) {
		this.r1 = r1;
	}

	
}