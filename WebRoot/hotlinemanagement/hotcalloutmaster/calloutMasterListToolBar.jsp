<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig"%>
<!--
	客户地区表页工具栏
-->
<script language="javascript">

//关于ToolBar
function CreateToolBar() {
	SetToolBarHandle(true);
	SetToolBarHeight(20);

	AddToolBarItemEx("SearchBtn", "../../common/images/toolbar/search.gif", "","", '<bean:message key="toolbar.search"/>', "65", "0","searchMethod()");
	AddToolBarItemEx("ViewBtn", "../../common/images/toolbar/view.gif", "", "",'<bean:message key="toolbar.read"/>', "65", "1", "viewMethod()");
	 <%-- 是否有可写的权限--%>
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nhotphone" value="Y"> 
	AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'<bean:message key="toolbar.add"/>',"65","1","addMethod()");
	AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modiyMethod()");
	AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod()");
    AddToolBarItemEx("ExcelBtn", "../../common/images/toolbar/xls.gif", "", "",'<bean:message key="toolbar.xls"/>', "90", "1", "excelMethod()");
    AddToolBarItemEx("ExcelBtn", "../../common/images/toolbar/edit.gif", "", "",'停梯回访', "90", "1", "stopHFMethod()");
    </logic:equal>
    AddToolBarItemEx("ExcelBtn", "../../common/images/toolbar/print.gif", "", "",'打印记录单', "90", "1", "printMethod()");
	window.document.getElementById("toolbar").innerHTML = GenToolBar("TabToolBar_Manage", "TextToolBar_Black", "Style_Over",
			"Style_Out", "Style_Down", "Style_Check");
}

//查询
function searchMethod() {
	serveTableForm.genReport.value = "";
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
}

//登记停梯回访备注
function stopHFMethod(){
	var index = checkedIndex();
	if(index >= 0){	
		var hisStop=document.getElementsByName("hisStop")[index].value;//停梯状态
		if(hisStop=="停梯"){
			window.location = '<html:rewrite page="/hotphoneAction.do"/>?method=toPrepareStophfRecord&id='+document.getElementsByName("ids")[index].value;
			return;
		}else{
			alert("请选择停梯的急修单，进行停梯回访！"); 
			return;
		}
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");		
	}
}

//查看
function viewMethod() {
if(serveTableForm.ids)
{
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		for(i=0;i<l;i++)
		{
			if(document.serveTableForm.ids[i].checked == true)
			{
				window.location = '<html:rewrite page="/hotphoneAction.do"/>?method=toDisplayRecord&id='+document.serveTableForm.ids[i].value;
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/hotphoneAction.do"/>?method=toDisplayRecord&id='+document.serveTableForm.ids.value;
	}
	else
	{
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}
}

//增加
function addMethod(){
	window.location = '<html:rewrite page="/hotphoneAction.do"/>?method=toPrepareAddRecord';
}
//修改
function modiyMethod(){
if(serveTableForm.ids)
{
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		for(i=0;i<l;i++)
		{
			if(document.serveTableForm.ids[i].checked == true)
			{		
				if(document.serveTableForm.submitType[i].value == "未提交")
				{	
				window.location = '<html:rewrite page="/hotphoneAction.do"/>?method=toPrepareUpdateRecord&id='+document.serveTableForm.ids[i].value;
				return;		
				}else{
					alert("此记录已提交，请选择未提交的记录！");
					return;
				}
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert1"/>");
		}
	}else if(document.serveTableForm.ids.checked == true){
	
		if(document.serveTableForm.submitType.value == "未提交")
		{
		window.location = '<html:rewrite page="/hotphoneAction.do"/>?method=toPrepareUpdateRecord&id='+document.serveTableForm.ids.value;
		}else{
			alert("此记录已提交，请选择未提交的记录！");
			return;
		}
		}
	else {
		alert("<bean:message key="javascript.role.alert1"/>");
	}
}

}

//打印
function printMethod(){
	var index = checkedIndex();
	if(index >= 0){	
		var handleStatus=document.getElementsByName("handleStatus")[index].value;//提交状态
		if(handleStatus == "5"||handleStatus == "6"||handleStatus=="7"){
		window.open('<html:rewrite page="/hotphoneAction.do"/>?id='+document.getElementsByName("ids")[index].value+'&method=toPreparePrintRecord');

		}else{
			alert("处理状态为已录入报告书后的,才可以打印"); 
			return;
		}
		
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");		
	}
}

//删除
function deleteMethod(){
if(serveTableForm.ids)
{
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		for(i=0;i<l;i++)
		{
			if(document.serveTableForm.ids[i].checked == true)
			{
				if(document.serveTableForm.submitType[i].value == "未提交")
				{
				if(confirm("<bean:message key="javascript.role.deletecomfirm"/>"+document.serveTableForm.ids[i].value)){
					window.location = '<html:rewrite page="/hotphoneAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDeleteRecord';
					
				}
				return;
				}else{
					alert("此记录已提交，请选择未提交的记录！");
					return;
				}
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert3"/>");
		}
	}
	else if(document.serveTableForm.ids.checked == true)
	{
		if(document.serveTableForm.submitType.value == "未提交")
		{
		if(confirm("<bean:message key="javascript.role.deletecomfirm"/>"+document.serveTableForm.ids.value)){
			window.location = '<html:rewrite page="/hotphoneAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDeleteRecord';
		}
		return;
		}else{
			alert("此记录已提交，请选择未提交的记录！");
			return;
		}
	}
	else
	{
		alert("<bean:message key="javascript.role.alert3"/>");
	}
}
}

//导出Excel
function excelMethod(){
	serveTableForm.genReport.value="Y";
	serveTableForm.target = "_blank";
	document.serveTableForm.submit();
}

function checkedIndex(){
	if(document.getElementsByName("ids").length){	
		var ids = document.getElementsByName("ids");
		for(var i=0;i<ids.length;i++){
		  if(ids[i].checked == true){
		    return i;
		  }
		}				
	}
	return -1;	
}

</script>

<form name="CalloutMasterjxForm" method="POST" action="">
	<input type="hidden" name="" value="" />
</form>

<tr>
	<td width="100%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="22" class="table_outline3" valign="bottom" width="100%">
					<div id="toolbar" align="center">

						<script language="javascript">
<!--
                CreateToolBar();
              //-->
            </script>
					</div>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>