<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()"); 
  <logic:notPresent name="display">  
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
</logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/logManagementSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
  
	
	var salesContractNo=document.getElementById("salesContractNo").value;
	var projectName=document.getElementById("projectName").value;
	if(salesContractNo==""){
		alert("销售合同号不能为空!");
		return;
	}
    if(projectName==""){
    	alert("项目名称不能为空!");
		return;
	}
	
	
    document.logManagementForm.isreturn.value = "N";
    document.logManagementForm.submit();
  
}

//保存返回
function saveReturnMethod(){
 
	
	var salesContractNo=document.getElementById("salesContractNo").value;
	var projectName=document.getElementById("projectName").value;
	if(salesContractNo==""){
		alert("销售合同号不能为空!");
		return;
	}
    if(projectName==""){
    	alert("项目名称不能为空!");
		return;
	}
    document.logManagementForm.isreturn.value = "Y";
    document.logManagementForm.submit();
  
}


</script>

  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%">
            <div id="toolbar" align="center">
            <script language="javascript">
              CreateToolBar();
            </script>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>