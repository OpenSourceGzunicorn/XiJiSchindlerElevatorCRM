<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<style>
  .show{display:block;}
  .hide{display:none;}
</style>

<logic:present name="contractPaymentEnquirybean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">委托合同号：</td>
		<td width="15%">
		<a onclick="simpleOpenWindow('returningMaintainDetailAction','${contractPaymentEnquirybean.entrustContractNo}');" class="link">${contractPaymentEnquirybean.entrustContractNo}</a>
		
		</td>
		<td class="wordtd" width="15%">委托单位名称：</td>
		<td width="15%"><bean:write name="contractPaymentEnquirybean" property="companyName"/></td>
		<td class="wordtd" width="15%">所属分部：</td>
		<td width="15%"><bean:write name="contractPaymentEnquirybean" property="maintDivision"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd"  width="20%">合同总金额：</td>
		<td width="15%"><bean:write name="contractPaymentEnquirybean" property="contractTotal"/></td>
		<td class="wordtd" width="15%">合同已付总额：</td>
		<td width="15%"><bean:write name="contractPaymentEnquirybean" property="payment"/></td>
		<td class="wordtd" width="15%">合同未付总额：</td>
		<td width="15%"><bean:write name="contractPaymentEnquirybean" property="noPayment"/></td>
	</tr>
	<tr>
		<td class="wordtd">应付总金额：</td>
		<td><bean:write name="contractPaymentEnquirybean" property="accountsPayable"/></td>
		<td class="wordtd">已收票总金额：</td>
		<td><bean:write name="contractPaymentEnquirybean" property="invoice"/></td>
		<td class="wordtd">未收票总金额金额：</td>
		<td><bean:write name="contractPaymentEnquirybean" property="noInvoice"/></td>
	</tr>
</table>
<br/>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;应付款信息</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="15%">应付款流水号</td>
			<td width="15%">应付款名称</td>
			<td width="15%">应付款金额</td>
			<td width="15%">应付款日期</td>
			<td width="40%">备注</td>
		</tr>
	</thead>
	<tfoot>
		<tr height="15"><td colspan="5"></td></tr>
	</tfoot>
	<tbody>
		<logic:present name="contractPayablesMasterList">
          <logic:iterate id="contractPayablesMaster" name="contractPayablesMasterList" >
          	<tr>
				<td><bean:write name="contractPayablesMaster" property="jnlNo"/></td>
				<td><bean:write name="contractPayablesMaster" property="recName"/></td>
				<td><bean:write name="contractPayablesMaster" property="preMoney"/></td>
				<td><bean:write name="contractPayablesMaster" property="preDate"/></td>
				<td><bean:write name="contractPayablesMaster" property="rem"/></td>
			</tr>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
<br/>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;收票信息</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="15%">收票流水号</td>
			<td width="15%">发票号</td>
			<td width="15%">发票金额</td>
			<td width="15%">发票类型</td>
			<td width="40%">备注</td>
		</tr>
	</thead>
	<tfoot>
		<tr height="15"><td colspan="5"></td></tr>
	</tfoot>
	<tbody>
		<logic:present name="contractTicketManageList">
          <logic:iterate id="contractTicketManage" name="contractTicketManageList" >
          	<tr>
				<td><bean:write name="contractTicketManage" property="jnlNo"/></td>
				<td><bean:write name="contractTicketManage" property="invoiceNo"/></td>
				<td><bean:write name="contractTicketManage" property="invoiceMoney"/></td>
				<td><bean:write name="contractTicketManage" property="invoiceType"/></td>
				<td><bean:write name="contractTicketManage" property="rem"/></td>
			</tr>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
<br/>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;付款信息</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="15%">收票流水号</td>
			<td width="15%">凭证号</td>
			<td width="15%">付款金额</td>
			<td width="15%">扣款金额</td>
			<td width="15%">付款日期</td>
			<td width="25%">备注</td>
		</tr>
	</thead>
	<tfoot>
		<tr height="15"><td colspan="5"></td></tr>
	</tfoot>
	<tbody>
		<logic:present name="contractPaymentManageList">
          <logic:iterate id="contractPaymentManage" name="contractPaymentManageList" >
          	<tr>
				<td><bean:write name="contractPaymentManage" property="ctJnlNo"/></td>
				<td><bean:write name="contractPaymentManage" property="paragraphNo"/></td>
				<td><bean:write name="contractPaymentManage" property="paragraphMoney"/></td>
				<td><bean:write name="contractPaymentManage" property="debitMoney"/></td>
				<td><bean:write name="contractPaymentManage" property="paragraphDate"/></td>
				<td><bean:write name="contractPaymentManage" property="rem"/></td>
			</tr>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
</logic:present>