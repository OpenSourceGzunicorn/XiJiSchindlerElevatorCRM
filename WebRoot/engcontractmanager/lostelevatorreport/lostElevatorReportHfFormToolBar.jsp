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

  <logic:notPresent name="workisdisplay">
	  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
	  //AddToolBarItemEx("ViewFlow","../../common/images/toolbar/viewdetail.gif","","",'查看审批流程',"110","1","viewFlow()");
	  <%-- 是否有可写的权限--%>
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nlostelevatorreporthf" value="Y">
	  	<logic:equal name="lostElevatorReportBean" property="fhRem" value="">
	  		AddToolBarItemEx("AuditBtn","../../common/images/toolbar/save.gif","","",'保存提交',"90","1","auditing('Y')");
	  	</logic:equal>
	  </logic:equal>
  </logic:notPresent>
  <logic:present name="workisdisplay">
  		AddToolBarItemEx("CloseBtn","../../common/images/toolbar/close.gif","","",'<bean:message key="toolbar.close"/>',"65","0","closeMethod()");
  </logic:present>
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//关闭
function closeMethod(){
  window.close();
}

//返回
function returnMethod(){
	window.location = '<html:rewrite page="/lostElevatorReportHfSearchAction.do"/>?method=toSearchRecord';
}

//审核
function auditing(value){
	var fhRem=document.lostElevatorReportAuditForm.fhRem.value;
	if(fhRem==""){
		alert("请填写 回访结果！");
		return;
	}

	document.getElementById("ReturnBtn").disabled='true';
	document.getElementById("AuditBtn").disabled='true';
	document.lostElevatorReportAuditForm.submit();
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