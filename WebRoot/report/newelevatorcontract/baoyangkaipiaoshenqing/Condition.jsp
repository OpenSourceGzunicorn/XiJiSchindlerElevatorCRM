<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.HashMap"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='CalendarCSS2'/>">
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<style type="text/css">
<!--
.add{font-family:Arial,"宋体";
    font-size:9pt;
	font-weight:bold;
	color: #ffffff;
	background-color:#9E0B0E;
	padding-right:13px;
	padding-bottom:2px;
	padding-left:13px;
	}
body {
	background-color: #F4F2F5;
}
.style5 {
	color: #9e0b0e;
	font-weight: bold;
}
input
{
	border:1px solid #666666;
	height:18px;
	font-size:12px;
}
.leftbottom { BORDER-BOTTOM: #666666 1px solid; BORDER-LEFT: #666666 1px solid;BORDER-top: #666666 1px solid; BORDER-right: #666666 1px solid; background-image:url(images/botton_bg.gif); padding-top:2px; height:20px;}
.bottom { BORDER-BOTTOM: #666666 1px solid; BORDER-LEFT: #666666 1px solid;BORDER-top: #666666 1px solid; BORDER-right: #666666 1px solid;padding-top:3px; background-color:#D4D0C8; height:20px;}
-->
</style>
<html:form action="/ProjectBaoYangKaiPiaoShenQingSearchAction.do?method=toSearchRecord">
	<html:hidden property="property(genReport)" styleId="genReport" value="" />
	<table width="98%" border="0" bgcolor="#000000" cellpadding="0"
				cellspacing="0" class="tb">
		<tr>
			<td width="20%" height="28" class="wordtd">
				<div align="right">合同编号:</div>
			</td>
			<td  width="80%">
				<html:text property="property(contractid)" />
			</td>
		</tr>
		 
		<tr>
			<td width="20%" height="28" class="wordtd">
				<div align="right">应收款日期</div>
			</td>
			<td  width="80%">
				<html:text property="property(predates)" styleClass="Wdate" onclick="setday(this)" readonly="true" />
				至:
				<html:text property="property(predatee)" styleClass="Wdate" onclick="setday(this)" readonly="true" />
			</td>
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">
				<div align="right">发票号码:</div>
			</td>
			<td  width="80%">
				<html:text property="property(invoiceno)" />
			</td>
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">
				<div align="right">开票申请日期:</div>
			</td>
			<td  width="80%">
				<html:text property="property(date2s)" styleClass="Wdate" onclick="setday(this)" readonly="true" />
				至:
				<html:text property="property(date2e)" styleClass="Wdate" onclick="setday(this)" readonly="true" />
			</td>
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">
				<div align="right">付款方名称:</div>
			</td>
			<td  width="80%">
				<html:text property="property(custname)" />
			</td>
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">
			<div align="right">所属维保分部:</div>
			</td>
			<td ><html:select
				property="property(grcid)">
				<html:options collection="grcidlist" property="grcid"
					labelProperty="grcname"></html:options>
			</html:select></td>
		</tr>
	</table>
</html:form>


