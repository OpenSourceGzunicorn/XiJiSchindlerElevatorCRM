  <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>


<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nqualitycheckaudit" value="Y"> 
    //AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="tollbar.checkcommer"/>',"65","1","modiyMethod()");
  </logic:equal>
  AddToolBarItemEx("ViewFlow","../../common/images/toolbar/viewdetail.gif","","",'查看审批流程',"110","1","viewFlow()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
	serveTableForm.genReport.value = "";
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
				window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toAuditDisplay';
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toAuditDisplay';
	}
	else
	{
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}
}

//查看流程
function viewFlow(){
if(serveTableForm.ids)
{
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		for(i=0;i<l;i++){
			if(document.serveTableForm.ids[i].checked == true){
				var status=document.getElementsByName("status")[i];
				if(status.value=="-1"){
					alert("流程未启动，无法查看审批流程图！");
				}else{
					var avf=document.getElementById("avf");
					var flowname=document.getElementById("flowname");
					var tokenid=document.getElementsByName("tokenid")[i];
					if(tokenid!=null && tokenid.value!=""){
						if(avf!=null && tokenid!=null && flowname!=null){
							avf.href='<html:rewrite page="/viewProcessAction.do"/>?method=toViewProcess&tokenid='+tokenid.value+'&flowname='+flowname.value;
							avf.click();
						}else{
							alert("请选择一条要查看的记录！");
						}
					}else{
						alert("该记录为历史数据，无法查看审批流程图！");
					}
				}
				return;
			}
		}
		if(l >0){
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true){
		var status=document.getElementsByName("status")[0];
		if(status.value=="-1"){
			alert("流程未启动，无法查看审批流程图！");
		}else{
			var avf=document.getElementById("avf");
			var flowname=document.getElementById("flowname");
			var tokenid=document.getElementsByName("tokenid")[0];
			if(tokenid!=null && tokenid.value!=""){
				if(avf!=null && tokenid!=null && flowname!=null){
					avf.href='<html:rewrite page="/viewProcessAction.do"/>?method=toViewProcess&tokenid='+tokenid.value+'&flowname='+flowname.value;
					avf.click();
				}else{
					alert("请选择一条要查看的记录！");
				}
			}else{
				alert("该记录为历史数据，无法查看审批流程图！");
			}
		}
	}else{
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}
}

//修改
/* function modiyMethod(){
	if(serveTableForm.ids)
	{
		var l = document.serveTableForm.ids.length;
		if(l)
		{
			for(i=0;i<l;i++)
			{
				if(document.serveTableForm.ids[i].checked == true)
				{
					var processStatus=document.serveTableForm.processStatus[i].value;
					if(processStatus!="3"){
					//alert(document.serveTableForm.ids[i].value);
						window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareAuditRecord&returnMethod='+"toSearchRecordAudit"+'&authority='+"qualitycheckaudit";
						return;
					}else{
						alert("该维保质量检查项已审核，不能进行审核！");
						
					}
					return;
				}
			}
			if(l>0)
			{
				alert("<bean:message key="javascript.role.alert1"/>");
			}
		}else if(document.serveTableForm.ids.checked == true)
		{
			var processStatus=document.serveTableForm.processStatus.value;
			if(processStatus!="3"){
				window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareAuditRecord&returnMethod='+"toSearchRecordAudit"+'&authority='+"qualitycheckaudit";
			}else{
				alert("该维保质量检查项已审核，不能进行修改！")
			}
		}
		else
		{
			alert("<bean:message key="javascript.role.alert1"/>");
		}
	}

	} */
 
</script>

  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%">
            <div id="toolbar" align="center">
            <script language="javascript">
              //<!--
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