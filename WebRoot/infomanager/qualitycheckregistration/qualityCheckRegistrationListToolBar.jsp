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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nqualitycheckregistration" value="Y"> 
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="tollbar.register"/>',"65","1","modiyMethod()");
  </logic:equal>
  
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
				window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toRegistrationDisplay';
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toRegistrationDisplay';
	}
	else
	{
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}
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
					var processStatus=document.serveTableForm.processStatus[i].value;
					if(processStatus=="0" || processStatus=="1"){
					//alert(document.serveTableForm.ids[i].value);
						window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareRegistrationRecord';
						return;
					}else{
						alert("该维保质量检查项已登记或已审核，不能进行修改！");
						
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
			if(processStatus=="0" || processStatus=="1"){
				window.location = '<html:rewrite page="/qualityCheckManagementAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareRegistrationRecord';
			}else{
				alert("该维保质量检查项已登记或已审核，不能进行修改！")
			}
		}
		else
		{
			alert("<bean:message key="javascript.role.alert1"/>");
		}
	}

	}
 
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