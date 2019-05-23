<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<style>
  .show{display:block;}
  .hide{display:none;}
</style>

<logic:present name="paymentManageBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">委托合同号：</td>
		<td width="15%">
		<a onclick="simpleOpenWindow('returningMaintainDetailAction','${paymentManageBean.entrustContractNo}');" class="link">${paymentManageBean.entrustContractNo}</a>
		
		
		</td>
		<td class="wordtd" width="15%">委托单位名称：</td>
		<td width="20%">
			<bean:write name="paymentManageBean" property="companyId" />
		</td>
		<td class="wordtd" width="15%">所属分部：</td>
		<td width="10%">
			<bean:write name="paymentManageBean" property="maintDivision" />
		</td>
	</tr>
</table>
<br/>
<table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">合同总额：</td>
		<td width="15%"><bean:write name="contractTotal"/></td>
		<td class="wordtd" width="15%">合同已付款金额：</td>
		<td width="20%"><bean:write name="invoiceReceivables"/></td>
		<td class="wordtd"  width="15%">合同未付款金额：</td>
		<td width="10%"><bean:write name="noInvoiceReceivables"/></td>
	</tr>	
	 <tr>
	 <td class="wordtd">发票号：</td>
	 <td><bean:write name="invoiceNo"/></td>
	 <td class="wordtd">发票类型：</td>
	 <td colspan="3"><bean:write name="invoiceType"/></td>
	 </tr>
	<tr>
		<td class="wordtd">发票总额：</td>
		<td ><bean:write name="invoiceMoney"/></td>
		<td class="wordtd">已付款金额：</td>
		<td ><bean:write name="builtReceivables"/></td>
		<td class="wordtd">未付款金额：</td>
		<td ><bean:write name="noBuiltReceivables"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">凭证号：</td>
		<td width="15%"><bean:write name="paymentManageBean" property="paragraphNo"/></td>
		<td class="wordtd" width="15%">付款金额：</td>
		<td width="20%"><bean:write name="paymentManageBean" property="paragraphMoney"/></td>
		<td class="wordtd" width="15%">付款日期：</td>
		<td width="10%"><bean:write name="paymentManageBean" property="paragraphDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><bean:write name="paymentManageBean" property="rem" /></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">录入人：</td>
		<td width="15%">
			<bean:write name="paymentManageBean" property="operId" />
		</td>
		<td class="wordtd" width="15%">录入日期：</td>
		<td width="20%"><html:text name="paymentManageBean" property="operDate" readonly="true" styleClass="default_input_noborder" /></td>
		
		<td class="wordtd" width="15%">&nbsp;</td>
		<td width="10%">&nbsp;</td>
	</tr>
</table> 
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>保养单审核人审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期:</td>
		<td width="15%"><bean:write name="paymentManageBean" property="bydAuditDate"/></td>
		<td class="wordtd" width="15%">评价:</td>
		<td width="20%">
		<logic:present name="paymentManageBean" property="bydAuditEvaluate">
			<logic:match name="paymentManageBean" property="bydAuditEvaluate" value="WZ">完整</logic:match>
			<logic:match name="paymentManageBean" property="bydAuditEvaluate" value="YB">一般</logic:match>
			<logic:match name="paymentManageBean" property="bydAuditEvaluate" value="BWZ">不完整</logic:match>
		</logic:present>
		</td>
		<td class="wordtd" width="15%">&nbsp;</td>
		<td width="10%">&nbsp;</td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentManageBean" property="bydAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>例行回访结果审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="15%"><bean:write name="paymentManageBean" property="hfAuditDate"/></td>
		<td class="wordtd" width="15%">因技术问题引起的负面意见次数：</td>
		<td width="20%"><bean:write name="paymentManageBean" property="hfAuditNum"/></td>
		<td class="wordtd" width="15%">因非技术问题引起的负面意见次数：</td>
		<td width="10%"><bean:write name="paymentManageBean" property="hfAuditNum2"/></td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentManageBean" property="hfAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>热线管理人审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="15%"><bean:write name="paymentManageBean" property="rxAuditDate"/></td>
		<td class="wordtd" width="15%">因技术问题引起的投诉次数：</td>
		<td width="20%"><bean:write name="paymentManageBean" property="rxAuditNum"/></td>
		<td class="wordtd" width="15%">因非技术问题引起的投诉次数：</td>
		<td width="10%"><bean:write name="paymentManageBean" property="rxAuditNum2"/></td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentManageBean" property="rxAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>旧件退回情况审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期:</td>
		<td width="15%"><bean:write name="paymentManageBean" property="jjthAuditDate"/></td>
		<td class="wordtd" width="15%">评价:</td>
		<td width="20%">
		<logic:present name="paymentManageBean" property="jjthAuditEvaluate">
			<logic:match name="paymentManageBean" property="jjthAuditEvaluate" value="THJS">退回及时</logic:match>
			<logic:match name="paymentManageBean" property="jjthAuditEvaluate" value="THYB">一般</logic:match>
			<logic:match name="paymentManageBean" property="jjthAuditEvaluate" value="THBJS">退回不及时</logic:match>
		</logic:present>
		</td>
		<td class="wordtd" width="15%">&nbsp;</td>
		<td width="10%">&nbsp;</td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentManageBean" property="jjthAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>分部长审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="15%"><bean:write name="paymentManageBean" property="fbzAuditDate"/></td>
		<td class="wordtd" width="15%">评价：</td>
		<td width="20%">
		<logic:present name="paymentManageBean" property="fbzAuditEvaluate">
			<logic:equal name="paymentManageBean" property="fbzAuditEvaluate" value="PH">配合</logic:equal>
			<logic:equal name="paymentManageBean" property="fbzAuditEvaluate" value="PHYB">一般</logic:equal>
			<logic:match name="paymentManageBean" property="fbzAuditEvaluate" value="BPH">不配合</logic:match>
		</logic:present>
		</td>
		<td class="wordtd" width="15%">扣款金额：</td>
		<td width="10%"><bean:write name="paymentManageBean" property="debitMoney"/></td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentManageBean" property="fbzAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="2">&nbsp;<b>总部长审核</b></td></tr>
	<tr>
		<td class="wordtd" width="30%">审核日期：</td>
		<td><bean:write name="paymentManageBean" property="zbzAuditDate"/></td>
	</tr>
	<tr>
		<td class="wordtd" width="30%">意见:</td>
		<td>
			<bean:write name="paymentManageBean" property="zbzAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<%@ include file="/workflow/processApproveMessage.jsp" %>
<%-- <div style="width: 100%;padding-top: 4;padding-bottom: 4;border-bottom: 0" class="tb">        
   		<b>&nbsp;应收款审核</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">审核人：</td>
		<td width="30%"><bean:write name="paymentManageBean" property="auditOperid"/></td>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="30%"><bean:write name="paymentManageBean" property="auditDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">审核意见：</td>
		<td colspan="3"><bean:write name="paymentManageBean" property="auditRem"/></td>
	</tr>
</table> --%>

</logic:present>