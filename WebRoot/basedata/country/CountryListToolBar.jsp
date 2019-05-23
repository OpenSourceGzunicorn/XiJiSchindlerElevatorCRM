 <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<!--
	客户地区表页工具栏
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  
<%-- 是否有可写的权限--%>
<logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncountry" value="Y"> 
  AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'<bean:message key="toolbar.add"/>',"65","1","addMethod()");
  AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modiyMethod()");
  AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod()");
</logic:equal>
  
 // AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
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
				window.location = '<html:rewrite page="/countryAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDisplayRecord';
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/countryAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDisplayRecord';
	}
	else
	{
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}
}

//增加
function addMethod(){
window.location = '<html:rewrite page="/countryAction.do"/>?method=toPrepareAddRecord';
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
				window.location = '<html:rewrite page="/countryAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toPrepareUpdateRecord';
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert1"/>");
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		window.location = '<html:rewrite page="/countryAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toPrepareUpdateRecord';
	}
	else
	{
		alert("<bean:message key="javascript.role.alert1"/>");
	}
}

}

//删除
function deleteMethod(){
if(serveTableForm.ids)
{
	//alert(document.serveTableForm.ids);
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		for(i=0;i<l;i++)
		{
			if(document.serveTableForm.ids[i].checked == true)
			{
				//alert(document.serveTableForm.ids[i].value);
				if(confirm("<bean:message key="javascript.role.deletecomfirm"/>"+document.serveTableForm.ids[i].value))
				{
					window.location = '<html:rewrite page="/countryAction.do"/>?id='+document.serveTableForm.ids[i].value +'&method=toDeleteRecord';
				}
				return;
			}
		}
		if(l >0)
		{
			alert("<bean:message key="javascript.role.alert3"/>");
		}
	}
	else if(document.serveTableForm.ids.checked == true)
	{
		if(confirm("<bean:message key="javascript.role.deletecomfirm"/>"+document.serveTableForm.ids.value))
			{
				window.location = '<html:rewrite page="/countryAction.do"/>?id='+document.serveTableForm.ids.value +'&method=toDeleteRecord';
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

//查看报表
function reportMethod(){

}

</script>

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