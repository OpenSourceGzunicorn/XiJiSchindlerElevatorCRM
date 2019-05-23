<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
	<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
	<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
  <link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
  <script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
  <link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
  <script type="text/javascript" src="<html:rewrite page="/common/javascript/highcharts/highcharts.js"/>"></script>


<br>
<html:form action="/inspectorReportAction.do?method=toSearchResults">

<input type="hidden" name="staffName" id="staffName" value="${rmap.staffName }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<html:hidden property="genReport" styleId="genReport" />
 
	<%int i=1; %>
<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class=wordtd_header>
	<td nowrap="nowrap"  style="text-align: center;">序号</td>	
	<td nowrap="nowrap"  style="text-align: center;">厂检人员姓名</td>
	<td nowrap="nowrap"  style="text-align: center;">流水号</td>
	<td nowrap="nowrap"  style="text-align: center;">提交厂检时间</td>
	<td nowrap="nowrap"  style="text-align: center;">厂检次数</td>
	<td nowrap="nowrap"  style="text-align: center;">项目名称</td>
	<td nowrap="nowrap"  style="text-align: center;">销售合同号</td>
	<td nowrap="nowrap"  style="text-align: center;">电梯编号</td>
	<td nowrap="nowrap"  style="text-align: center;">层</td>
	<td nowrap="nowrap"  style="text-align: center;">站</td>
	<td nowrap="nowrap"  style="text-align: center;">门</td>
	<td nowrap="nowrap"  style="text-align: center;">提升高度</td>
	<td nowrap="nowrap"  style="text-align: center;">厂检结果</td>
	</tr>
	<logic:present name="maintenanceReportList">
	  <logic:iterate id="ele" name="maintenanceReportList">
	  <tr class="inputtd" align="center" height="20">
	  <td><%=i++ %></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="staffName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="billno"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="checkTime"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="checkNum"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="projectName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="salesContractNo"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="elevatorNo"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="floor"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="stage"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="door"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="high"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="factoryCheckResult"/></td>
	  </tr>
	  </logic:iterate>
	  <tr class="inputtd" align="center" height="20">
	  	<td colspan="11"></td>
	  	<td nowrap="nowrap"  style="text-align: center;">厂检合格率</td>
	  	<td>${rate}%</td>
	  </tr>
	  	</logic:present>
	  	<logic:notPresent name="maintenanceReportList">
	  <tr class="noItems" align="center" height="20"><td colspan="13" >没有记录显示！</td></tr>
	  </logic:notPresent>
</table>
		
</html:form>
