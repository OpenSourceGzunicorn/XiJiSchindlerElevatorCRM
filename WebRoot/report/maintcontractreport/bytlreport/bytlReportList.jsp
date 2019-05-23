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
<html:form action="/bytlReportAction.do?method=toSearchResults">

<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="maintcontractno" id="maintcontractno" value="${rmap.maintcontractno }"/>
<input type="hidden" name="elevatorno" id="elevatorno" value="${rmap.elevatorno }"/>
<input type="hidden" name="salescontractno" id="salescontractno" value="${rmap.salescontractno }"/>
<input type="hidden" name="maintpersonnel" id="maintpersonnel" value="${rmap.maintpersonnel }"/>
<%-- 
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
--%>
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
		<td nowrap="nowrap"  style="text-align: center;">层站系数</td>
		<td nowrap="nowrap"  style="text-align: center;">扶梯类型</td>
		<td nowrap="nowrap"  style="text-align: center;">扶梯系数</td>
		<td nowrap="nowrap"  style="text-align: center;">载重</td>
		<td nowrap="nowrap"  style="text-align: center;">载重系数</td>
		
		<logic:present name="ElevatorNatureList" >
		<logic:iterate id="enl" name="ElevatorNatureList">
		<%-- background-color: yellow; --%>
			<td nowrap="nowrap" style="text-align: center;">${enl.pullname}</td>
		</logic:iterate>
		</logic:present>
		
		<td nowrap="nowrap"  style="text-align: center;">台量</td>
		<td nowrap="nowrap"  style="text-align: center;">台量系数</td>
		<td nowrap="nowrap"  style="text-align: center;">台量奖(元)</td>
		<td nowrap="nowrap"  style="text-align: center;">维保工身份证号</td>
		<td nowrap="nowrap"  style="text-align: center;">维保工</td>
		<td nowrap="nowrap"  style="text-align: center;">交接是否完成</td>
		<td nowrap="nowrap"  style="text-align: center;">是否发台量奖</td>
		<%-- td nowrap="nowrap"  style="text-align: center;">下达日期</td--%>
	</tr>
	
	<logic:present name="maintenanceBytlReportList" >
	  <logic:iterate id="ele" name="maintenanceBytlReportList" indexId="elexh">
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
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="floorstagexs"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="seriesidname"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="seriesidxs"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="weight"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="weightxs"/></td>
		
	  <logic:present name="ElevatorNatureList" >
		<logic:iterate id="enl2" name="ElevatorNatureList">
			<logic:equal name="enl2" property="id.pullid" value="${ele.elevatornature }">
				<td nowrap="nowrap"  style="text-align: center;">${ele.elevatornaturexs }</td>
			</logic:equal>
			<logic:notEqual name="enl2" property="id.pullid" value="${ele.elevatornature }">
				<td nowrap="nowrap"  style="text-align: center;">&nbsp;</td>
			</logic:notEqual>
		</logic:iterate>
		</logic:present>
		
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="tlnum"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="tlxs"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="nummoney"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="idcardno"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="maintpersonnelname"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="transfercomplete"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="r4"/></td>
	  <%--td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="tasksubdate"/></td--%>
	  </tr>
	  </logic:iterate>
	</logic:present>
	<logic:notPresent name="maintenanceBytlReportList">
	  <tr class="noItems" align="center" height="20">
	  	<td colspan="${colnum }" >
	  		<bean:write name="showinfostr"/>
	  	</td>
	  </tr>
	</logic:notPresent>
</table>
<br/>
<b>
&nbsp;台量奖合计(元):<bean:write name="totalhmap" property="totalmoney"/>
</b>
<br/>
		
</html:form>

