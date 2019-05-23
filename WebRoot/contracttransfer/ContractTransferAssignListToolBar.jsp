 <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<!--
	工程网点信息工具栏
-->
<script language="javascript"> 
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  
<%--  工程网点信息工具栏模块EngUnitInfo是否有可写的权限,在property	--%>
<logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nContractTransferAssign" value="Y"><!-- -->
  AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'<bean:message key="toolbar.add"/>',"65","1","addMethod()");
  AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modiyMethod()");
  AddToolBarItemEx("SubmitBtn","../../common/images/toolbar/save.gif","","",'提交',"65","1","submitMethod()");
  AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod('N')");

  <logic:equal name="showroleid" value="A01"> 
	AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'管理员删除',"95","1","deleteMethod('Y')");
</logic:equal>
  </logic:equal>
  
  AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
	serveTableForm.genReport.value="";
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
}

//查看
function viewMethod(){
if(serveTableForm.ids)
{
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		for(i=0;i<l;i++)
		{
			if(document.serveTableForm.ids[i].checked == true)
			{
				window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDisplayRecord';
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDisplayRecord';
	}
	else
	{
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}
}

//增加
function addMethod(){
	window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?method=toPrepareAddRecord';
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
			//alert(document.serveTableForm.ids[i].value);
				if(document.getElementsByName("submittype")[i].value!="已提交"){
					
					window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareUpdateRecord';
				}else{
					alert("该数据已提交，不可修改！");
				}
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert1"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		if(document.getElementsByName("submittype")[0].value!="已提交"){
			
			window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareUpdateRecord';
	
		}else{
			alert("该数据已提交，不可修改！");
		}
	}else
	{
		alert("<bean:message key="javascript.role.alert1"/>");
	}
}

}
//提交
function submitMethod(){
if(serveTableForm.ids)
{
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		for(i=0;i<l;i++)
		{
			if(document.serveTableForm.ids[i].checked == true)
			{
			//alert(document.serveTableForm.ids[i].value);
			//alert(document.getElementsByName("submittype")[i].value)
				if(document.getElementsByName("submittype")[i].value!="已提交"){
					//alert(1)
					window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?billno='+document.serveTableForm.ids[i].value+'&isreturn=Y&submitflag=Y&method=toUpdateRecord';
				}else{
					alert("该数据已提交，不可重复提交！");
				}
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert1"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		if(document.getElementsByName("submittype")[0].value!="已提交"){
			window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?billno='+document.serveTableForm.ids.value+'&isreturn=Y&submitflag=Y&method=toUpdateRecord';
		}else{
			alert("该数据已提交，不可重复提交！");
		}
	}else
	{
		alert("<bean:message key="javascript.role.alert1"/>");
	}
}

}

//删除
function deleteMethod(valstr){
	if(serveTableForm.ids)
	{
		var l = document.serveTableForm.ids.length;
		if(l)
		{
			for(i=0;i<l;i++)
			{
				if(document.serveTableForm.ids[i].checked == true)
				{
				//alert(document.serveTableForm.ids[i].value);
					if(valstr=='N'){
						if(document.getElementsByName("submittype")[i].value!="已提交"){
							if(confirm("<bean:message key="javascript.role.deletecomfirm"/>")){
								window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDeleteRecord';
							}
						}else{
							alert("该数据已提交，不可删除！");
						}
					}else{
						if(confirm("<bean:message key="javascript.role.deletecomfirm"/>")){
							window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDeleteRecord';
						}
					}
					return;
				}
			}
			if(l >0)
			{
				alert("<bean:message key="javascript.role.alert3"/>");
			}
		}else if(document.serveTableForm.ids.checked == true)
		{
			if(valstr=='N'){
				if(document.getElementsByName("submittype")[0].value!="已提交"){
					if(confirm("<bean:message key="javascript.role.deletecomfirm"/>")){
						window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDeleteRecord';
					}
				}else{
					alert("该数据已提交，不可删除！");
				}
			}else{
				if(confirm("<bean:message key="javascript.role.deletecomfirm"/>")){
					window.location = '<html:rewrite page="/ContractTransferAssignAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDeleteRecord';
				}
			}
		}else
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

//查看报表
function reportMethod(){

}

</script>

<form name="toModifyPageFrm" method="POST" action="">
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