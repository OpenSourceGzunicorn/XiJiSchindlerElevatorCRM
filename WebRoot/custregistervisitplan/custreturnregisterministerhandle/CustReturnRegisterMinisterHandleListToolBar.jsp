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
  AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.handle"/>',"80","1","modiyMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
	custReturnRegisterMinisterHandleForm.genReport.value = "";
	custReturnRegisterMinisterHandleForm.target = "_self";
	document.custReturnRegisterMinisterHandleForm.submit();
}

//处理
function modiyMethod(){
	
	if(custReturnRegisterMinisterHandleForm.ids)
	{
		
		var l = document.custReturnRegisterMinisterHandleForm.ids.length;
		if(l)
		{
			
			for(i=0;i<l;i++)
			{
				
				if(document.custReturnRegisterMinisterHandleForm.ids[i].checked == true)
				{					
					window.location = '<html:rewrite page="/custReturnRegisterMinisterHandleAction.do"/>?id='+document.custReturnRegisterMinisterHandleForm.toReturnResult[i].value +'&method=toPrepareUpdateRecord';
						return;
				}

			}
			if(l >0)
			{
				alert("<bean:message key="javascript.custreturnregister.role.alert8"/>");
			}
		}else if(document.custReturnRegisterMinisterHandleForm.ids.checked == true)
		{
			window.location = '<html:rewrite page="/custReturnRegisterMinisterHandleAction.do"/>?id='+document.custReturnRegisterMinisterHandleForm.toReturnResult.value +'&method=toPrepareUpdateRecord';
		}
		else
		{
			alert("<bean:message key="javascript.custreturnregister.role.alert8"/>");
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