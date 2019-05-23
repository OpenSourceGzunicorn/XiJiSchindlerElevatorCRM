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

  <logic:present name="workisdisplay">  
	AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","closeMethod()");
	//AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","window.close()");
</logic:present>
<logic:notPresent name="workisdisplay">  
	  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
	  //AddToolBarItemEx("ViewFlow","../../common/images/toolbar/viewdetail.gif","","",'查看审批流程',"110","1","viewFlow()");
	  <%-- 是否有可写的权限--%>
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintcontractdelayaudit" value="Y">
	  	<logic:notEqual name="delayBean" property="auditStatus" value="Y">
	  	AddToolBarItemEx("AuditBtn","../../common/images/toolbar/save.gif","","",'审核通过',"90","1","auditing('Y')");
	  	AddToolBarItemEx("RejectBtn","../../common/images/toolbar/delete.gif","","",'驳回',"65","1"," auditing('N')");
	  	</logic:notEqual>
	  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//关闭
function closeMethod(){
  window.close();
}
//返回
function returnMethod(){
	window.location = '<html:rewrite page="/maintContractDelayAuditSearchAction.do"/>?method=toSearchRecord';
}

//审核
function auditing(value){
	var submitType=document.maintContractDelayAuditForm.submitType.value;
	var auditStatus=document.maintContractDelayAuditForm.auditStatus.value;
	if(submitType!="Y"){
		alert("此记录没有提交不能审核！");
		return;
	}
	if(auditStatus=="Y"){
		alert("此记录已经审核，不能重复审核！");
		return;
	}
	document.maintContractDelayAuditForm.auditStatus.value=value;
	document.getElementById("ReturnBtn").disabled='true';
	document.getElementById("AuditBtn").disabled='true';
	document.getElementById("RejectBtn").disabled='true';
	document.maintContractDelayAuditForm.submit();
}

//查看流程
function viewFlow(){
		
	var avf=document.getElementById("avf");//查看审批流程页面链接
	var status=document.getElementById("status").value;//审核状态	
	var flowname=document.getElementById("flowname").value;//流程名称 
	var tokenid=document.getElementById("tokenId").value;//流程令牌
	
	if(status && status == "-1"){
		alert("流程未启动，无法查看审批流程图！");
		return;
	}
	if(tokenid == null || tokenid.value==""){
		alert("该记录为历史数据，无法查看审批流程图！");
		return;
	}

	avf.href='<html:rewrite page="/viewProcessAction.do"/>?method=toViewProcess&tokenid='+tokenid+'&flowname='+flowname;
	avf.click();
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