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


  
    AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");

  <logic:notPresent name="display">  
	  <%-- 是否有可写的权限--%>
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintenanceworkplanaudit" value="Y"> 
	  <logic:notEqual name="ischeckstr" value="Y">
	      AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'审 核',"65","1","saveReturnMethod('Y')");
	      AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save_back.gif","","",'驳 回',"65","1","saveReturnMethod('N')");
	  </logic:notEqual>
	  </logic:equal>
  </logic:notPresent>
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");

}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/maintenanceWorkPlanAuditSearchAction.do"/>?method=toSearchRecord';
}


//保存返回
function saveReturnMethod(flag){
	  
	  var checkflag=document.getElementsByName("checkflag");
	  if(checkflag[0].value=="Y")
	  {
		  alert("该合同作业计划已被审核,不能重复审核！")
			return;
	  }
      document.maintenanceWorkPlanAuditForm.isreturn.value = flag;
      document.maintenanceWorkPlanAuditForm.submit();
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