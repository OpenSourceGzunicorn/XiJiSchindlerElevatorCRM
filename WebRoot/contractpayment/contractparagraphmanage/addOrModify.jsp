<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="contractParagraphManageBean" property="jnlNo"/>

<html:hidden name="contractParagraphManageBean" property="billNo"/>
<html:hidden name="contractParagraphManageBean" property="submitType"/>
<html:hidden name="contractParagraphManageBean" property="auditStatus"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">合同号：</td>
		<td width="20%"><html:text name="contractParagraphManageBean" property="contractNo" readonly="true" styleClass="default_input_noborder" /></td>
		<td class="wordtd" width="15%">甲方单位名称：</td>
		<td width="20%">
			<bean:write name="companyName" />
			<html:hidden name="contractParagraphManageBean" property="companyId" />
		</td>
		<td class="wordtd" width="15%">合同类型：</td>
		<td width="20%">
			<logic:match name="contractParagraphManageBean" property="contractType" value="B">保养</logic:match>
			<logic:match name="contractParagraphManageBean" property="contractType" value="W">维修</logic:match>
			<logic:match name="contractParagraphManageBean" property="contractType" value="G">改造</logic:match>
			<html:hidden name="contractParagraphManageBean" property="contractType" />
		</td>
	</tr>
	<tr>
		<td class="wordtd">所属分部：</td>
		<td>
			<bean:write name="maintDivisionName"/>
			<html:hidden name="contractParagraphManageBean" property="maintDivision" />
		</td>
		<td class="wordtd">所属维保站：</td>
		<td>
			<bean:write name="maintStationName"/>
			<html:hidden name="contractParagraphManageBean" property="maintStation" />
		</td>
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
		<td width="20%">
			<html:text name="contractParagraphManageBean" property="paragraphNo" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd" width="15%">来款金额：</td>
		<td width="20%"><html:text name="contractParagraphManageBean" property="paragraphMoney" styleId="paragraphMoney" styleClass="default_input" onkeypress="f_check_number3();"  onkeydown="judgePreMoney(this,'${contractBean.arrearsMoney}');" onkeyup="judgePreMoney(this,'${contractBean.arrearsMoney}');" /><font color="red">*</font></td>
		<td class="wordtd" width="15%">来款日期：</td>
		<td width="20%"><html:text name="contractParagraphManageBean" property="paragraphDate" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" /></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><html:textarea name="contractParagraphManageBean" property="rem" rows="3" cols="100" styleClass="default_textarea"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">录入人：</td>
		<td width="20%">
			<bean:write name="operName"/>
			<html:hidden name="contractParagraphManageBean" property="operId" />
		</td>
		<td class="wordtd" width="15%">录入日期：</td>
		<td><html:text name="contractParagraphManageBean" property="operDate" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>

<script type="text/javascript"> 
</script>