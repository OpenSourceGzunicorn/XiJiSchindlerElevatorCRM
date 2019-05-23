<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="contractInvoiceManageBean" property="jnlNo"/>

<html:hidden name="contractInvoiceManageBean" property="billNo"/>
<html:hidden name="contractInvoiceManageBean" property="submitType"/>
<html:hidden name="contractInvoiceManageBean" property="auditStatus"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd">合同号：</td>
		<td width="20%"><html:text name="contractInvoiceManageBean" property="contractNo" readonly="true" styleClass="default_input_noborder" /></td>
		<td class="wordtd" width="15%">台数：</td>
		<td width="20%">
			<html:text name="contractInvoiceManageBean" size="10" property="r9" styleId="r9" onkeypress="f_check_number3();" />
			<font color="red">*</font>
		</td>
		<td class="wordtd">合同类型：</td>
		<td width="20%">
			<logic:match name="contractInvoiceManageBean" property="contractType" value="B">保养</logic:match>
			<logic:match name="contractInvoiceManageBean" property="contractType" value="W">维修</logic:match>
			<logic:match name="contractInvoiceManageBean" property="contractType" value="G">改造</logic:match>
			<html:hidden name="contractInvoiceManageBean" property="contractType" />
		</td>
	</tr>
	<tr>
		<td class="wordtd">甲方单位名称：</td>
		<td width="20%">
			<bean:write name="companyName" />
			<html:hidden name="contractInvoiceManageBean" property="companyId" />
		</td>
		<td class="wordtd">所属分部：</td>
		<td>
			<bean:write name="maintDivisionName"/>
			<html:hidden name="contractInvoiceManageBean" property="maintDivision" />
		</td>
		<td class="wordtd">所属维保站：</td>
		<td>
			<bean:write name="maintStationName"/>
			<html:hidden name="contractInvoiceManageBean" property="maintStation" />
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd">合同总额：</td>
		<td width="20%"><bean:write name="contractBean" property="contractTotal"/></td>
		<td class="wordtd">已开票总额：</td>
		<td width="20%"><bean:write name="contractBean" property="invoiceTotal"/></td>
		<td class="wordtd">未开票总额：</td>
		<td width="20%"><bean:write name="contractBean" property="noInvoiceTotal"/></td>
	</tr>
	<tr>
		<td class="wordtd">应收款流水号：</td>
		<td><html:text name="contractBean" property="ARF_JnlNo" readonly="true" styleClass="default_input_noborder" /></td>
		<td class="wordtd">应收款名称：</td>
		<td><bean:write name="contractBean" property="recName"/></td>
		<td class="wordtd">应收款日期：</td>
		<td><bean:write name="contractBean" property="preDate"/></td>
	</tr>
	<tr>
		<td class="wordtd">应收款金额：</td>
		<td><bean:write name="contractBean" property="preMoney"/></td>
		<td class="wordtd">已开票金额：</td>
		<td><bean:write name="contractBean" property="bilMoney"/></td>
		<td class="wordtd">未开票金额：</td>
		<td><bean:write name="contractBean" property="nobilMoney"/></td>
	</tr>
	<tr>
		<td class="wordtd">应收款备注：</td>
		<td colspan="5"><bean:write name="contractBean" property="yskrem"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd">发票号：</td>
		<td width="20%"><html:text name="contractInvoiceManageBean" property="invoiceNo" styleClass="default_input"/></td>
		<td class="wordtd">开票名称：</td>
		<td width="20%">
			<bean:write name="contractInvoiceManageBean" property="invoiceName"/>
			<html:hidden name="contractInvoiceManageBean" property="invoiceName"/>
		</td>
		<td class="wordtd">维保区间：</td>
		<td width="20%">
			<html:text name="contractInvoiceManageBean" size="22" property="maintScope" styleId="maintScope"/>
		</td>
	</tr>
	<tr>
		<td class="wordtd">发票类型：</td>
		<td>
			<html:select name="contractInvoiceManageBean" property="invoiceType">
				<html:option value="">请选择</html:option>
		    	<html:options collection="invoiceTypeList" property="inTypeId" labelProperty="inTypeName"/>
        	</html:select><font color="red">*</font>
			<%-- <html:text name="contractInvoiceManageBean" property="invoiceType" styleClass="default_input" /> --%>
		</td>
		<td class="wordtd">开票金额：</td>
		<td><html:text name="contractInvoiceManageBean" property="invoiceMoney" styleId="invoiceMoney" styleClass="default_input" onkeypress="f_check_number3();" onkeydown="judgePreMoney(this,'${contractBean.nobilMoney}');" onkeyup="judgePreMoney(this,'${contractBean.nobilMoney}');" /><font color="red">*</font></td>
		<td class="wordtd">开票日期：</td>
		<td><html:text name="contractInvoiceManageBean" property="invoiceDate" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" /></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><html:textarea name="contractInvoiceManageBean" property="rem" rows="3" cols="100" styleClass="default_textarea"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd">录入人：</td>
		<td width="20%">
			<bean:write name="operName"/>
			<html:hidden name="contractInvoiceManageBean" property="operId" />
		</td>
		<td class="wordtd">录入日期：</td>
		<td><html:text name="contractInvoiceManageBean" property="operDate" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>

<script type="text/javascript"> 
</script>