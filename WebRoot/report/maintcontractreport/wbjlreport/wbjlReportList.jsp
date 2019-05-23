<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
  <script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
  <link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">

<br>
<html:form action="/wbjlReportAction.do?method=toSearchResults">

<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="maintcontractno" id="maintcontractno" value="${rmap.maintcontractno }"/>
<input type="hidden" name="salescontractno" id="salescontractno" value="${rmap.salescontractno }"/>
<input type="hidden" name="elevatorno" id="elevatorno" value="${rmap.elevatorno }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<input type="hidden" name="sdate2" id="sdate2" value="${rmap.sdate2 }"/>
<input type="hidden" name="edate2" id="edate2" value="${rmap.edate2 }"/>
<html:hidden property="genReport" styleId="genReport" />

	<%int i=1; %>
<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class=wordtd_header>
		<td nowrap="nowrap"  style="text-align: center;">序号</td>
		<td nowrap="nowrap"  style="text-align: center;">维保分部</td>
		<td nowrap="nowrap"  style="text-align: center;">维保站</td>
		<td nowrap="nowrap"  style="text-align: center;">维保合同号</td>
		<td nowrap="nowrap"  style="text-align: center;">销售合同号</td>
		<td nowrap="nowrap"  style="text-align: center;">电梯编号</td>
		<td nowrap="nowrap"  style="text-align: center;">电梯类型</td>
		<td nowrap="nowrap"  style="text-align: center;">层站</td>
		<td nowrap="nowrap"  style="text-align: center;">保养费金额</td>
		<td nowrap="nowrap"  style="text-align: center;">变更方式</td>
		<td nowrap="nowrap"  style="text-align: center;">有效期</td>
		<td nowrap="nowrap"  style="text-align: center;">价格调整幅度</td>
		<td nowrap="nowrap"  style="text-align: center;">延误与否</td>
		<td nowrap="nowrap"  style="text-align: center;">折扣率</td>
		<td nowrap="nowrap"  style="text-align: center;">销售奖金</td>
		<td nowrap="nowrap"  style="text-align: center;">来款日期</td>
		<td nowrap="nowrap"  style="text-align: center;">来款录入日期</td>
		<td nowrap="nowrap"  style="text-align: center;">来款金额</td>
		<td nowrap="nowrap"  style="text-align: center;">来款比例</td>
		<td nowrap="nowrap"  style="text-align: center;">实际奖金</td>
	</tr>
	
	<logic:present name="maintenanceWbjlReportList" >
	  <logic:iterate id="ele" name="maintenanceWbjlReportList" indexId="elexh">
	  <tr class="inputtd" align="center" height="20">
		  <td>${elexh+1 }</td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="comname"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="storagename"/></td>
		  <td nowrap="nowrap"  style="text-align: center;">
		  	<a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="ele"  property="billno"/>" target="_blnk"><bean:write name="ele" property="maintcontractno"/></a>
		  </td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="salescontractno"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="elevatorno"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="elevatortypename"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="floorstage"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="bymoney"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="signwayname"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="contractperiod"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="jgtzfd2"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ywyf"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="zkl"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="xsjj"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="lkdate"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="lkdate1"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="lkmoney"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="lkbl"/></td>
		  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="sjjj"/></td>

	  </tr>
	  </logic:iterate>
	</logic:present>
	<logic:notPresent name="maintenanceWbjlReportList">
	  <tr class="noItems" align="center" height="20">
	  	<td colspan="19" >
	  		<bean:write name="showinfostr"/>
	  	</td>
	  </tr>
	</logic:notPresent>
</table>
<br/>
<b>
&nbsp;合计&nbsp;&nbsp;实际奖金(元):<bean:write name="totalhmap" property="totalmoney"/>
</b>
<br/>
		
</html:form>

