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
<html:form action="/flatBillContractReportAction.do?method=toSearchResults">
<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="contractno" id="contractno" value="${rmap.contractno }"/>
<input type="hidden" name="companyid" id="companyid" value="${rmap.companyid }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<html:hidden property="genReport" styleId="genReport" />

	<%int i=1; %>
<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class=wordtd_header>
	<td nowrap="nowrap"  style="text-align: center;">序号</td>	
	<td nowrap="nowrap"  style="text-align: center;">平账日期</td>
	<td nowrap="nowrap"  style="text-align: center;">维保分部</td>
	<td nowrap="nowrap"  style="text-align: center;">维保站</td>
	<td nowrap="nowrap"  style="text-align: center;">合同号</td>
	<td nowrap="nowrap"  style="text-align: center;">台量</td>
	<td nowrap="nowrap"  style="text-align: center;">甲方单位</td>
	<td nowrap="nowrap"  style="text-align: center;">合同开始日期</td>
	<td nowrap="nowrap"  style="text-align: center;">合同结束日期</td>
	<td nowrap="nowrap"  style="text-align: center;">合同欠款</td>
	</tr>
	
	<logic:present name="maintenancefbcReportList" >
	  <logic:iterate id="ele" name="maintenancefbcReportList">
	  <tr class="inputtd" align="center" height="20">
	  <td><%=i++ %></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ParagraphDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ComName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="StorageName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;">
	  	<logic:equal name="ele" property="BusType" value="B">
	  		<a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="ele"  property="BillNo"/>" target="_blnk"><bean:write name="ele" property="ContractNo"/></a>
	  	</logic:equal>
	  	<logic:notEqual name="ele" property="BusType" value="B">
	  		<a href="<html:rewrite page='/wgchangeContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="ele"  property="BillNo"/>" target="_blnk"><bean:write name="ele" property="ContractNo"/></a>
	 	</logic:notEqual>
	  </td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="num"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="CompanyName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ContractSDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ContractEDate"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ContractTotal2"/></td>
	  </tr>
	  </logic:iterate>
	</logic:present>
	<logic:notPresent name="maintenancefbcReportList">
	  <tr class="noItems" align="center" height="20">
	  	<td colspan="10" >
	  		<bean:write name="showinfostr"/>
	  	</td>
	  </tr>
	</logic:notPresent>
</table>
<br/>
		
</html:form>
