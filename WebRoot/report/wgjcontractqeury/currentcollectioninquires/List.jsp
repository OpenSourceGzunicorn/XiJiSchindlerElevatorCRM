<%@ page contentType="text/html;charset=gb2312" %>
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

<html:form action="/CurrentCollectionInquiresReportSearchAction.do?method=toSearchRecord">

<html:hidden property="property(genReport)" styleId="genReport" />
<html:hidden property="property(contractid)" value="${conditionmap.contractid }" />
<html:hidden property="property(getheringdates)" value="${conditionmap.getheringdates }" />
<html:hidden property="property(getheringdatee)" value="${conditionmap.getheringdatee }" />
<html:hidden property="property(custname)" value="${conditionmap.custname }" />
<html:hidden property="property(username)" value="${conditionmap.username }" />
<html:hidden property="property(nodeid)" value="${conditionmap.nodeid }" />
<html:hidden property="property(grcid)" value="${conditionmap.grcid }" />
</html:form>

<body style="font-size:3px;">

<br>
<br>
<br>


<table width="98%"  border="0" cellpadding="0" cellspacing="0" align="center">
  <tr>
    <td align="center" height="50">
	    <div align="center"><font style="font-size:15px" color="blue">
		   <b>当期收款查询报表</b>
		</font></div>
    </td>
  </tr>
</table>

<table width="98%" height="58"  border="0" cellpadding="0" class="tb" cellspacing="1" align="center">
  <tr>
    <td class="th" nowrap>序号</td>
    <td class="th" nowrap>收款日期</td>
    <td class="th" nowrap>应收款日期</td>
    <td class="th" nowrap>合同类别</td>
    <td class="th" nowrap>合同号</td>
    <td class="th" nowrap>单位名称</td>
    <td class="th" nowrap>合同总金额</td>
    <td class="th" nowrap>收款金额</td>
    <td class="th" nowrap>所属维保分部</td>          
  </tr>
<logic:present name="resultList">
<logic:iterate id="element" name="resultList">
  <tr style="font-size:12px;border:1px;">
  		<td nowrap align="center"><bean:write name="element" property="xuhao"/></td>  		
  		<td nowrap align="center"><bean:write name="element" property="getheringdate"/></td>  		
  		<td nowrap align="center"><bean:write name="element" property="predate"/></td>
  		<td nowrap align="center"><bean:write name="element" property="contracttypename"/></td>
  		<td nowrap align="center"><bean:write name="element" property="contractid"/></td> 
  		<td nowrap align="center"><bean:write name="element" property="custname"/></td>
  		<td nowrap align="center"><bean:write name="element" property="contractfee"/></td>
  		<td nowrap align="center"><bean:write name="element" property="realfee"/></td>
  		<td nowrap align="center"><bean:write name="element" property="grcname"/></td>
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

