
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<br>
<html:form action="/companyPaymentReportAction.do?method=toSearchResults">
<html:hidden property="genReport" styleId="genReport" />
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
    	<td width="20%" class="wordtd">所属部门:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="maintdivision" styleId="maintdivision">
		    	<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
    		</html:select>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">委托合同号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="contractno" styleId="contractno" styleClass="default_input" />
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">委托单位名称:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="companyname" styleId="companyname" styleClass="default_input" size="33" />
    	</td>
    </tr>
    <tr>
	    <td width="20%" class="wordtd">付款日期:</td>
	    <td width="80%" class="inputtd">
		    <html:text property="paragraphdate1" styleId="paragraphdate1" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
			- 
			<html:text property="paragraphdate2" styleId="paragraphdate2" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
	    </td>
    </tr>
    </table>
</html:form>


