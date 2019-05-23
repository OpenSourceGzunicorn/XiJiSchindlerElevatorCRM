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
<html:form action="/calloutMasterIsStopReportAction.do?method=toSearchResults">

<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<html:hidden property="genReport" styleId="genReport" />

	<%int i=1; %>
<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class=wordtd_header>
		<td nowrap="nowrap"  style="text-align: center;">序号</td>	
		<td nowrap="nowrap"  style="text-align: center;">急修编号</td>
		<td nowrap="nowrap"  style="text-align: center;">维保分部</td>
		<td nowrap="nowrap"  style="text-align: center;">维保站</td>
		<td nowrap="nowrap"  style="text-align: center;">报修时间</td>
		<td nowrap="nowrap"  style="text-align: center;">处理人</td>
		<td nowrap="nowrap"  style="text-align: center;">报修单位名称</td>
		<td nowrap="nowrap"  style="text-align: center;">电梯编号</td>
		<td nowrap="nowrap"  style="text-align: center;">停梯备注</td>
	</tr>
	
	<logic:present name="isStopReportList" >
	  <logic:iterate id="ele" name="isStopReportList">
	  <tr class="inputtd" align="center" height="20">
		  <td><%=i++ %></td>
		  <td nowrap="nowrap"  style="text-align: center;">
		  	<a href="<html:rewrite page='/hotphoneAction.do'/>?typejsp=display&isopenshow=Yes&id=<bean:write name="ele" property="calloutmasterno"/>" target="_blnk"><bean:write name="ele" property="calloutmasterno"/></a>
		  </td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="comname"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="storagename"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="repairtime"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="username"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="companyname"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="elevatorno"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="stoprem"/></td>
	  
	  </tr>
	  </logic:iterate>
	</logic:present>
	<logic:notPresent name="isStopReportList">
	  <tr class="noItems" align="center" height="20">
	  	<td colspan="9" >
	  		<bean:write name="showinfostr"/>
	  	</td>
	  </tr>
	</logic:notPresent>
</table>
<br/>
		
</html:form>
