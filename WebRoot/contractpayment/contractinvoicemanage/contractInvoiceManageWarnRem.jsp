<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<head>
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
  <html:form action="/contractInvoiceManageAction.do?method=toWarnRecord">
     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
		<tr>
			<td class="wordtd" >应收款流水号:</td>
			<td><html:hidden property="jnlNo" write="true"/></td>
		</tr>
		<tr>
			<td class="wordtd">合同号:</td>
			<td><html:hidden property="contractNo" write="true"/></td>
		</tr>
		<tr>
			<td class="wordtd">提醒备注:</td>
			<td><html:text property="warnRem" styleClass="default_input" size="60"/></td>
		</tr>
	</table>
	<br/>
  </html:form>
</body>