<%@ page contentType="text/html;charset=GBK"%>

<br/>
<!-- 要上传的附件 -->
<table width="100%" class="tb">
	<tr>
		<td><b>上传附件</b></td>
	</tr>
</table>
<table  width="100%" class="tb">
	<tr>
		<td class="td_1" colspan="8">
			<input type="button" name="toaddrow" class="default_input" value="增加" onclick="AddRow(uploadtab_2)">
			&nbsp&nbsp
			<input type="button" name="todelrow" value="删除" onclick="delSelNode2(uploadtab_2)" class="default_input">
			 &nbsp;&nbsp;
		</td>
	</tr>
</table>
<table id="uploadtab_2" width="100%" class="tb">
	<thead>
	<tr>
		<td width="5%" class="wordtd_k2">
			<div align="center">
				<input type="checkbox" name="selAll2" onclick="selAllNode(this,'nodes')">
			</div>
		</td>
		<td class="wordtd_k2"><div align="center">附件</div></td>
	</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<%@ include file="UpLoadJS.js" %>

