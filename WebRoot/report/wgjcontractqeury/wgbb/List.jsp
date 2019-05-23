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

<html:form action="/completeStatementSearchAction.do?method=toSearchRecord">

<html:hidden property="property(genReport)" styleId="genReport" />
<html:hidden property="property(lotdates)" value="${conditionmap.lotdates }" />
<html:hidden property="property(lotdatee)" value="${conditionmap.lotdatee }" />
<html:hidden property="property(completes)" value="${conditionmap.completes }" />
<html:hidden property="property(completee)" value="${conditionmap.completee}" />
<html:hidden property="property(grcid)" value="${conditionmap.grcid }" />
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
		   <b> 完工报表    
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

<table width="98%" height="58"  border="0" cellpadding="0" class="tb" cellspacing="0" align="center">
  <tr>
    <td class="th" nowrap width="5%">序号</td>
    <td class="th" nowrap width="20%">合同号</td>
    <td class="th" nowrap width="30%">委托单位（维修项目）</td>
    <td class="th" nowrap width="5%">台数</td>
    <td class="th" nowrap width="10%">产值（元）</td>
    <td class="th" nowrap width="10%">完工日期</td>
    <td class="th" nowrap width="10%">所属维保分部</td>  
  </tr>   
  
<logic:present name="wtempList">
<logic:iterate id="element" name="wtempList" indexId="rowid">
  <tr style="font-size:12px;">
  		<td nowrap align="center">${rowid+1}</td>
  		<td nowrap align="center"><bean:write name="element" property="contractid"/></td>
  		<td nowrap align="center"><bean:write name="element" property="custname"/></td>
  		<td nowrap align="center"><bean:write name="element" property="num"/></td>
  		<td nowrap align="center"><bean:write name="element" property="collectMon"/></td>
  		<td nowrap align="center"><bean:write name="element" property="finishdate"/></td>

  		<td nowrap align="center"><bean:write name="element" property="grcname"/></td>
  </tr>
</logic:iterate>
</logic:present>

<tr>
<td colspan="3" style="font-size: 14px; text-align: center;">合计：</td>
<td nowrap align="center">${wnum}</td>
 <td nowrap align="center">${wprice}</td>
 <td nowrap align="center"></td>
 <td nowrap align="center"></td>
</tr>
</table>

<br>
<table width="98%" height="58"  border="0" cellpadding="0" class="tb" cellspacing="0" align="center">
  <tr>
    <td class="th" nowrap width="5%">序号</td>
    <td class="th" nowrap width="20%">合同号</td>
    <td class="th" nowrap width="30%">改造项目</td>
    <td class="th" nowrap width="5%">台数</td>
    <td class="th" nowrap width="10%">产值（元）</td>
    <td class="th" nowrap width="10%">完工日期</td>
    <td class="th" nowrap width="10%">所属部门分部</td>  
  </tr>   
  
<logic:present name="gtempList">
<logic:iterate id="element" name="gtempList" indexId="rowid">
  <tr style="font-size:12px;">
  		<td nowrap align="center">${rowid+1}</td>
  		<td nowrap align="center"><bean:write name="element" property="contractid"/></td>
  		<td nowrap align="center"><bean:write name="element" property="custname"/></td>
  		<td nowrap align="center"><bean:write name="element" property="num"/></td>
  		<td nowrap align="center"><bean:write name="element" property="collectMon"/></td>
  		<td nowrap align="center"><bean:write name="element" property="finishdate"/></td>
  		<td nowrap align="center"><bean:write name="element" property="grcname"/></td> 		
  </tr>
</logic:iterate>
</logic:present>

<tr>
<td colspan="3" style="font-size: 14px; text-align: center;">合计：</td>
<td nowrap align="center">${gnum}</td>
 <td nowrap align="center">${gprice}</td>
 <td nowrap align="center"></td>
</tr>
</table>

<br>
<div align="center">
<input type="button" name="toExcelRecord" value="导出Excel" onclick="toExcelRecord()"/>&nbsp;
<input type="button" value="  关闭  " onclick="javascript:window.close()";>
</div>
<br>
</body>
</html>


