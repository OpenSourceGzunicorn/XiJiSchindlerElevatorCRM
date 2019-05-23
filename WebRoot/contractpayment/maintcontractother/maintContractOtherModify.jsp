<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<br>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
  <html:form action="/maintContractOtherAction.do?method=toUpdateRecord">
  
   <input type="hidden" name="hidsta" id="hidsta" value="update"/>
   <html:hidden property="isreturn"/>

<logic:present name="masterhmap">
<html:hidden name="masterhmap" property="billno" styleId="billno"/>

	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
		<tr>
			<td class="wordtd" width="15%">维保分部：</td>
			<td width="15%"><bean:write name="masterhmap" property="maintdivision"/></td>
			<td class="wordtd" width="15%">维保站：</td>
			<td width="15%"><bean:write name="masterhmap" property="maintstation"/></td>
			<td class="wordtd" width="15%">开票名称：</td>
			<td width="20%"><bean:write name="masterhmap" property="invoicename"/></td>
		</tr>
		<tr>
			<td class="wordtd">维保合同号：</td>
			<td><bean:write name="masterhmap" property="maintcontractno"/></td>
			<td class="wordtd">业务费总额：</td>
			<td><bean:write name="masterhmap" property="otherfee"/></td>
			<td class="wordtd">未支出金额 ：</td>
			<td>
				<bean:write name="masterhmap" property="nototherfee"/>
				<input type="hidden" name="nototherfee" id="nototherfee" value="${masterhmap.nototherfee}"/>
			</td>
		</tr>
	</table>
	<br/>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
		<tr>
			<td class="wordtd" width="15%">支出日期：</td>
			<td width="15%">
			<html:text property="paydate" styleId="paydate" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" /><font color="red">*</font></td>
			<td class="wordtd" width="15%">支出金额：</td>
			<td width="15%"><html:text property="paymoney" styleId="paymoney" styleClass="default_input" onkeypress="f_number();"/><font color="red">*</font></td>
			<td class="wordtd" width="15%">&nbsp;</td>
			<td width="20%">&nbsp;</td>
		</tr>
		<tr>
			<td class="wordtd" width="15%">备注：</td>
			<td colspan="5"><html:textarea property="rem" styleId="rem" cols="70" rows="3"/></td>
		</tr>
		<tr>
			<td class="wordtd" width="15%">录入人：</td>
			<td width="15%"><bean:write name="masterhmap" property="operid"/></td>
			<td class="wordtd" width="15%">录入日期：</td>
			<td width="15%"><bean:write name="masterhmap" property="operdate"/></td>
			<td class="wordtd" width="15%">&nbsp;</td>
			<td width="20%">&nbsp;</td>
		</tr>
	</table>
	
	
	<br/>
	<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
	  <b>&nbsp;业务费支出明细</b>
	</div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
		<thead>
			<tr class="wordtd_header">
				<td width="15%">支出日期</td>
				<td width="15%">支出金额</td>
				<td width="15%">录入人</td>
				<td width="15%">录入日期</td>
				<td width="40%">备注</td>
			</tr>
		</thead>
		<tfoot>
			<tr height="15"><td colspan="5"></td></tr>
		</tfoot>
		<tbody>
			<logic:present name="mcolist">
	          <logic:iterate id="pro" name="mcolist" >
	          	<tr>
					<td align="center"><bean:write name="pro" property="paydate"/></td>
					<td align="center"><bean:write name="pro" property="paymoney"/></td>
					<td align="center"><bean:write name="pro" property="operid"/></td>
					<td align="center"><bean:write name="pro" property="operdate"/></td>
					<td><bean:write name="pro" property="rem"/></td>
				</tr>
	          </logic:iterate>
	        </logic:present>
		</tbody>
	</table>
	<br/>

</logic:present>
  </html:form>
</body>