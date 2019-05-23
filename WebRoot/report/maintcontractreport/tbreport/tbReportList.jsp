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
<html:form action="/tbReportAction.do?method=toSearchResults">

<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="causeanalysis" id="causeanalysis" value="${rmap.causeanalysis }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<input type="hidden" name="sdate2" id="sdate2" value="${rmap.sdate2 }"/>
<input type="hidden" name="edate2" id="edate2" value="${rmap.edate2 }"/>
<html:hidden property="genReport" styleId="genReport" />

	<%int i=1; %>
<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class=wordtd_header>
	<td nowrap="nowrap"  style="text-align: center;">序号</td>	
	<td nowrap="nowrap"  style="text-align: center;">办理退保日期</td>
	<td nowrap="nowrap"  style="text-align: center;">退保审核通过日期</td>
	<td nowrap="nowrap"  style="text-align: center;">丢梯日期</td>
	<td nowrap="nowrap"  style="text-align: center;">退保流水号</td>
	<td nowrap="nowrap"  style="text-align: center;">维保分部</td>
	<td nowrap="nowrap"  style="text-align: center;">维保站</td>
	<td nowrap="nowrap"  style="text-align: center;">维保合同号</td>
	<td nowrap="nowrap" width="300px" style="text-align: center;">销售合同号</td>
	<td nowrap="nowrap"  style="text-align: center;">合同性质</td>
	<td nowrap="nowrap"  style="text-align: center;">合同开始日期</td>
	<td nowrap="nowrap"  style="text-align: center;">合同结束日期</td>
	<td nowrap="nowrap"  style="text-align: center;">退保台量</td>
	<td nowrap="nowrap"  style="text-align: center;">甲方单位</td>
	<td nowrap="nowrap"  style="text-align: center;">退保原因</td>
	<td nowrap="nowrap"  style="text-align: center;">退保金额</td>
	<td nowrap="nowrap"  style="text-align: center;">退保年金额</td>
	</tr>
	
	<logic:present name="maintenanceTbReportList" >
	  <logic:iterate id="ele" name="maintenanceTbReportList">
	  <tr class="inputtd" align="center" height="20">
	  <td><%=i++ %></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="OperDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="auditDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="LostElevatorDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;">
	  	<a href="<html:rewrite page='/lostElevatorReportAction.do'/>?method=toDisplayRecord&isopenshow=Yes&id=<bean:write name="ele"  property="jnlno"/>" target="_blnk"><bean:write name="ele" property="jnlno"/></a>
	  </td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ComName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="StorageName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="MaintContractNo"/></td>
	  <td style="text-align: center;word-wrap:break-word;"><bean:write name="ele" property="SalesContractNo"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="MainModeName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ContractSDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ContractEDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="eleNum"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ProjectName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="CauseAnalysisName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="tbcon"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="tbyearcon"/></td>
	  </tr>
	  </logic:iterate>
	</logic:present>
	<logic:notPresent name="maintenanceTbReportList">
	  <tr class="noItems" align="center" height="20">
	  	<td colspan="11" >
	  		<bean:write name="showinfostr"/>
	  	</td>
	  </tr>
	</logic:notPresent>
</table>
<br/>
<b>
&nbsp;退保总台量:<bean:write name="totalhmap" property="totalnum"/>
</b>
<br/>
		
</html:form>
