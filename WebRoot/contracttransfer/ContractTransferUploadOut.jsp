<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<html:errors />
<br>
<html:form action="/ContractTransferUploadAction.do?method=toOutRecord">
<html:hidden property="isreturn"/>
<html:hidden property="billNo" value="${billNo}"/>
<html:hidden property="id"/>
<html:hidden property="transfeSubmitType"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td height="23" colspan="9">&nbsp;<b>主信息</td>
	</tr>
	<tr class="wordtd_header">
		<td>流水号</td>
		<td>维保合同号</td>
		<td>销售合同号</td>
		<td>甲方单位名称</td>
		<td>电梯编号</td>
		<td>合同开始日期</td>
		<td>合同结束日期</td>
		<td>所属维保分部</td>
		<td>所属维保站</td>
	</tr>
	<logic:iterate id="element" name="masterList">
	<tr>
		<td >${element.billNo}</td>
		<td >${element.maintContractNo}</td>
		<td >${element.salesContractNo}</td> 
		<td >${element.companyId}</td>
		<td >${element.elevatorNo}</td>
		<td >${element.contractSdate}</td>
		<td >${element.contractEdate}</td>
		<td >${element.maintDivision}</td>
		<td >${element.maintStation}</td>
	</tr>
	</logic:iterate>
</table>
<br/>

<table width="100%" class="tb">
<tr>
<td class="wordtd" >上传驳回意见：</td>
<td width="83%">
<html:textarea property="transferRem" rows="3" cols="55"></html:textarea><span style="color: red">*</span>
</td>
</tr>
</table>

</html:form>


