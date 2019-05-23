<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="proContractArfeeMasterBean" property="jnlNo"/>
<%-- <html:hidden property="billno"/> --%>
<!-- <input type="hidden" name="billno"/> -->
<html:hidden name="proContractArfeeMasterBean" property="billNo"/>
<html:hidden name="proContractArfeeMasterBean" property="contractNo"/>
<html:hidden name="proContractArfeeMasterBean" property="submitType"/>
<html:hidden name="proContractArfeeMasterBean" property="auditStatus"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">合同号：</td>
		<td width="20%">
		<a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="proContractArfeeMasterBean"  property="billNo"/>" target="_blnk"> 
			<bean:write name="proContractArfeeMasterBean" property="contractNo"/>
		</a>
		</td>
		<td class="wordtd" width="15%">甲方单位名称：</td>
		<td width="20%">
			<bean:write name="companyName" />
			<html:hidden name="proContractArfeeMasterBean" property="companyId" />
		</td>
		<td class="wordtd" width="15%">合同类型：</td>
		<td width="20%">
			<logic:match name="proContractArfeeMasterBean" property="contractType" value="B">保养</logic:match>
			<logic:match name="proContractArfeeMasterBean" property="contractType" value="W">维修</logic:match>
			<logic:match name="proContractArfeeMasterBean" property="contractType" value="G">改造</logic:match>
			<html:hidden name="proContractArfeeMasterBean" property="contractType" />
		</td>
	</tr>
	<tr>
		<td class="wordtd">所属分部：</td>
		<td>
			<bean:write name="maintDivisionName"/>
			<html:hidden name="proContractArfeeMasterBean" property="maintDivision" />
		</td>
		<td class="wordtd">所属维保站：</td>
		<td>
			<bean:write name="maintStationName"/>
			<html:hidden name="proContractArfeeMasterBean" property="maintStation"/>
		</td>
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
		<td colspan="5"><html:text name="proContractArfeeMasterBean" property="maintScope" styleId="maintScope" styleClass="default_input" size="40"/><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="wordtd" width="15%">应收款名称：</td>
		<td width="20%">
			<html:select name="proContractArfeeMasterBean" property="recName">
				<html:option value="">请选择</html:option>
		    	<html:options collection="receivablesList" property="recId" labelProperty="recName"/>
        	</html:select><font color="red">*</font>
			<%-- <html:text name="contractInvoiceManageBean" property="invoiceType" styleClass="default_input" /> --%>
		</td>
		<td class="wordtd" width="15%">应收款金额：</td>
		<td width="20%"><html:text name="proContractArfeeMasterBean" property="preMoney" styleId="preMoney" styleClass="default_input" onkeydown="judgePreMoney(this,'${noBuiltReceivables}');" onkeyup="judgePreMoney(this,'${noBuiltReceivables}');" /><font color="red">*</font></td>
		<td class="wordtd" width="15%">应收款日期：</td>
		<td width="20%"><html:text name="proContractArfeeMasterBean" property="preDate" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" /></td>
	</tr>
	<tr>
		<td class="wordtd">备注：</td>
		<td colspan="5"><html:textarea name="proContractArfeeMasterBean" property="rem" rows="3" cols="100" styleClass="default_textarea"/></td>
	</tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="15%">录入人：</td>
		<td width="20%">
			<bean:write name="operName"/>
			<html:hidden name="proContractArfeeMasterBean" property="operId" />
		</td>
		<td class="wordtd" width="15%">录入日期：</td>
		<td><html:text name="proContractArfeeMasterBean" property="operDate" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>

<br/>
<logic:present name="proList">
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;已建应收款</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="15%">应收款流水号</td>
			<td width="15%">应收款名称</td>
			<td width="15%">应收款金额</td>
			<td width="15%">应收款日期</td>
			<td width="15%">维保区间</td>
			<td width="40%">备注</td>
		</tr>
	</thead>
	<tfoot>
		<tr height="15"><td colspan="6"></td></tr>
	</tfoot>
	<tbody>
          <logic:iterate id="pro" name="proList" >
          	<tr>
				<td align="center"><bean:write name="pro" property="jnlNo"/></td>
				<td align="center"><bean:write name="pro" property="recName"/></td>
				<td align="center"><bean:write name="pro" property="preMoney"/></td>
				<td align="center"><bean:write name="pro" property="preDate"/></td>
				<td align="center"><bean:write name="pro" property="maintScope"/></td>
				<td><bean:write name="pro" property="rem"/></td>
			</tr>
          </logic:iterate>
	</tbody>
</table>
</logic:present>

<script type="text/javascript"> 
</script>