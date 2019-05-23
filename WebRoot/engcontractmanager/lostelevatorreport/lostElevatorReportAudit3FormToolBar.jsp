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
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nlostelevatorreportaudit3" value="Y">
	  	<logic:notEqual name="lostElevatorReportBean" property="auditStatus3" value="Y">
	  	AddToolBarItemEx("AuditBtn","../../common/images/toolbar/save.gif","","",'审核通过',"90","1","auditing('Y')");
	  	AddToolBarItemEx("RejectBtn","../../common/images/toolbar/delete.gif","","",'驳回',"65","1"," auditing('N')");
	  	</logic:notEqual>
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
	window.location = '<html:rewrite page="/lostElevatorReportAudit3SearchAction.do"/>?method=toSearchRecord';
}

//审核
function auditing(value){
	var submitType=document.lostElevatorReportAuditForm.submitType.value;
	var auditStatus3=document.lostElevatorReportAuditForm.auditStatus3.value;
	if(submitType!="Y"){
		alert("此记录没有提交不能审核！");
		return;
	}
	if(auditStatus3=="Y"){
		alert("此记录已经审核，不能重复审核！");
		return;
	}
	
	if(value=='N'){
		var auditRem3=document.lostElevatorReportAuditForm.auditRem3.value;
		if(auditRem3==""){
			alert("驳回需要填写审核意见！");
			return;
		}
	}
	
	document.lostElevatorReportAuditForm.auditStatus3.value=value;
	document.getElementById("ReturnBtn").disabled='true';
	document.getElementById("AuditBtn").disabled='true';
	document.getElementById("RejectBtn").disabled='true';
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