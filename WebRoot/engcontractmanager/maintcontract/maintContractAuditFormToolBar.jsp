<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  <logic:present name="workisdisplay">  
	AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","closeMethod()");
	//AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","window.close()");
</logic:present>
<logic:notPresent name="workisdisplay">  
	  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
	  
	  <%-- <logic:notPresent name="display"> --%>
	  <%-- 是否有可写的权限--%>
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintcontractaudit" value="Y"> 
	  	<logic:notEqual name="maintContractBean" property="auditStatus" value="Y">
	      AddToolBarItemEx("AuditBtn","../../common/images/toolbar/save.gif","","","审核通过","80","1","auditing('Y')");
	      AddToolBarItemEx("RejectBtn","../../common/images/toolbar/save.gif","","","驳 回","60","1","rejectMethod('N')");
	    </logic:notEqual>
	  </logic:equal>
	  <%-- </logic:notPresent> --%>
  </logic:notPresent> 
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//关闭
function closeMethod(){
  window.close();
}
//返回
function returnMethod(){
	window.location = '<html:rewrite page="/maintContractAuditSearchAction.do"/>?method=toSearchRecord';
}

//审核
function auditing(value){
	var submitType=document.maintContractAuditForm.submitType.value;
	var auditStatus=document.maintContractAuditForm.auditStatus.value;
	if(submitType!="Y"){
		alert("此记录没有提交不能审核！");
		return;
	}
	if(auditStatus=="Y"){
		alert("此记录已经审核，不能重复审核！");
		return;
	}
	if(confirm("是否确定审核通过该维保合同？")){
		document.maintContractAuditForm.auditStatus.value=value;
		document.getElementById("ReturnBtn").disabled='true';
		document.getElementById("AuditBtn").disabled='true';
		document.getElementById("RejectBtn").disabled='true';
		document.maintContractAuditForm.submit();
	}
}
function rejectMethod(value){
	if(confirm("确定要驳回该维保合同？")){
		document.maintContractAuditForm.auditStatus.value=value;
		document.getElementById("ReturnBtn").disabled='true';
		document.getElementById("AuditBtn").disabled='true';
		document.getElementById("RejectBtn").disabled='true';
		document.maintContractAuditForm.submit();
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
              CreateToolBar();
            </script>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>