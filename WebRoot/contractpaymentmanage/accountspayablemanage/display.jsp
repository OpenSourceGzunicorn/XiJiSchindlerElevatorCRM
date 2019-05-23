<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<style>
  .show{display:block;}
  .hide{display:none;}
</style>

<logic:present name="accountsPayableManageBean">
 <html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden property="auditStatus"/>
<html:hidden name="accountsPayableManageBean" property="jnlNo"/>
<html:hidden name="accountsPayableManageBean" property="billNo"/>

<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">委托合同号：</td>
		<td width="15%">
		<a onclick="simpleOpenWindow('returningMaintainDetailAction','${accountsPayableManageBean.entrustContractNo}');" class="link"><bean:write name="accountsPayableManageBean" property="entrustContractNo"/></a>
		</td>
		<td class="wordtd" width="15%">委托单位名称：</td>
		<td width="20%"><bean:write name="accountsPayableManageBean" property="companyId"/></td>
		<td class="wordtd" width="15%">所属分部：</td>
		<td width="10%"><bean:write name="accountsPayableManageBean" property="maintDivision"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd"  width="20%">合同总额：</td>
		<td width="15%"><bean:write name="contractTotal"/></td>
		<td class="wordtd" width="15%">已建应收款金额：</td>
		<td width="20%"><bean:write name="builtReceivables"/></td>
		<td class="wordtd" width="15%">未建应收款金额：</td>
		<td width="10%"><bean:write name="noBuiltReceivables"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">应收款名称：</td>
		<td width="15%"><bean:write name="accountsPayableManageBean" property="recName"/></td>
		<td class="wordtd" width="15%">应收款金额：</td>
		<td width="20%"><bean:write name="accountsPayableManageBean" property="preMoney"/></td>
		<td class="wordtd" width="15%">应收款日期：</td>
		<td width="10%"><bean:write name="accountsPayableManageBean" property="preDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><bean:write name="accountsPayableManageBean" property="rem"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">录入人：</td>
		<td width="30%"><bean:write name="accountsPayableManageBean" property="operId"/></td>
		<td class="wordtd" width="20%">录入日期：</td>
		<td width="30%"><bean:write name="accountsPayableManageBean" property="operDate"/></td>
	</tr>
</table>
<br/>
<div style="width: 100%;padding-top: 4;padding-bottom: 4;border-bottom: 0" class="tb">        
   		<b>&nbsp;应收款审核</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">审核人：</td>
		<td width="30%"><bean:write name="accountsPayableManageBean" property="auditOperid"/></td>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="30%"><bean:write name="accountsPayableManageBean" property="auditDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">审核意见：</td>
		<td colspan="3"><bean:write name="accountsPayableManageBean" property="auditRem"/></td>
	</tr>
</table>

  <script type="text/javascript"> 
	
  </script> 
</logic:present>