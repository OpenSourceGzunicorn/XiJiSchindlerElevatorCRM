<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  <logic:equal name="typejsp" value="Yes">
    AddToolBarItemEx("SaveSubmitBtn","../../common/images/toolbar/close.gif","","",'关闭',"60","0","window.close()");
  </logic:equal>
  <logic:notPresent name="typejsp">
    AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  </logic:notPresent>
  <logic:notPresent name="display">  
	  <%-- 是否有可写的权限--%>
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintenanceworkplancustomize" value="Y"> 
	    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod('N')");   
	    <logic:notPresent name="doType">
	      AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'保存并提交返回',"120","1","saveReturnMethod()");
	    </logic:notPresent>

	    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'保存定制备注',"120","1","saveMethod('Y')");  
	  </logic:equal>
  </logic:notPresent>
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");

}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/maintenanceWorkPlanCustomizeSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(isrem){
  if(checkInput()){
	  if(isrem=="Y"){
		  var customizeRem=document.getElementById("customizeRem").value;
		  if(customizeRem==""){
			  alert("定制备注，不能为空！");
			  return;
		  }
	  }
	 document.getElementById("issaverem").value=isrem;
     document.maintenanceWorkPlanCustomizeForm.isreturn.value = "N";
     document.maintenanceWorkPlanCustomizeForm.submit();
  }
  
}

//保存返回
function saveReturnMethod(){
 //
  if(checkInput()){  
      document.maintenanceWorkPlanCustomizeForm.isreturn.value = "Y";
      document.maintenanceWorkPlanCustomizeForm.submit();
    } 
}


function checkInput()
{
	var mainSdates=document.getElementsByName("mainSdate");
	var mainEdates=document.getElementsByName("mainEdate");
	var isReturn=true;
	for(var i = 0;i < mainSdates.length; i++)
	{
		if(mainSdates[i].value=="")
		{
			alert("第"+(i+1)+"行,保养开始时间为空");
			isReturn=false;
			return;
		}
		if(mainEdates[i].value=="")
		{
			alert("第"+(i+1)+"行,保养结束时间为空");
			isReturn=false;
			return;
		}
	}
	
  return  isReturn; 
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