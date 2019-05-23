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
<html:form action="/eleNumReportAction.do?method=toSearchResults">

<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="signway" id="signway" value="${rmap.signway }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<html:hidden property="genReport" styleId="genReport" />

	<%int i=1; %>
<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class=wordtd_header>
	<td nowrap="nowrap"  style="text-align: center;">序号</td>	
	<td nowrap="nowrap"  style="text-align: center;">维保分部</td>
	<td nowrap="nowrap"  style="text-align: center;">维保站</td>
	<td nowrap="nowrap"  style="text-align: center;">维保合同号</td>
	<td nowrap="nowrap"  style="text-align: center;">甲方名称</td>
	<td nowrap="nowrap"  style="text-align: center;">电梯编号</td>
	<td nowrap="nowrap"  style="text-align: center;">签署方式</td>
	<td nowrap="nowrap"  style="text-align: center;">维保开始日期</td>
	<td nowrap="nowrap"  style="text-align: center;">维保结束日期</td>
	<td nowrap="nowrap"  style="text-align: center;">合同审核通过日期</td>
	<td nowrap="nowrap"  style="text-align: center;">金额</td>
	</tr>
	
	<logic:present name="maintEleNumReportList" >
	  <logic:iterate id="ele" name="maintEleNumReportList">
	  <tr class="inputtd" align="center" height="20">
	  <td><%=i++ %></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ComName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="StorageName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;">
	  	<a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="ele"  property="BillNo"/>" target="_blnk"><bean:write name="ele" property="MaintContractNo"/></a>
	  </td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="CompanyName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ElevatorNo"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="SignWayname"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="MainSDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="MainEDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="AuditDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="emoney"/></td>
	  </tr>
	  </logic:iterate>
	</logic:present>
	<logic:notPresent name="maintEleNumReportList">
	  <tr class="noItems" align="center" height="20">
	  	<td colspan="11" >
	  		<bean:write name="showinfostr"/>
	  	</td>
	  </tr>
	</logic:notPresent>
</table>
<br/>
<logic:present name="huizlist" >
	<table>
	<logic:iterate id="ele2" name="huizlist">
		<tr>
		<td>&nbsp;&nbsp;<b><bean:write name="ele2" property="SignWayname"/>：</b></td>
		<td><b><bean:write name="ele2" property="enum"/></b></td>
		</tr>
	</logic:iterate>
	</table>
</logic:present>
<br/>
		
</html:form>
