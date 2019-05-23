<%@ page contentType="text/html; charset=GBK" %>
<br/>
<!-- 已上传的附件 -->
<div id="filemessagestring"></div>
<table width="100%" class="tb">
	<tr>
		<td>已上传的附件</td>
	</tr>
</table>
<table id="uploadtab_1" width="100%" class="tb">
	<thead>
		<tr>
			<td class="wordtd_k2"><div align="center">文件名称</div></td>
			<td class="wordtd_k2"><div align="center">上传人</div></td>
			<td class="wordtd_k2"><div align="center">上传日期</div></td>
			<td class="wordtd_k2"><div align="center">操作</div></td>
		</tr>
	</thead>
	<logic:present name="updatefileList">
	
		<logic:empty name="updatefileList">
			<tr>
				<td colspan="9" align="center">没有记录！</td>
			</tr>
		</logic:empty>
		

		<logic:notEmpty name="updatefileList">
			<logic:iterate id="ele"  name="updatefileList">
				<tr>
				<td class=""><bean:write name="ele" property="oldfilename" /></td>
				<td class=""><bean:write name="ele" property="uploadername" /></td>
				<td class=""><bean:write name="ele" property="uploaddate" /></td>
				<td align="center">
					<input type="button" name="display" value="查看"  class="default_input"  onclick="downloadFile('${ele.filesid}')">
					&nbsp;
					<input type="button" name="del" value="删除"  class="default_input" onclick="deleteFile('${ele.filesid}',this)">
				</td>
				</tr>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>
	
</table>


