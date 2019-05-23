<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/common/css/jquery.bigautocomplete.css"/>">
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery.bigautocomplete.js"/>"></script>
<br>
<head>
  <title>XJSCRM</title>
</head>

  <html:errors/>
  <html:form action="/ContractTransferAssignAction.do?method=toUpdateRecord" enctype="multipart/form-data">
	<html:hidden property="isreturn" value=""/>


<table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
		<tr height="23">
			<td colspan="13">
				
  				<b>&nbsp;派工电梯</b>
			</td>
		</tr>
	<logic:iterate id="elementcm" name="cmlist">
		<tr>
			<td class="wordtd" width="10%">流水号：</td>
			<td width="23%"><bean:write name="elementcm" property="billno"/>
			<input type="hidden" name="billno" value='<bean:write name="elementcm" property="billno"/>'/>
			</td>
			<td class="wordtd" width="10%">甲方单位名称：</td>
			<td width="56%" colspan="3"><bean:write name="elementcm" property="companyname"/></td>
		</tr>
		<tr>
			<td class="wordtd" width="10%">维保合同号：</td>
			<td width="23%"><bean:write name="elementcm" property="maintcontractno"/></td>
			<td class="wordtd" width="10%">销售合同号：</td>
			<td width="23%"><bean:write name="elementcm" property="salescontractno"/></td>
			<td class="wordtd" width="10%">电梯编号：</td>
			<td width="23%"><bean:write name="elementcm" property="elevatorno"/>
			<input type="hidden" name="elevatorno" value='<bean:write name="elementcm" property="elevatorno"/>'/>
			</td>
		</tr>
		<tr>
			<td class="wordtd" width="10%">合同开始日期：</td>
			<td width="23%"><bean:write name="elementcm" property="contractsdate"/></td>
			<td class="wordtd" width="10%">所属维保分部：</td>
			<td width="23%"><bean:write name="elementcm" property="comfullname"/></td>
			<td class="wordtd" width="10%">所属维保站：</td>
			<td width="23%"><bean:write name="elementcm" property="storagename"/></td>
		</tr>
		<tr>
			<td class="wordtd" width="10%">合同结束日期：</td>
			<td width="23%"><bean:write name="elementcm" property="contractedate"/></td>
			<td class="wordtd" width="10%">派工人：</td>
			<td width="23%"><bean:write name="elementcm" property="opname"/></td>
			<td class="wordtd" width="10%">派工日期：</td>
			<td width="23%"><bean:write name="elementcm" property="operdate"/></td>
		</tr>
   <tr>
   		<td class="wordtd" width="10%">附件类型：</td>
   		<td id="filetypetd" colspan="5" width="89%">
   		<logic:iterate id="element" name="filetypelist">
   		<input type="checkbox" name="filetypechoose" id="filetypechoose<bean:write name="element" property="id.pullid"/>" value='<bean:write name="element" property="id.pullid"/>' onclick="checktype(this)" /><bean:write name="element" property="pullname"/>&nbsp;&nbsp;
   		</logic:iterate>
   		</td>
   </tr>
   <logic:equal name="elementcm" property="submittype" value="R">
		<tr>
			<td class="wordtd" width="10%">驳回意见：</td>
			<td width="23%"><bean:write name="elementcm" property="transferrem"/></td>
			<td class="wordtd" width="10%">驳回人：</td>
			<td width="23%"><bean:write name="elementcm" property="upname"/></td>
			<td class="wordtd" width="10%">驳回日期：</td>
			<td width="23%"><bean:write name="elementcm" property="transfedate"/></td>
		</tr>
	</logic:equal>
	</logic:iterate>
</table> 
<br/>
<table width="100%" class="tb">
	<tr>
		<td height="23" width="13%" style="text-align:center;background-color:#b8c4f4;" rowspan="2">派工附件上传</td>
		<logic:present name="fileList">
		<td>
		<table width="100%" border="0" cellpadding="1" cellspacing="2">
			<logic:iterate id="element2" name="fileList">
				<tr>
				<td style="border-right-style:none;border-left-style:none;border-top-style:none;border-bottom-style:none;">
					<input type="hidden" name="file" value="${element2.filesid}"/>
					<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newfilename}','${element2.oldfilename}','${element2.filepath}','ContractTransferDebugFileinfo.file.upload.folder')">${element2.oldfilename}</a>
					<a style="cursor:hand;" onclick="deleteFile(this,'${element2.filesid}','${element2.filepath}')"><img src="../../common/images/toolbar/del_attach.gif" alt="<bean:message key="delete.button.value"/>" /></a>
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
					<input type="checkbox" onclick="checkTableAll(this)">
				</td>
				<td align="left">&nbsp;&nbsp;<input type="button" name="toaddrow" value="+" onclick="AddRow(this,'1')"/>
				&nbsp;<input type="button" name="todelrow" value="-" onclick="deleteRow(this)">
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<br>

  </html:form>
<script>
<logic:iterate id="elementcf" name="cflist">
		document.getElementById("filetypechoose<bean:write name="elementcf" property="filetype"/>").checked="checked"
		document.getElementById("filetypechoose<bean:write name="elementcf" property="filetype"/>").onclick();
</logic:iterate>
</script>