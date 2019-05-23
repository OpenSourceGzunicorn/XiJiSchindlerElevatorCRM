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
  <html:form action="/proContractArfeeMasterAction.do?method=toAuditRecord">
   <logic:present name="proContractArfeeMasterBean">
 <html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="proContractArfeeMasterBean" property="jnlNo"/>
<html:hidden name="proContractArfeeMasterBean" property="billNo"/>
<html:hidden name="proContractArfeeMasterBean" property="submitType"/>
<html:hidden name="proContractArfeeMasterBean" property="auditStatus"/>

<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">合同号：</td>
		<td width="20%"><bean:write name="proContractArfeeMasterBean" property="contractNo"/></td>
		<td class="wordtd" width="15%">甲方单位名称：</td>
		<td width="20%"><bean:write name="proContractArfeeMasterBean" property="companyId"/></td>
		<td class="wordtd" width="15%">合同类型：</td>
		<td width="20%">
			<logic:match name="proContractArfeeMasterBean" property="contractType" value="B">保养</logic:match>
			<logic:match name="proContractArfeeMasterBean" property="contractType" value="W">维修</logic:match>
			<logic:match name="proContractArfeeMasterBean" property="contractType" value="G">改造</logic:match>
		</td>
	</tr>
	<tr>
		<td class="wordtd">所属分部：</td>
		<td><bean:write name="proContractArfeeMasterBean" property="maintDivision"/></td>
		<td class="wordtd">所属维保站：</td>
		<td><bean:write name="proContractArfeeMasterBean" property="maintStation"/></td>
		<td class="wordtd"></td>
		<td></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd"  width="15%">合同总额：</td>
		<td width="20%"><bean:write name="contractTotal"/></td>
		<td class="wordtd" width="15%">已建应收款金额：</td>
		<td width="20%"><bean:write name="builtReceivables"/></td>
		<td class="wordtd" width="15%">未建应收款金额：</td>
		<td width="20%"><bean:write name="noBuiltReceivables"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">维保区间：</td>
		<td colspan="5"><bean:write name="proContractArfeeMasterBean" property="maintScope"/></td>
	</tr>
	<tr>
		<td class="wordtd" width="15%">应收款名称：</td>
		<td width="20%"><bean:write name="proContractArfeeMasterBean" property="recName"/></td>
		<td class="wordtd" width="15%">应收款金额：</td>
		<td width="20%"><bean:write name="proContractArfeeMasterBean" property="preMoney"/></td>
		<td class="wordtd" width="15%">应收款日期：</td>
		<td width="20%"><bean:write name="proContractArfeeMasterBean" property="preDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><bean:write name="proContractArfeeMasterBean" property="rem"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">录入人：</td>
		<td width="20%"><bean:write name="proContractArfeeMasterBean" property="operId"/></td>
		<td class="wordtd" width="15%">录入日期：</td>
		<td><bean:write name="proContractArfeeMasterBean" property="operDate"/></td>
	</tr>
</table>

    <br/>
    <div style="width: 100%;padding-top: 4;padding-bottom: 4;border-bottom: 0" class="tb">        
   		<b>&nbsp;应收款审核</b>
  	</div>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    	<tr>
    		<td class="wordtd" width="15%">审核人：</td>
    		<td width="20%"><bean:write name="auditOpername"/></td>
    		<td class="wordtd" width="15%">审核日期：</td>
    		<td><bean:write name="auditDate"/></td>
    	</tr>
    	<tr>
    		<td class="wordtd">审核意见：</td>
    		<td colspan="3"><html:textarea property="auditRem" rows="3" cols="100" styleClass="default_textarea"/></td>
    	</tr>
    </table>
</logic:present>
  </html:form>
</body>