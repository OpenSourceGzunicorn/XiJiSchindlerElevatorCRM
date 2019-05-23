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
//按收款系数导出EXCEL
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

<html:form action="/ProjectBaoYangYingShouKuanAction.do?method=toSearchRecord">
<html:hidden property="property(flag)" styleId="flag" value="Y"/>
<html:hidden property="property(genReport)" styleId="genReport" value="Y"/>
<html:hidden property="property(startdate)" styleId="startdate" value="${search.startdate}"/>
<html:hidden property="property(enddate)" styleId="startdate" value="${search.enddate}"/>
<html:hidden property="property(contractid)" styleId="contractid" value="${search.contractid}"/>
<html:hidden property="property(custname)" styleId="custname" value="${search.custname}"/>
<html:hidden property="property(grcid)" styleId="grcid" value="${search.grcid}"/>
<html:hidden property="property(grcid22)" styleId="grcid22" value="${grcid22}"/>
</html:form>
<body style="font-size:3px;">
<table width="98%"  border="0" cellpadding="0" cellspacing="0" align="center">
  <tr>
    <td align="center" height="50">
    <div align="center"><font style="font-size:15px" color="blue">
	    <bean:write name="dateinfo" property="startdate"/>
	    <logic:match name="dateinfo" property="logicmatch" value="Y">至</logic:match>
	    <logic:match name="dateinfo" property="logicmatch" value="N"></logic:match>
	    <bean:write name="dateinfo" property="enddate"/>电梯保养应收款查询</font>
    </div>
    </td>
  </tr>
</table>

<table width="98%" height="58"  border="0" cellpadding="0" class="tb" cellspacing="1" align="center">
<tr>
    <td class="th" nowrap>序号</td>
    <td class="th" nowrap>合同号</td>
    <td class="th" nowrap>甲方单位</td>
    <td class="th" nowrap>应收日期</td>
    <td class="th" nowrap>应收金额</td>
    <td class="th" nowrap>已开票金额</td>
    <td class="th" nowrap>未开票金额</td>
    <td class="th" nowrap>所属分部</td>
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
  		<td nowrap align="center"><bean:write name="element" property="contractid"/></td>
  		<td nowrap align="center"><bean:write name="element" property="custname"/></td>
  		<td nowrap align="center"><bean:write name="element" property="predate"/></td>
  		<td nowrap align="center"><bean:write name="element" property="premoney"/></td>
  		<td nowrap align="center"><bean:write name="element" property="nowfee"/></td>
  		<td nowrap align="center"><bean:write name="element" property="nonowfee"/></td>
        <td nowrap align="center"><bean:write name="element" property="grcname"/></td>
  	</tr>
  </logic:iterate>
  </logic:present>
  <tr>
  	<td colspan="13">
  		<br>
		<div height="100">&nbsp;&nbsp;&nbsp;统计：记录数<b><%=i%></b>条,收款金额总计为：<b><bean:write name="countprice"/></b>（元）,已开票金额总计为：<b><bean:write name="nowfeeprice"/></b>（元),未开票金额总计为：<b><bean:write name="nonowfeeprice"/></b>（元）</div>
		<br>
  	</td>
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

