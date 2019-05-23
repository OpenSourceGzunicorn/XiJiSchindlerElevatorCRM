<%@ page contentType="text/html;charset=gb2312" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<html>
<head>
<script language="javascript">
function toExcelRecord(){
    document.serveTableForm.flag.value="Y";
    document.serveTableForm.genReport.value="Y";
	document.serveTableForm.target = "_blank";
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
<html:form action="/BaoYangQingKuangTotalAction.do?method=toSearchRecord">
<html:hidden property="property(flag)" styleId="flag" value="Y"/>
<html:hidden property="property(genReport)" styleId="genReport" value="Y"/>
<html:hidden property="property(selectyear)" styleId="selectyear" value="${selectyear}"/>
<html:hidden property="property(selectmonth)" styleId="selectmonth" value="${selectmonth}"/>
<html:hidden property="property(mugStorages)" styleId="mugStorages" value="${mugStorages}"/>
<html:hidden property="property(grcid22)" styleId="grcid22" value="${grcid22}"/>
</html:form>
<body style="font-size:3px;">

<br>
<br>
<br>


<table width="98%"  border="0" cellpadding="0" cellspacing="0" align="center">
  <tr>
    <td align="center" height="50"><div align="center"><font style="font-size:15px" color="blue">
    <bean:write name="dateinfo" property="selectyear"/><logic:match name="dateinfo" property="currentyear" value="Y">年</logic:match><bean:write name="dateinfo" property="selectmonth"/><logic:match name="dateinfo" property="currentmonth" value="Y">月</logic:match>电梯保养情况汇总表</font></div></td>
  </tr>
</table>

<table width="98%" height="58"  border="0" cellpadding="0" class="tb" cellspacing="1" align="center">
<tr>
    <td class="th" nowrap rowspan="2" colspan="3">项目</td>
    <td class="th" nowrap colspan="6">本月合同情况</td>
    <td class="th" nowrap colspan="8">本月保养电梯情况</td>
</tr>
<tr>
    <td class="th" nowrap colspan="2">新签定保养合同</td>
    <td class="th" nowrap colspan="2">退保养合同</td>
    <td class="th" nowrap colspan="2">续签定保养合同</td>
    <td class="th" nowrap colspan="2">自保电梯</td>
    <td class="th" nowrap colspan="2">发包保养电梯</td>
    <td class="th" nowrap colspan="2">三包电梯</td>
    <td class="th" nowrap colspan="2">小计</td>

</tr>
<tr>
    <td class="th" nowrap>序号</td>
    <td class="th" nowrap>站别</td>
    <td class="th" nowrap>所属分部</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    <td class="th" nowrap>台数</td>
    <td class="th" nowrap>金额</td>
    
     
  </tr>
  <%
  int i=0;
  //保存单价
  %>
  <logic:present name="report_list">
  <logic:iterate id="element" name="report_list">
  	<tr style="font-size:12px;border:1px;">
  	<%
  		i++;
  	%>
  	    <td nowrap align="center"><%=i%></td>
  	    <td nowrap align="center"><bean:write name="element" property="nodename"/></td>
  	    <td nowrap align="center"><bean:write name="element" property="grcname"/></td>
  	    <td nowrap align="center"><bean:write name="element" property="signnum"/></td> 	    
  	    <td nowrap align="right"><bean:write name="element" property="signPrice"/></td>
  	    <td nowrap align="center"><bean:write name="element" property="backNum"/></td>
  	    <td nowrap align="right"><bean:write name="element" property="backPrice"/></td>
  	    <td nowrap align="center"><bean:write name="element" property="continueNum"/></td>
  	    <td nowrap align="right"><bean:write name="element" property="continuePrice"/></td>
  	    <td nowrap align="center"><bean:write name="element" property="zbfNum"/></td>
  	    <td nowrap align="right"><bean:write name="element" property="zbPrice"/></td>
  	    <td nowrap align="center"><bean:write name="element" property="fbNum"/></td>
  	    <td nowrap align="right"><bean:write name="element" property="fbPrice"/></td>
  	    <td nowrap align="center"><bean:write name="element" property="sbNum"/></td>
  	    <td nowrap align="right"><bean:write name="element" property="sbPrice"/></td>
  	    <td nowrap align="center"><bean:write name="element" property="xjNum"/></td>
  	    <td nowrap align="right"><bean:write name="element" property="xjnPrice"/></td>		
  	</tr>
  </logic:iterate>
  </logic:present>
  <tr>
  	<td colspan="3" align="center"><font size="5">保养总计</font></td>
  	<td align="center">${totalsignnum}</td>
  	<td align="right">${totalsignPrice}</td>
  	<td align="center">${totalbackNum}</td>
  	<td align="right">${totalbackPrice}</td>
  	<td align="center">${totalcontinueNum}</td>
  	<td align="right">${totalcontinuePrice}</td>
  	<td align="center">${totalzbfNum}</td>
  	<td align="right">${totalzbPrice}</td>
  	<td align="center">${totalfbNum}</td>
  	<td align="right">${totalfbPrice}</td>
  	<td align="center">${totalsbNum}</td>
  	<td align="right">${totalsbPrice}</td> 
  	<td align="center">${totalxjNum}</td>
  	<td align="right">${totalxjnPrice}</td> 	
  </tr>
  <tr>
  	<td colspan="17">
  		<br>
		<div height="100">&nbsp;&nbsp;&nbsp;统计：记录数<b><%=i%></b>条</div>
		<br>
  	</td>
  </tr>
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

