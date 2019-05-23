<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<style>
  .show{display:block;}
  .hide{display:none;}
</style>

<logic:present name="proContractArfeeMasterBean">
 <html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="proContractArfeeMasterBean" property="jnlNo"/>
<html:hidden name="proContractArfeeMasterBean" property="billNo"/>

<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">合同号：</td>
		<td width="20%">
		<a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="proContractArfeeMasterBean"  property="billNo"/>" target="_blnk"> 
			<bean:write name="proContractArfeeMasterBean" property="contractNo"/>
		</a>
		</td>
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
		<td width="20%"><bean:write name="proContractArfeeMasterBean" property="auditOperid"/></td>
		<td class="wordtd" width="15%">审核日期：</td>
		<td><bean:write name="proContractArfeeMasterBean" property="auditDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">审核意见：</td>
		<td colspan="3"><bean:write name="proContractArfeeMasterBean" property="auditRem"/></td>
	</tr>
</table>

<br/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    	<tr>
    		<td class="wordtd" width="15%">提醒备注：</td>
    		<td>
    			<logic:present name="doType">
	    			<logic:match name="doType" value="warn">
	    				<html:textarea name="proContractArfeeMasterBean" property="warnRem" cols="100" rows="4"></html:textarea>
	    			</logic:match>
    			</logic:present>
    			<logic:notPresent name="doType">
    				<bean:write name="proContractArfeeMasterBean" property="warnRem"/>
    			</logic:notPresent>
    		</td>
    	</tr>
    </table>

  <script type="text/javascript"> 
	
  </script> 
</logic:present>