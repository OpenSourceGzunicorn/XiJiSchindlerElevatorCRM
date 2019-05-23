<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<br>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
<html:form action="/accountsPayableAuditAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden property="auditStatus"/>
<logic:present name="accountsPayableAuditBean">
<html:hidden name="accountsPayableAuditBean" property="jnlNo"/>
<html:hidden name="accountsPayableAuditBean" property="billNo"/>
<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">委托合同号：</td>
		<td width="20%">
		<a onclick="simpleOpenWindow('returningMaintainDetailAction','${accountsPayableAuditBean.entrustContractNo}');" class="link"><bean:write name="accountsPayableAuditBean" property="entrustContractNo"/></a>
		
		</td>
		<td class="wordtd">委托单位名称：</td>
		<td width="20%"><bean:write name="accountsPayableAuditBean" property="companyId"/></td>
		<td nowrap="nowrap" class="wordtd">所属分部：</td>
		<td width="20%"><bean:write name="accountsPayableAuditBean" property="maintDivision"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">合同总额：</td>
		<td width="20%"><bean:write name="contractTotal"/></td>
		<td class="wordtd">已建应收款金额：</td>
		<td width="20%"><bean:write name="builtReceivables"/></td>
		<td class="wordtd">未建应收款金额：</td>
		<td width="20%"><bean:write name="noBuiltReceivables"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd">应收款名称：</td>
		<td width="20%"><bean:write name="accountsPayableAuditBean" property="recName"/></td>
		<td class="wordtd">应收款金额：</td>
		<td width="20%"><bean:write name="accountsPayableAuditBean" property="preMoney"/></td>
		<td class="wordtd">应收款日期：</td>
		<td width="20%"><bean:write name="accountsPayableAuditBean" property="preDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><bean:write name="accountsPayableAuditBean" property="rem"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">录入人：</td>
		<td width="30%"><bean:write name="accountsPayableAuditBean" property="operId"/></td>
		<td nowrap="nowrap" class="wordtd">录入日期：</td>
		<td width="30%"><bean:write name="accountsPayableAuditBean" property="operDate"/></td>
	</tr>
</table>
<br/>
<div style="width: 100%;padding-top: 4;padding-bottom: 4;border-bottom: 0" class="tb">        
   		<b>&nbsp;应收款审核</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">审核人：</td>
		<td width="30%"><bean:write name="accountsPayableAuditBean" property="auditOperid"/></td>
		<td nowrap="nowrap" class="wordtd">审核日期：</td>
		<td width="30%"><bean:write name="accountsPayableAuditBean" property="auditDate"/>
		<html:hidden name="accountsPayableAuditBean" property="auditDate"/>
		</td>
	</tr>
	<tr>
		<td class="wordtd">审核意见：</td>
		<td colspan="3">
		<logic:match name="isAudit" value="N">
		<html:textarea name="accountsPayableAuditBean" property="auditRem" cols="100" rows="3"></html:textarea>
	    </logic:match>
	    <logic:match name="isAudit" value="Y">
	    <bean:write name="accountsPayableAuditBean" property="auditRem"/>
		</logic:match>
	    </td>
	</tr>
</table>

  <script type="text/javascript"> 
	
  </script> 
</logic:present>
  </html:form>
</body>