<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
  <script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
  <link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
  <script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<br>
<html:form action="/qcdfReportAction.do?method=toSearchResults">

<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="maintpersonnel" id="maintpersonnel" value="${rmap.maintpersonnel }"/>
<input type="hidden" name="projectname" id="projectname" value="${rmap.projectname }"/>
<input type="hidden" name="salescontractno" id="salescontractno" value="${rmap.salescontractno }"/>
<input type="hidden" name="elevatorno" id="elevatorno" value="${rmap.elevatorno }"/>
<input type="hidden" name="scorelevel" id="scorelevel" value="${rmap.scorelevel }"/>
<input type="hidden" name="sdate" id="sdate" value="${rmap.sdate }"/>
<input type="hidden" name="edate" id="edate" value="${rmap.edate }"/>
<html:hidden property="genReport" styleId="genReport" />

	<%int i=1; %>
<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class=wordtd_header>
	<td nowrap="nowrap"  style="text-align: center;">ÐòºÅ</td>	
		<logic:present name="totalhmap" >
		  	<logic:iterate id="ele" name="totalhmap" property="titlename">
				<td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele"/></td>
			</logic:iterate>
		</logic:present>
	</tr>
	
	<logic:present name="reportList" >
	  <logic:iterate id="ele" name="reportList">
	  <tr class="inputtd" align="center" height="20">
	  <td><%=i++ %></td>
	  <logic:present name="totalhmap" >
		  	<logic:iterate id="ele2" name="totalhmap" property="titleid">
				<td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="${ele2 }"/></td>
			</logic:iterate>
		</logic:present>
	  </tr>
	  </logic:iterate>
	</logic:present>
	<logic:notPresent name="reportList">
	  <tr class="noItems" align="center" height="20">
	  	<td colspan="${totalhmap.tlen+1 }" >
	  		<bean:write name="showinfostr"/>
	  	</td>
	  </tr>
	</logic:notPresent>
</table>
<br/>
<logic:present name="totalhmap" >
	<table>
		<tr>
		<td><b><bean:write name="totalhmap" property="totalstr"/></b></td>
		</tr>
	</table>
</logic:present>
<br/>
		
</html:form>
