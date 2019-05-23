<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<style>
  .show{display:block;}
  .hide{display:none;}
</style>

<logic:present name="contractBean">
 <html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="contractBean" property="billNo"/>

<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">合同号：</td>
		<td width="20%"><bean:write name="contractBean" property="contractNo"/></td>
		<td class="wordtd" width="15%">甲方单位名称：</td>
		<td width="20%"><bean:write name="contractBean" property="companyId"/></td>
		<td class="wordtd" width="15%">合同类型：</td>
		<td width="20%">
			<logic:match name="contractBean" property="contractType" value="B">保养</logic:match>
			<logic:match name="contractBean" property="contractType" value="W">维修</logic:match>
			<logic:match name="contractBean" property="contractType" value="G">改造</logic:match>
		</td>
	</tr>
	<tr>
		<td class="wordtd">所属分部：</td>
		<td><bean:write name="contractBean" property="maintDivision"/></td>
		<td class="wordtd">所属维保站：</td>
		<td><bean:write name="contractBean" property="maintStation"/></td>
		<td class="wordtd"></td>
		<td></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd"  width="15%">合同总金额：</td>
		<td width="20%"><bean:write name="contractBean" property="contractTotal"/></td>
		<td class="wordtd" width="15%">合同已收总额：</td>
		<td width="20%"><bean:write name="contractBean" property="invoiceTotal"/></td>
		<td class="wordtd" width="15%">合同未收总额：</td>
		<td width="20%"><bean:write name="contractBean" property="noInvoiceTotal"/></td>
	</tr>
	<tr>
		<td class="wordtd">应收总金额：</td>
		<td><bean:write name="contractBean" property="preMoney"/></td>
		<td class="wordtd">已开票总金额：</td>
		<td><bean:write name="contractBean" property="amount"/></td>
		<td class="wordtd">未开票总金额金额：</td>
		<td><bean:write name="contractBean" property="arrearsMoney"/></td>
	</tr>
</table>
<br/>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;应收款信息</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="15%">应收款流水号</td>
			<td width="15%">应收款名称</td>
			<td width="15%">应收款金额</td>
			<td width="15%">应收款日期</td>
			<td width="40%">备注</td>
		</tr>
	</thead>
	<tfoot>
		<tr height="15"><td colspan="5"></td></tr>
	</tfoot>
	<tbody>
		<logic:present name="proList">
          <logic:iterate id="pro" name="proList" >
          	<tr>
				<td align="center"><bean:write name="pro" property="jnlNo"/></td>
				<td align="center"><bean:write name="pro" property="recName"/></td>
				<td align="center"><bean:write name="pro" property="preMoney"/></td>
				<td align="center"><bean:write name="pro" property="preDate"/></td>
				<td><bean:write name="pro" property="rem"/></td>
			</tr>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
<br/>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;开票信息</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="15%">应收款流水号</td>
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
		<logic:present name="invoiceList">
          <logic:iterate id="invoice" name="invoiceList" >
          	<tr>
				<td align="center">${invoice.arfJnlNo}</td>
				<td align="center"><bean:write name="invoice" property="invoiceNo"/></td>
				<td align="center"><bean:write name="invoice" property="invoiceMoney"/></td>
				<td align="center"><bean:write name="invoice" property="invoiceType"/></td>
				<td><bean:write name="invoice" property="rem"/></td>
			</tr>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
<br/>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;来款信息</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="15%">应收款流水号</td>
			<td width="15%">凭证号</td>
			<td width="15%">来款金额</td>
			<td width="15%">来款日期</td>
			<td width="40%">备注</td>
		</tr>
	</thead>
	<tfoot>
		<tr height="15"><td colspan="5"></td></tr>
	</tfoot>
	<tbody>
		<logic:present name="paragraphList">
          <logic:iterate id="paragraph" name="paragraphList" >
          	<tr>
				<td align="center">${paragraph.arfJnlNo}</td>
				<td align="center"><bean:write name="paragraph" property="paragraphNo"/></td>
				<td align="center"><bean:write name="paragraph" property="paragraphMoney"/></td>
				<td align="center"><bean:write name="paragraph" property="paragraphDate"/></td>
				<td><bean:write name="paragraph" property="rem"/></td>
			</tr>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>

  <script type="text/javascript"> 
	
  </script> 
</logic:present>