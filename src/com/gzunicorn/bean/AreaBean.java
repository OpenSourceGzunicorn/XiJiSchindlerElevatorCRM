package com.gzunicorn.bean;

public class AreaBean implements java.io.Serializable{
	private String areaid; 
	private String areaidref;
	private String areaname;
	public String getAreaid()
	{
		return areaid;
	}
	public void setAreaid(String areaid)
	{
		this.areaid = areaid;
	}
	public String getAreaidref()
	{
		return areaidref;
	}
	public void setAreaidref(String areaidref)
	{
		this.areaidref = areaidref;
	}
	public String getAreaname()
	{
		return areaname;
	}
	public void setAreaname(String areaname)
	{
		this.areaname = areaname;
	}
	public AreaBean() {
		super();
		// TODO Auto-generated constructor stub
	}

}
