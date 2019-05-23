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
  <html:form action="/contractInvoiceManageAction.do?method=toAuditRecord">
   <logic:present name="contractInvoiceManageBean">
 <html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="contractInvoiceManageBean" property="jnlNo"/>
<html:hidden name="contractInvoiceManageBean" property="billNo"/>
<html:hidden name="contractInvoiceManageBean" property="submitType"/>
<html:hidden name="contractInvoiceManageBean" property="auditStatus"/>

<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">合同号：</td>
		<td width="20%"><bean:write name="contractInvoiceManageBean" property="contractNo"/></td>
		<td class="wordtd" width="15%">台数：</td>
		<td width="20%"><bean:write name="contractInvoiceManageBean" property="r9"/></td>
		<td class="wordtd" width="15%">合同类型：</td>
		<td width="20%">
			<logic:match name="contractInvoiceManageBean" property="contractType" value="B">保养</logic:match>
			<logic:match name="contractInvoiceManageBean" property="contractType" value="W">维修</logic:match>
			<logic:match name="contractInvoiceManageBean" property="contractType" value="G">改造</logic:match>
		</td>
	</tr>
	<tr>
		<td class="wordtd" width="15%">甲方单位名称：</td>
		<td width="20%"><bean:write name="CustomerBean" property="companyName"/></td>
		<td class="wordtd">所属分部：</td>
		<td><bean:write name="contractInvoiceManageBean" property="maintDivision"/></td>
		<td class="wordtd">所属维保站：</td>
		<td><bean:write name="contractInvoiceManageBean" property="maintStation"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd"  width="15%">合同总额：</td>
		<td width="20%"><bean:write name="contractBean" property="contractTotal"/></td>
		<td class="wordtd" width="15%">已开票总额：</td>
		<td width="20%"><bean:write name="contractBean" property="invoiceTotal"/></td>
		<td class="wordtd" width="15%">未开票总额：</td>
		<td width="20%"><bean:write name="contractBean" property="noInvoiceTotal"/></td>
	</tr>
	<tr>
		<td class="wordtd">应收款金额：</td>
		<td><bean:write name="contractBean" property="preMoney"/></td>
		<td class="wordtd">已开票金额：</td>
		<td><bean:write name="contractBean" property="bilMoney"/></td>
		<td class="wordtd">未开票金额：</td>
		<td><bean:write name="contractBean" property="nobilMoney"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">

	<tr>
		<td class="wordtd">应收款流水号：</td>
		<td><html:text name="contractBean" property="ARF_JnlNo" readonly="true" styleClass="default_input_noborder" /></td>
		<td class="wordtd">应收款名称：</td>
		<td><bean:write name="contractBean" property="recName"/></td>
		<td class="wordtd">应收款日期：</td>
		<td><bean:write name="contractBean" property="preDate"/></td>
	</tr>
	<tr>
		<td class="wordtd" width="15%">发票号：</td>
		<td width="20%"><bean:write name="contractInvoiceManageBean" property="invoiceNo"/></td>
		<td class="wordtd" width="15%">开票名称：</td>
		<td width="20%"><bean:write name="contractInvoiceManageBean" property="invoiceName"/></td>
		<td class="wordtd" width="15%">维保区间：</td>
		<td width="20%"><bean:write name="contractInvoiceManageBean" property="maintScope"/></td>
	</tr>
	<tr>
		<td class="wordtd">发票类型：</td>
		<td><bean:write name="contractInvoiceManageBean" property="invoiceType"/></td>
		<td class="wordtd">开票金额：</td>
		<td><bean:write name="contractInvoiceManageBean" property="invoiceMoney"/></td>
		<td class="wordtd">开票日期：</td>
		<td><bean:write name="contractInvoiceManageBean" property="invoiceDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><bean:write name="contractInvoiceManageBean" property="rem"/></td>
	</tr>

  <tr>
    <td height="23" colspan="6" align="center">&nbsp;<b>开票信息</td>
  </tr>
    <tr>
    <td class="wordtd"><bean:message key="customer.invoiceName"/>:</td>
    <td colspan="5"><bean:write name="CustomerBean" property="invoiceName"/></td> 
  </tr>
    <tr>
	    <td class="wordtd">纳税人识别号:</td>
	    <td colspan="5"><bean:write name="CustomerBean" property="taxId"/></td>    
  	</tr>
    <tr>
	    <td class="wordtd">地址、电话:</td>
	    <td colspan="5"><bean:write name="CustomerBean" property="accountHolder"/></td> 
    </tr> 
  <tr>
	    <td class="wordtd"><bean:message key="customer.bank"/>:</td>
	    <td colspan="5"><bean:write name="CustomerBean" property="bank"/></td>
  </tr>
  <tr>  
	    <td class="wordtd"><bean:message key="customer.account"/>:</td>
	    <td colspan="5"><bean:write name="CustomerBean" property="account"/></td>
    </tr> 
  <%-- 
  <tr>
    <td class="wordtd"><bean:message key="customer.invoiceName"/>:</td>
    <td><bean:write name="CustomerBean" property="invoiceName"/></td> 
    <td class="wordtd">开票类型:</td>
    <td>
    <logic:equal name="CustomerBean" property="r1" value="Z">专票</logic:equal>
    <logic:equal name="CustomerBean" property="r1" value="P">普票</logic:equal>
    </td>
    <td class="wordtd"><bean:message key="customer.bank"/>:</td>
    <td><bean:write name="CustomerBean" property="bank"/></td>
  </tr>
  <tr>  
    <td class="wordtd"><bean:message key="customer.account"/>:</td>
    <td><bean:write name="CustomerBean" property="account"/></td>
    <td class="wordtd">地址、电话:</td>
    <td><bean:write name="CustomerBean" property="accountHolder"/></td> 
    <td class="wordtd">纳税人识别号:</td>
    <td><bean:write name="CustomerBean" property="taxId"/></td>    
  </tr>  
  --%>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">录入人：</td>
		<td width="20%"><bean:write name="contractInvoiceManageBean" property="operId"/></td>
		<td class="wordtd" width="15%">录入日期：</td>
		<td><bean:write name="contractInvoiceManageBean" property="operDate"/></td>
	</tr>
</table>
</logic:present>
    <br/>
    <div style="width: 100%;padding-top: 4;padding-bottom: 4;border-bottom: 0" class="tb">        
   		<b>&nbsp;开票确认</b>
  	</div>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    	<tr>
    		<td class="wordtd" width="15%">确认人：</td>
    		<td width="20%"><bean:write name="auditOpername"/></td>
    		<td class="wordtd" width="15%">确认日期：</td>
    		<td><bean:write name="auditDate"/></td>
    	</tr>
    	<tr>
    		<td class="wordtd">确认意见：</td>
    		<td colspan="3"><html:textarea property="auditRem" rows="3" cols="100" styleClass="default_textarea"/></td>
    	</tr>
    </table>
    <br/>

  </html:form>
</body>

