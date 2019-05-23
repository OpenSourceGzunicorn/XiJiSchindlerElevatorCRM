<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td height="23" colspan="6">&nbsp;<b>主信息</td>
	</tr>
	<tr>
		<td class="wordtd">流水号：</td>
		<td width="22%">
			<html:text name="contractTransferMasterBean" property="billNo" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd">维保合同号：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="maintContractNo" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd">销售合同号：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="salesContractNo" readonly="true" styleClass="default_input_noborder"/>
		</td>  
	</tr>
	<tr>
		<td class="wordtd">甲方单位名称：</td>
		<td width="20%">
			${contractTransferMasterBean.companyId}
		</td>
		<td class="wordtd">电梯编号：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="elevatorNo" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd">所属维保分部：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="maintDivision" readonly="true" styleClass="default_input_noborder"/>
		</td>  
	</tr>
	<tr>
		<td class="wordtd">合同开始日期：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="contractSdate" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd">合同结束日期：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="contractEdate" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd">所属维保站：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="maintStation" readonly="true" styleClass="default_input_noborder"/>
		</td>  
	</tr>
</table>
<br/>
<table width="100%" class="tb">
<logic:iterate id="ele" name="fileTypes">
	<tr>
		<td width="15%" height="23" class="wordtd">&nbsp;上传${ele.fileTypeName}</td>
		<td>
			<logic:present name="ele" property="fileList">
			<table width="100%" border="0" cellpadding="1" cellspacing="2">
				<logic:iterate id="element2" name="ele" property="fileList">
					<tr>
					<td style="border-right-style:none;border-left-style:none;border-top-style:none;border-bottom-style:none;">
						<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}','${element2.filePath}','ContractTransferFileinfo.file.upload.folder')">${element2.oldFileName}</a>
					</td>
					</tr>
				</logic:iterate>
			</table>
			</logic:present>
		</td>
	</tr>
</logic:iterate>
</table>
<br/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td height="23" colspan="5">&nbsp;<b>反馈进度</td>
	</tr>
	<tr>
		<td width="10%" class="wordtd_header">反馈人</td>
		<td width="12%" class="wordtd_header">反馈日期</td>
		<td width="10%" class="wordtd_header">反馈问题类型</td>
		<td width="45%" class="wordtd_header">反馈内容</td>
		<td width="23%" class="wordtd_header">反馈附件</td>
	</tr>
	<logic:iterate id="ele" name="feedbackList">
	<tr>
		<td style="text-align: center;">${ele.operId}</td>
		<td style="text-align: center;">${ele.operDate}</td>
		<td style="text-align: center;">${ele.feedbacktypeid}</td>
		<td style="text-align: center;word-wrap:break-word;"><span style="width: 400px">${ele.transferRem}</span></td>
		<td>
		<table width="100%" border="0" cellpadding="1" cellspacing="2">
		<logic:present name="ele" property="fileList">
			<logic:iterate id="element2" name="ele" property="fileList">
				<tr>
					<td style="border-right-style:none;border-left-style:none;border-top-style:none;border-bottom-style:none;">
						<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}','${element2.filePath}','ContractTransferFeedbackFileinfo.file.upload.folder')">${element2.oldFileName}</a>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		</table>
		</td>
	</tr>
	</logic:iterate>
</table>

<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td height="23" colspan="6">&nbsp;<b>派工信息</td>
	</tr>
	<tr>
		<td class="wordtd">派工人：</td>
		<td width="22%">
			<html:text name="contractTransferMasterBean" property="operId" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd">派工状态：</td>
		<td width="20%">
			<logic:match name="contractTransferMasterBean" property="submitType" value="Y">已提交</logic:match>
	        <logic:match name="contractTransferMasterBean" property="submitType" value="N">未提交</logic:match>
	        <logic:match name="contractTransferMasterBean" property="submitType" value="R">驳回</logic:match>
		</td>
		<td class="wordtd">派工日期：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="operDate" readonly="true" styleClass="default_input_noborder"/>
		</td>  
	</tr>
</table>

<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td height="23" colspan="6">&nbsp;<b>上传信息</td>
	</tr>
	<tr>
		<td class="wordtd">上传人：</td>
		<td width="22%">
			<html:text name="contractTransferMasterBean" property="transfeId" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd">上传状态：</td>
		<td width="20%">
			<logic:match name="contractTransferMasterBean" property="transfeSubmitType" value="Y">已提交</logic:match>
	        <logic:match name="contractTransferMasterBean" property="transfeSubmitType" value="N">未提交</logic:match>
	        <logic:match name="contractTransferMasterBean" property="transfeSubmitType" value="R">驳回</logic:match>
		</td>
		<td class="wordtd">上传日期：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="transfeDate" readonly="true" styleClass="default_input_noborder"/>
		</td>  
	</tr>
	<tr>
		<td class="wordtd">上传驳回意见：</td>
		<td width="22%" colspan="5">
			${contractTransferMasterBean.transferRem}
		</td>
	</tr>
</table>

<logic:notEqual name="addflag2" value="Y">
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td height="23" colspan="6">&nbsp;<b>维保经理文员审核信息</td>
	</tr>
	<tr>
		<td class="wordtd">经理文员审核人：</td>
		<td width="22%">
			<html:text name="contractTransferMasterBean" property="auditOperid2" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd">经理文员审核状态：</td>
		<td width="20%">
			<logic:equal name="contractTransferMasterBean" property="auditStatus2" value="Y">已审核</logic:equal>
	        <logic:notEqual name="contractTransferMasterBean" property="auditStatus2" value="Y">未审核</logic:notEqual>
		</td>
		<td class="wordtd">经理文员审核日期：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="auditDate2" readonly="true" styleClass="default_input_noborder"/>
		</td>  
	</tr>
	<tr>
		<td class="wordtd">经理文员审核意见：</td>
		<td width="22%" colspan="5">
			${contractTransferMasterBean.auditRem2}
		</td>
	</tr>
</table>
</logic:notEqual>


<logic:notEqual name="addflag" value="Y">
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td height="23" colspan="6">&nbsp;<b>审核信息</td>
	</tr>
	<tr>
		<td class="wordtd">审核人：</td>
		<td width="22%">
			<html:text name="contractTransferMasterBean" property="auditOperid" readonly="true" styleClass="default_input_noborder"/>
		</td>
		<td class="wordtd">审核状态：</td>
		<td width="20%">
			<logic:match name="contractTransferMasterBean" property="auditStatus" value="Y">已审核</logic:match>
	        <logic:match name="contractTransferMasterBean" property="auditStatus" value="N">未审核</logic:match>
		</td>
		<td class="wordtd">审核日期：</td>
		<td width="20%">
			<html:text name="contractTransferMasterBean" property="auditDate" readonly="true" styleClass="default_input_noborder"/>
		</td>  
	</tr>
	<tr>
		<td class="wordtd">审核意见：</td>
		<td width="22%" colspan="5">
			${contractTransferMasterBean.auditRem}
		</td>
	</tr>
</table>
</logic:notEqual>

<script>

//下载附件
function downloadFile(name,oldName,filePath,folder){
	var uri = '<html:rewrite page="/ContractTransferUploadAction.do"/>?method=toDownloadFile';
	var name1=encodeURI(name);
	name1=encodeURI(name1);
	var oldName1=encodeURI(oldName);
	oldName1=encodeURI(oldName1);
	filePath=encodeURI(filePath);
	filePath=encodeURI(filePath);
	    uri +='&filePath='+ filePath;
		uri +='&filesname='+ name1;
		uri +='&folder='+folder;
		uri+='&fileOldName='+oldName1;
	window.location = uri;
	//window.open(url);
}
</script>