<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<html>
<head>
<script language="javascript">
function toExcelRecord(){
	document.serveTableForm.target = "_blank";
    document.serveTableForm.genReport.value="Y";
	document.serveTableForm.submit();
}
</script>
<title></title>
</head>
<style type="text/css">
	.th{
		font-size: 12px;
		text-align: center;
		vertical-align:top;
		padding-top: 6px;
		padding-right: 5px;
		padding-bottom: 3px;
		padding-left: 5px;
		background-color: #b8c4f4;
	}
</style>
<html:form action="/NewSignBusinessSummaryTableReportSearchAction.do?method=toSearchRecord">

<html:hidden property="property(genReport)" styleId="genReport" />
<%--html:hidden property="property(contractid)" value="${conditionmap.contractid }" /--%>
<html:hidden property="property(lotdates)" value="${conditionmap.lotdates }" />
<html:hidden property="property(lotdatee)" value="${conditionmap.lotdatee }" />
<html:hidden property="property(username)" value="${conditionmap.username }" />
<html:hidden property="property(grcid)" value="${conditionmap.grcid }" />
<%--html:hidden property="property(custname)" value="${conditionmap.custname }" /--%>
<%--html:hidden property="property(nodeid)" value="${conditionmap.nodeid }" /--%>
</html:form>

<body style="font-size:3px;">

<br>
<br>
<br>

<table width="98%"  border="0" cellpadding="0" cellspacing="0" align="center">
  <tr>
    <td align="center" height="50">
	    <div align="center">
	    <font style="font-size:15px" color="blue">
		   <b> 新签业务汇总表	    
		  	<logic:present name="conditionmap">
			    <logic:equal name="conditionmap" property="timerange" value="Y">
			    	(${conditionmap.lotdates }至${conditionmap.lotdatee })
			    </logic:equal>
		    </logic:present>
		   </b>
		</font>
		</div>
    </td>
  </tr>
</table>

<table width="98%" height="58"  border="0" cellpadding="0" class="tb" cellspacing="1" align="center">
  <tr>
    <td rowspan="2" class="th" nowrap valign="bottom">序号</td>
    <td rowspan="2" class="th" nowrap valign="bottom">业务员</td>
    <td rowspan="2" class="th" nowrap valign="bottom">所属维保分部</td>  
    <td colspan="2" class="th" nowrap>新签订维修合同</td>
    <td colspan="2" class="th" nowrap>新签订改造合同</td>
    <td colspan="2" class="th" nowrap>新签订加装合同</td>
    <td colspan="2" class="th" nowrap>新签定保养合同</td>
  </tr>  
  <tr>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
  </tr>
  
<logic:present name="resultList">
<logic:iterate id="element" name="resultList">
  <tr style="font-size:12px;border:1px;">
  		<td nowrap align="center"><bean:write name="element" property="xuhao"/></td>
  		<td nowrap align="center"><bean:write name="element" property="username"/></td>
  		<td nowrap align="center"><bean:write name="element" property="grcname"/></td>
  		<td nowrap align="center"><bean:write name="element" property="wnum"/></td>
  		<td nowrap align="center"><bean:write name="element" property="wamt"/></td>
  		<td nowrap align="center"><bean:write name="element" property="gnum"/></td>
  		<td nowrap align="center"><bean:write name="element" property="gamt"/></td>
  		<td nowrap align="center"><bean:write name="element" property="jnum"/></td>
  		<td nowrap align="center"><bean:write name="element" property="jamt"/></td>
  		<td nowrap align="center"><bean:write name="element" property="bnum"/></td>
  		<td nowrap align="center"><bean:write name="element" property="bamt"/></td>
  </tr>
</logic:iterate>
</logic:present>
</table>

<br>
<br>
<div align="center">
<input type="button" name="toExcelRecord" value="导出Excel" onclick="toExcelRecord()"/>&nbsp;
<input type="button" value="  关闭  " onclick="javascript:window.close()";>
</div>
<br>
</body>
</html>


