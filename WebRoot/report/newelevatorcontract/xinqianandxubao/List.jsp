<%@ page contentType="text/html;charset=gb2312" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<script language="javascript" src="<html:rewrite forward="jq.js"/>"></script>
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
function toExcelRecord2(){
    document.serveTableForm.flag.value="Y";
    document.serveTableForm.genReport.value="C";
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
<html:form action="/XinQianAndXuBaoAction.do?method=toSearchRecord&">
<html:hidden property="property(flag)" styleId="flag" value="Y"/>
<html:hidden property="property(genReport)" styleId="genReport" value="Y"/>
<html:hidden property="property(contractid)" styleId="contractid" value="${contractid}"/>
<html:hidden property="property(contractstatus)" styleId="contractstatus" value="${contractstatus}"/>
<html:hidden property="property(custname)" styleId="custname" value="${custname}"/>
<html:hidden property="property(elevatorno)" styleId="elevatorno" value="${elevatorno}"/>
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
保养科新签与续保明细</font></div></td>
  </tr>
</table>

<table width="98%" height="58"  border="0" cellpadding="0" class="tb" cellspacing="1" align="center">
<tr >
    <td class="th" nowrap>序号</td>
    <td class="th" nowrap>合同号</td>
    <td class="th" nowrap>站别</td>
    <td class="th" nowrap>甲方单位</td>
    <td class="th" nowrap>电梯编号</td>
    <td class="th" nowrap>保养月份</td>
    <td class="th" nowrap>合同总额</td>
    <td class="th" nowrap>开始日期</td>
    <td class="th" nowrap>到期日期</td>
    <td class="th" nowrap>所属分部</td>
  </tr>
  <%
  int i=0;
  //保存单价
  %>
  <logic:present name="report_list" >
  <logic:iterate id="element" name="report_list">
  	<tr style="font-size:12px;border:1px;">
  	<%
  		i++;
  	%>
  		<td nowrap align="center"><%=i%></td>
  		<td nowrap align="center"><bean:write name="element" property="contractid"/></td>
  		<td nowrap align="center"><bean:write name="element" property="nodename"/></td>
  		<td nowrap align="center"><bean:write name="element" property="custname"/></td>
  		<td nowrap align="center"><bean:write name="element" property="elevatorno"/></td>
  		<td nowrap align="center"><bean:write name="element" property="r18"/></td>
  		<td nowrap align="right"><bean:write name="element" property="allprice"/></td>
  		<td nowrap align="center"><bean:write name="element" property="mugstartdate"/></td>
  		<td nowrap align="center"><bean:write name="element" property="mugenddate"/></td>
  		<td nowrap align="center"><bean:write name="element" property="grcname"/></td>
  	</tr>
  </logic:iterate>
  </logic:present>
</table>
<br>
<br>
<div align="center">
<input type="button" name="toExcelRecord" value="按合同导出EXCEL" onclick="toExcelRecord2()"/>&nbsp;
<input type="button" name="toExcelRecord" value="导出Excel" onclick="toExcelRecord()"/>&nbsp;
<input type="button" value="  关闭  " onclick="javascript:window.close()";>
</div>
<br>
</body>
</html>
<script>
$(document).ready(function(){
	var list=${report_list};
	if(list==''){
		var tr=$(".tb").children().children().eq(0);
		tr.after("<tr><td nowrap align='center'colspan='10'><b>无记录！</b></td></tr>")
	}
})
</script>

