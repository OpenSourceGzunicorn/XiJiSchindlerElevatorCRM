<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<br>
<!-- pdf导出中文用 -->
<style type="text/css">
		*{
			font-family: SongTi_GB2312;
		}
</style>
<style type="text/css">
<%--	用于pdf导出的样式--%>
.divtable table{border-collapse: collapse;border: 1px outset #999999;background-color: #FFFFFF;}
.divtable table td{font-size: 12px;border: 1px outset #999999;}
</style>
<html:errors/>
<logic:present name="pulldownBean">
	<body>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"  class=tb>
			<tr>
				<td width="20%"  class="wordtd">
					代码：
				</td>
				<td width="80%" >
				    <bean:write name="pulldownBean" property="pullid"/>
				</td>
			</tr>
			<tr>
				<td class="wordtd">
					名称：
				</td>
				<td>
					<bean:write name="pulldownBean" property="pullname"/>
				</td>
			</tr>
			<tr>
				<td class="wordtd">
					排序号：
				</td>
				<td>
				    <bean:write name="pulldownBean" property="orderby"/>
				</td>
			</tr>
			<tr>
				<td class="wordtd">
					类型：
				</td>
				<td>
				    <bean:write name="pulldownBean" property="typeflag"/>
				</td>
			</tr>
		  <tr>
		    <td class="wordtd"><bean:message key="loginuser.enabledflag"/>：</td>
		    <td>${pulldownBean.enabledflag=='Y'?'是':'否' }</td>	 
		 </tr>
			<tr>
				<td class="wordtd">
					描述：
				</td>
				<td>
				    <bean:write name="pulldownBean" property="pullrem"/>
				</td>
			</tr>
		</table>
	</body>
</logic:present>
<script>
	/**
	*页面加载完就保存html文件
	*/
	$(function(){
		var url1 = '<html:rewrite page="/PullDownAction.do"/>';
		 var test=document.getElementsByTagName('html')[0].innerHTML; 
		 var top=$("table.top_navigation").parent().html();	
		 var tabToolBar=$("td.table_outline3").html();
		 	$.ajax({
					url : url1,// 要请求的action
				data : {
					method : "createPdfFile",
					test : test,
					top : top,
					tabToolBar : tabToolBar,
				    htmlName : "PullDown.html"
				},// 请求的参数和方法
				type : "POST",
				dataType : "text",// 是什么数据类型
				async : "false",// 是否异步请求，如果是异步，则服务器不会等待这个方法返回就执行完了这个函数
				cache : "false",// 是否进行缓存
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				success : function(arr) {

				},
				error: function(){
					
				}
			});
	})
</script>
