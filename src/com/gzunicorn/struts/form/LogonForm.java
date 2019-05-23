//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.form;

import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/** 
 * MyEclipse Struts
 * Creation date: 07-10-2005
 * 
 * XDoclet definition:
 * @struts:form name="logonForm"
 */
public class LogonForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/** userID property */
	private String userID;

	/** passwd property */
	private String passwd;

	/** 
	 * Returns the userID.
	 * @return String
	 */
	public String getUserID() {
		return userID;
	}

	/** 
	 * Set the userID.
	 * @param userID The userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/** 
	 * Returns the passwd.
	 * @return String
	 */
	public String getPasswd() {
		return passwd;
	}

	/** 
	 * Set the passwd.
	 * @param passwd The passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}