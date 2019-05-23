<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<style>
  .show{display:block;}
  .hide{display:none;}
</style>

<logic:present name="contractParagraphManageBean">
 <html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="contractParagraphManageBean" property="jnlNo"/>
<html:hidden name="contractParagraphManageBean" property="billNo"/>

<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">合同号：</td>
		<td width="20%"><bean:write name="contractParagraphManageBean" property="contractNo"/></td>
		<td class="wordtd" width="15%">甲方单位名称：</td>
		<td width="20%"><bean:write name="contractParagraphManageBean" property="companyId"/></td>
		<td class="wordtd" width="15%">合同类型：</td>
		<td width="20%">
			<logic:match name="contractParagraphManageBean" property="contractType" value="B">保养</logic:match>
			<logic:match name="contractParagraphManageBean" property="contractType" value="W">维修</logic:match>
			<logic:match name="contractParagraphManageBean" property="contractType" value="G">改造</logic:match>
		</td>
	</tr>
	<tr>
		<td class="wordtd">所属分部：</td>
		<td><bean:write name="contractParagraphManageBean" property="maintDivision"/></td>
		<td class="wordtd">所属维保站：</td>
		<td><bean:write name="contractParagraphManageBean" property="maintStation"/></td>
		<td class="wordtd"></td>
		<td></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd"  width="15%">合同总额：</td>
		<td width="20%"><bean:write name="contractBean" property="contractTotal"/></td>
		<td class="wordtd" width="15%">合同已收总额：</td>
		<td width="20%"><bean:write name="contractBean" property="invoiceTotal"/></td>
		<td class="wordtd" width="15%">合同未收总额：</td>
		<td width="20%"><bean:write name="contractBean" property="noInvoiceTotal"/></td>
	</tr>
	<tr>
		<td class="wordtd">应收款流水号：</td>
		<td><html:text name="contractBean" property="ARF_JnlNo" readonly="true" styleClass="default_input_noborder" /></td>
		<td class="wordtd">应收款名称：</td>
		<td><bean:write name="contractBean" property="recName"/></td>
		<td class="wordtd">维保区间：</td>
		<td><bean:write name="contractBean" property="maintScope"/></td>
	</tr>
	<tr>
		<td class="wordtd">应收款金额：</td>
		<td><bean:write name="contractBean" property="preMoney"/></td>
		<td class="wordtd">已来款金额：</td>
		<td><bean:write name="contractBean" property="amount"/></td>
		<td class="wordtd">欠款金额：</td>
		<td><bean:write name="contractBean" property="arrearsMoney"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">凭证号：</td>
		<td width="20%"><bean:write name="contractParagraphManageBean" property="paragraphNo"/></td>
		<td class="wordtd" width="15%">来款金额：</td>
		<td width="20%"><bean:write name="contractParagraphManageBean" property="paragraphMoney"/></td>
		<td class="wordtd" width="15%">来款日期：</td>
		<td width="20%"><bean:write name="contractParagraphManageBean" property="paragraphDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><bean:write name="contractParagraphManageBean" property="rem"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">录入人：</td>
		<td width="20%"><bean:write name="contractParagraphManageBean" property="operId"/></td>
		<td class="wordtd" width="15%">录入日期：</td>
		<td ><bean:write name="contractParagraphManageBean" property="operDate"/></td>
	</tr>
</table>
<br/>
<div style="width: 100%;padding-top: 4;padding-bottom: 4;border-bottom: 0" class="tb">        
   		<b>&nbsp;来款审核</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">审核人：</td>
		<td width="20%"><bean:write name="contractParagraphManageBean" property="auditOperid"/></td>
		<td class="wordtd" width="15%">审核日期：</td>
		<td ><bean:write name="contractParagraphManageBean" property="auditDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">审核意见：</td>
		<td colspan="3"><bean:write name="contractParagraphManageBean" property="auditRem"/></td>
	</tr>
</table>

  <script type="text/javascript"> 
	
  </script> 
</logic:present>