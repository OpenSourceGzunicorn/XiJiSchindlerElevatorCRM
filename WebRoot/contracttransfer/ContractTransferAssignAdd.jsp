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
  <html:form action="/ContractTransferAssignAction.do?method=toAddRecord"  enctype="multipart/form-data">
	<html:hidden property="isreturn" value=""/>


<table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr height="23">
			<td colspan="13">
				&nbsp;&nbsp;<input type="button" name="toaddrow" value=" + " onclick="addElevator2('ContractTransferAssignSearchAction',this)"/>
				&nbsp;<input type="button" name="todelrow" value=" - " onclick="deleteRow(this)">
  				<b>&nbsp;派工电梯</b>
			</td>
		</tr>
		<tr class="wordtd_header">
			<td width="5"><input type="checkbox" onclick="checkTableAll(this)"></td>
			<td>甲方单位名称</td>
			<td>维保合同号</td>
			<td>销售合同号</td>
			<td>电梯编号</td>
			<td>所属维保分部</td>
			<td>所属维保站</td>
			<td>合同开始日期</td>
			<td>合同结束日期</td>
		</tr>
	</thead>
	<tbody>
		<tr height="15"><td colspan="10"></td></tr>
	</tbody>
	<!-- <tfoot>
      <tr height="15"><td colspan="12"></td></tr>
    </tfoot> -->
</table>

<br/>
<table width="100%"  class="tb">
   <tr>
   		<td class="wordtd_header">附件类型</td>
   		<td id="filetypetd">
   		<logic:iterate id="element" name="filetypelist">
   		<logic:notEqual name="element" property="id.pullid" value="D">
   		<input type="checkbox" name="filetypechoose" value='<bean:write name="element" property="id.pullid"/>' onclick="checktype(this)" /><bean:write name="element" property="pullname"/>
		</logic:notEqual>
   		<logic:equal name="element" property="id.pullid" value="D">
   		<input type="checkbox" name="filetypechoose" value='<bean:write name="element" property="id.pullid"/>' onclick="checktype(this)" checked="checked" /><bean:write name="element" property="pullname"/>
   		<input type='hidden' name='filetype' id='filetype<bean:write name="element" property="id.pullid"/>' value='<bean:write name="element" property="id.pullid"/>'
   		</logic:equal>
   		</logic:iterate>
   		</td>
   </tr>
</table> 
<br/>
<table width="100%" class="tb">
	<tr>
		<td height="23" width="13%" style="text-align:center;background-color:#b8c4f4;" rowspan="2">派工附件上传</td>
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
