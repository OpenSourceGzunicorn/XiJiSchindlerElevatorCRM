<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="invoiceManageBean" property="jnlNo"/>

<html:hidden name="invoiceManageBean" property="billNo"/>
<html:hidden name="invoiceManageBean" property="submitType"/>
<html:hidden name="invoiceManageBean" property="auditStatus"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">委托合同号：</td>
		<td width="15%"><html:hidden name="invoiceManageBean" property="entrustContractNo" />
		<a onclick="simpleOpenWindow('returningMaintainDetailAction','${invoiceManageBean.entrustContractNo}');" class="link">${invoiceManageBean.entrustContractNo}</a>
		
		</td>
		<td class="wordtd" width="15%">委托单位名称：</td>
		<td width="20%">
		    <bean:write name="companyName"/>
			<html:hidden name="invoiceManageBean" property="companyId" />
		</td>
		<td class="wordtd" width="15%">所属分部：</td>
		<td width="10%">
		    <bean:write name="maintDivisionName"/>
			<html:hidden name="invoiceManageBean" property="maintDivision" />
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd"  width="20%">合同总额：</td>
		<td width="15%"><bean:write name="contractTotal"/></td>
		<td class="wordtd" width="15%">已收发票金额：</td>
		<td width="20%"><bean:write name="builtReceivables"/></td>
		<td class="wordtd" width="15%">未收发票金额：</td>
		<td width="10%"><bean:write name="noBuiltReceivables" format="0.00"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
	<td class="wordtd" width="20%">发票号：</td>
		<td colspan="5"><html:text name="invoiceManageBean" property="invoiceNo" size="25" styleClass="default_input" /><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="wordtd" width="20%">发票类型：</td>
		<td width="15%">
			<html:select name="invoiceManageBean" property="invoiceType">
				<html:option value="">请选择</html:option>
		    	<html:options collection="receivablesList" property="inTypeId" labelProperty="inTypeName"/>
        	</html:select><font color="red">*</font>
		</td>
		<td class="wordtd" width="15%">发票金额：</td>
		<td width="20%"><html:text name="invoiceManageBean" property="invoiceMoney" styleId="money" styleClass="default_input" onkeypress="f_check_number3()" onchange="judgePreMoney(this,'${noBuiltReceivables}')" /><font color="red">*</font></td>
		<td class="wordtd" width="15%">发票日期：</td>
		<td width="10%"><html:text name="invoiceManageBean" property="invoiceDate" size="12" styleClass="Wdate" readonly="true" onfocus="WdatePicker({isShowClear:true,readOnly:true})" /></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><html:textarea name="invoiceManageBean" property="rem" rows="3" cols="100" styleClass="default_textarea"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">录入人：</td>
		<td width="30%">
			<bean:write name="operName"/>
			<html:hidden name="invoiceManageBean" property="operId" />
		</td>
		<td class="wordtd" width="20%">录入日期：</td>
		<td width="30%"><html:text name="invoiceManageBean" property="operDate" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>
<script type="text/javascript"> 
</script>