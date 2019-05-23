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
<html:form action="/ContractTransferUploadAction.do?method=toAddRecord" enctype="multipart/form-data">
<html:hidden property="isreturn" value="N"/>
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
<logic:iterate id="ele" name="fileTypes">
<table width="100%" class="tb">
	<tr>
		<td height="23" class="wordtd" rowspan="2">&nbsp;<b>上传${ele.fileTypeName}</td>
		<logic:present name="ele" property="fileList">
		<td>
		<table width="100%" border="0" cellpadding="1" cellspacing="2">
			<logic:iterate id="element2" name="ele" property="fileList">
				<tr>
				<td style="border-right-style:none;border-left-style:none;border-top-style:none;border-bottom-style:none;">
					<input type="hidden" name="file${ele.jnlno}" value="${element2.fileSid}"/>
					<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}','${element2.filePath}','ContractTransferFileinfo.file.upload.folder')">${element2.oldFileName}</a>
					<a style="cursor:hand;" onclick="deleteFile(this,'${element2.fileSid}','${element2.filePath}')"><img src="../../common/images/toolbar/del_attach.gif" alt="<bean:message key="delete.button.value"/>" /></a>
				</td>
				</tr>
			</logic:iterate>
		</table>
		</td>
		</logic:present>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
			<tr>
				<td width="5%" align="center">
					<input type="checkbox" onclick="checkTableFileAll(this)">
					<html:hidden property="jnlno" value="${ele.jnlno}"/>
					<html:hidden property="fileType" value="${ele.fileType}"/>
					<html:hidden property="fileFlag" value="${ele.fileFlag}"/>
				</td>
				<td align="left">&nbsp;&nbsp;<input type="button" name="toaddrow" value="+" onclick="AddRow(this,'${ele.jnlno}')"/>
				&nbsp;<input type="button" name="todelrow" value="-" onclick="deleteFileRow(this)">
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<br/>
</logic:iterate>

</html:form>

