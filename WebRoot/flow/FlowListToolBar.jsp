<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>


<!--
	登录用户控制工具栏的显示
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");

<%--  判断登录用户模块loginUser是否有可写的权限,在property	--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nflow" value="Y">
	  AddToolBarItemEx("SetBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.configure"/>',"65","1","setMethod()");
	  AddToolBarItemEx("SetBtn","../../common/images/toolbar/edit.gif","","","配置审核权限","100","1","setshrMethod()");
   </logic:equal>
   
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
				window.location = '<html:rewrite page="/flowAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDisplayRecord';
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/flowAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDisplayRecord';
	}
	else
	{
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}
}

//修改
function setMethod(){
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
				window.location = '<html:rewrite page="/flowAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareSetRecord';
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert1"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/flowAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareSetRecord';
	}
	else
	{
		alert("<bean:message key="javascript.role.alert1"/>");
	}
}

}
//配置审核人权限
function setshrMethod(){
	if(serveTableForm.ids){
		var l = document.serveTableForm.ids.length;
		var k=document.getElementsByName("paliasname");
		if(l)
		{
			for(i=0;i<l;i++)
			{
				if(document.serveTableForm.ids[i].checked == true)
				{
					if(k[i] !=null && k[i].value !=""){
				//alert(document.serveTableForm.ids[i].value);
					window.location = '<html:rewrite page="/flowAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareSetShrRecord';
					return;
					}else{
						alert("流程名称为空，不能设置审核权限！");
						return;
					}
				}
			}
			if(l >0)
			{
				alert("<bean:message key="javascript.role.alert1"/>");
			}
		}else if(document.serveTableForm.ids.checked == true)
		{
			window.location = '<html:rewrite page="/flowAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareSetShrRecord';
		}
		else
		{
			alert("<bean:message key="javascript.role.alert1"/>");
		}
		
	}
}

</script>

<form name="toModifyPageFrm" method="POST" action="">
	<input type="hidden" name="" value=""/>
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