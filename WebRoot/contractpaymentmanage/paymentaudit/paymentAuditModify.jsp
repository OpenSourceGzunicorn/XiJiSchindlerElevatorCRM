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
<html:form action="/paymentAuditAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<%-- <html:hidden property="auditStatus"/> --%>
<logic:present name="paymentAuditBean">
<html:hidden name="paymentAuditBean" property="jnlNo"/>
<html:hidden name="paymentAuditBean" property="billNo"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">委托合同号：</td>
		<td width="15%">
		<a onclick="simpleOpenWindow('returningMaintainDetailAction','${paymentAuditBean.entrustContractNo}');" class="link">${paymentAuditBean.entrustContractNo}</a>
		
		</td>
		<td class="wordtd" width="15%">委托单位名称：</td>
		<td width="20%">
			<bean:write name="paymentAuditBean" property="companyId" />
		</td>
		<td class="wordtd" width="15%">所属分部：</td>
		<td width="10%">
			<bean:write name="paymentAuditBean" property="maintDivision" />
		</td>
	</tr>
</table>
<br/>
<%-- <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
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
<br/>--%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">凭证号：</td>
		<td width="15%"><bean:write name="paymentAuditBean" property="paragraphNo"/></td>
		<td class="wordtd" width="15%">付款金额：</td>
		<td width="20%"><bean:write name="paymentAuditBean" property="paragraphMoney"/></td>
		<td class="wordtd" width="15%">付款日期：</td>
		<td width="10%"><bean:write name="paymentAuditBean" property="paragraphDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><bean:write name="paymentAuditBean" property="rem" /></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">录入人：</td>
		<td width="15%">
			<bean:write name="paymentAuditBean" property="operId" />
		</td>
		<td class="wordtd" width="15%">录入日期：</td>
		<td width="20%"><bean:write name="paymentAuditBean" property="operDate"/></td>
		
		<td class="wordtd" width="15%">&nbsp;</td>
		<td width="10%">&nbsp;</td>
	</tr>
</table> 
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>保养单审核人审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期:</td>
		<td width="15%"><bean:write name="paymentAuditBean" property="bydAuditDate"/></td>
		<td class="wordtd" width="15%">评价:</td>
		<td width="20%">
		<logic:present name="paymentAuditBean" property="bydAuditEvaluate">
			<logic:match name="paymentAuditBean" property="bydAuditEvaluate" value="WZ">完整</logic:match>
			<logic:match name="paymentAuditBean" property="bydAuditEvaluate" value="YB">一般</logic:match>
			<logic:match name="paymentAuditBean" property="bydAuditEvaluate" value="BWZ">不完整</logic:match>
		</logic:present>
		</td>
		<td class="wordtd" width="15%">&nbsp;</td>
		<td width="10%">&nbsp;</td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentAuditBean" property="bydAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>例行回访结果审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="15%"><bean:write name="paymentAuditBean" property="hfAuditDate"/></td>
		<td class="wordtd" width="15%">因技术问题引起的负面意见次数：</td>
		<td width="20%"><bean:write name="paymentAuditBean" property="hfAuditNum"/></td>
		<td class="wordtd" width="15%">因非技术问题引起的负面意见次数：</td>
		<td width="10%"><bean:write name="paymentAuditBean" property="hfAuditNum2"/></td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentAuditBean" property="hfAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>热线管理人审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="15%"><bean:write name="paymentAuditBean" property="rxAuditDate"/></td>
		<td class="wordtd" width="15%">因技术问题引起的投诉次数：</td>
		<td width="20%"><bean:write name="paymentAuditBean" property="rxAuditNum"/></td>
		<td class="wordtd" width="15%">因非技术问题引起的投诉次数：</td>
		<td width="10%"><bean:write name="paymentAuditBean" property="rxAuditNum2"/></td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentAuditBean" property="rxAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>旧件退回情况审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期:</td>
		<td width="15%"><bean:write name="paymentAuditBean" property="jjthAuditDate"/></td>
		<td class="wordtd" width="15%">评价:</td>
		<td width="20%">
		<logic:present name="paymentAuditBean" property="jjthAuditEvaluate">
			<logic:match name="paymentAuditBean" property="jjthAuditEvaluate" value="THJS">退回及时</logic:match>
			<logic:match name="paymentAuditBean" property="jjthAuditEvaluate" value="THYB">一般</logic:match>
			<logic:match name="paymentAuditBean" property="jjthAuditEvaluate" value="THBJS">退回不及时</logic:match>
		</logic:present>
		</td>
		<td class="wordtd" width="15%">&nbsp;</td>
		<td width="10%">&nbsp;</td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentAuditBean" property="jjthAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="6">&nbsp;<b>分部长审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="15%"><bean:write name="paymentAuditBean" property="fbzAuditDate"/></td>
		<td class="wordtd" width="15%">评价：</td>
		<td width="20%">
		<logic:present name="paymentAuditBean" property="fbzAuditEvaluate">
			<logic:equal name="paymentAuditBean" property="fbzAuditEvaluate" value="PH">配合</logic:equal>
			<logic:equal name="paymentAuditBean" property="fbzAuditEvaluate" value="PHYB">一般</logic:equal>
			<logic:match name="paymentAuditBean" property="fbzAuditEvaluate" value="BPH">不配合</logic:match>
		</logic:present>
		</td>
		<td class="wordtd" width="15%">扣款金额：</td>
		<td width="10%"><bean:write name="paymentAuditBean" property="debitMoney"/></td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">意见:</td>
		<td colspan="5">
			<bean:write name="paymentAuditBean" property="fbzAuditRem"/>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr height="23"><td colspan="2">&nbsp;<b>总部长审核</b></td></tr>
	<tr>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="75%"><bean:write name="paymentAuditBean" property="zbzAuditDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">意见:</td>
		<td>
			<bean:write name="paymentAuditBean" property="zbzAuditRem"/>
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
		<td width="30%"><bean:write name="paymentAuditBean" property="auditOperid"/></td>
		<td class="wordtd" width="20%">审核日期：</td>
		<td width="30%"><bean:write name="paymentAuditBean" property="auditDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">审核意见：</td>
		<td colspan="3"><logic:match name="isAudit" value="N">
		<html:textarea name="paymentAuditBean" property="auditRem" cols="100" rows="3"></html:textarea>
	    </logic:match>
	    <logic:match name="isAudit" value="Y">
	    <bean:write name="paymentAuditBean" property="auditRem"/>
		</logic:match></td>
	</tr>
</table> --%>

  <script type="text/javascript"> 
	
  </script> 
</logic:present>
  </html:form>
</body>