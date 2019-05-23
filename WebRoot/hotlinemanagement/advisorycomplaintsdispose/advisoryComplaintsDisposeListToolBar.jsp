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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nadvisorycomplaintsdispose" value="Y"> 
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="tollbar.dispose"/>',"65","1","modiyMethod()");
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
				window.location = '<html:rewrite page="/advisoryComplaintsManageAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDisposeDisplay&returnMethod='+"toSearchRecordDispose";
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/advisoryComplaintsManageAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDisposeDisplay&returnMethod='+"toSearchRecordDispose";
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
					var processType=document.serveTableForm.processType[i].value;
					if(processType!="Y"){
					//alert(document.serveTableForm.ids[i].value);
						window.location = '<html:rewrite page="/advisoryComplaintsManageAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareDisposeRecord&returnMethod='+"toSearchRecordDispose"+'&authority='+"advisorycomplaintsdispose";
						return;
					}else{
						alert("该咨询投诉项已登记，不能进行修改！");
						
					}
					return;
				}
			}
			if(l>0)
			{
				alert("请选择一条要处理的记录！");
			}
		}else if(document.serveTableForm.ids.checked == true)
		{
			var processType=document.serveTableForm.processType.value;
			if(processType!="Y"){
				window.location = '<html:rewrite page="/advisoryComplaintsManageAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareDisposeRecord&returnMethod='+"toSearchRecordDispose"+'&authority='+"advisorycomplaintsdispose";
			}else{
				alert("该咨询投诉项已登记，不能进行修改！")
			}
		}
		else
		{
			alert("请选择一条要处理的记录！");
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