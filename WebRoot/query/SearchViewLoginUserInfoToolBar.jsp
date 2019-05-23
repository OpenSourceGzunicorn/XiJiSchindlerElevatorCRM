<base target="_self">
<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<html:base />
<!--
	角色列表页工具栏
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
 <logic:notPresent name="display">
  <%--  判断登录用户模块是否有可写的权限--%>
 <%--logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nquotePre" value="Y"--%>
  AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'<bean:message key="toolbar.selectreturn"/>',"110","1","selectReturnMethod()");
  <%--/logic:equal--%>
  AddToolBarItemEx("CloseBtn","../../common/images/toolbar/close.gif","","",'<bean:message key="toolbar.close"/>',"65","1","closeMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
</logic:notPresent>
}


//查询
function searchMethod(){
	document.serveTableForm.submit();
}


//选择返回
function selectReturnMethod(){
if(serveTableForm.ids)
{
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		var returnarray = Array(l);
		var flag = false;
		var d = 0;
		for(i=0;i<l;i++)
		{
			if(document.serveTableForm.ids[i].checked == true)
			{
				returnarray[d] = new Array(4);
				returnarray[d][0] = document.serveTableForm.ids[i].value;
				returnarray[d][1] = document.serveTableForm.Sname[i].value;
				returnarray[d][2] = document.serveTableForm.Stype1[i].value;
				returnarray[d][3] = document.serveTableForm.Stype2[i].value;
				d++;
				flag = true;
			}
		}
		if(l >0 && !flag)
		{
			alert("<bean:message key='javascript.search.alert1'/>");
		}
		else
		{
		 parent.window.returnuu(returnarray);
		}
	}else if(document.serveTableForm.ids.checked == true)
	{
		var returnarray = Array(1);
		returnarray[0] = new Array(2);
		returnarray[0][0] = document.serveTableForm.ids.value;
		returnarray[0][1] = serveTableForm.Sname.value;
		returnarray[0][2] = serveTableForm.Stype1.value;
		returnarray[0][3] = serveTableForm.Stype2.value;
		parent.window.returnuu(returnarray);
	}
	else
	{
		alert("<bean:message key='javascript.search.alert1'/>");
	}
}
}
//关闭
function closeMethod(){
	window.close()
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