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
  <script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
  <script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
  <script type="text/javascript" src="<html:rewrite page="/common/javascript/highcharts/highcharts.js"/>"></script>


<br>
<html:form action="/reimbursExpenseReportAction.do?method=toSearchResults">

<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="reimburspeople" id="reimburspeople" value="${rmap.reimburspeople }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<input type="hidden" name="maintcontractno" id="maintcontractno" value="${rmap.maintcontractno }"/>
<input type="hidden" name="reimburrange" id="reimburrange" value="${rmap.reimburrange }"/>
<input type="hidden" name="reimburtype" id="reimburtype" value="${rmap.reimburtype }"/>
<html:hidden property="genReport" styleId="genReport" />

	<%int i=1; %>
<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class=wordtd_header>
	<td nowrap="nowrap"  style="text-align: center;">序号</td>	
	<td nowrap="nowrap"  style="text-align: center;">维保分部</td>
	<td nowrap="nowrap"  style="text-align: center;">维保站</td>
	<td nowrap="nowrap"  style="text-align: center;">报销人</td>
	<td nowrap="nowrap"  style="text-align: center;">身份证号</td>
	<td nowrap="nowrap"  style="text-align: center;">报销日期</td>
	
	<td nowrap="nowrap"  style="text-align: center;">报销范围</td>
	<td nowrap="nowrap"  style="text-align: center;">报销类型</td>
	<td nowrap="nowrap"  style="text-align: center;">维保合同号</td>
	<td nowrap="nowrap"  style="text-align: center;">费用所属维保站</td>
	<td nowrap="nowrap"  style="text-align: center;">费用所属分部门</td>
	
	<td nowrap="nowrap"  style="text-align: center;">报销金额(元)</td>
	<td nowrap="nowrap"  style="text-align: center;">备注</td>
	<td nowrap="nowrap"  style="text-align: center;">录入人</td>
	</tr>
	
	<logic:present name="maintenanceReportList" >
	  <logic:iterate id="ele" name="maintenanceReportList">
	  <tr class="inputtd" align="center" height="20">
	  <td><%=i++ %></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ComName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="StorageName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="UserName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="IdCardNo"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ReimbursDate"/></td>
	  
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="reimburrange"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="reimburtypeName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="maintcontractno"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="MaintStationBXName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="BelongsDepartName"/></td>
	  
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="bxMoney"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="rem"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="OperName"/></td>
	  </tr>
	  </logic:iterate>
	</logic:present>
	<logic:notPresent name="maintenanceReportList">
	  <tr class="noItems" align="center" height="20">
	  	<td colspan="11" >
	  		<bean:write name="showinfostr"/>
	  	</td>
	  </tr>
	</logic:notPresent>
</table>
<br/>
<b>
&nbsp;报销合计(元):<bean:write name="totalhmap" property="totalsum"/>
</b>
<br/>
		
</html:form>
