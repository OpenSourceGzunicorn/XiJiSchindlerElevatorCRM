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
  <logic:equal name="ServicingContractMasterList" property="auditStatus" value="未审核">
  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save.gif","","","审核通过","80","1","Auditing('Y')");
  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save.gif","","","驳回","60","1","Auditing('N')");
 </logic:equal>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/wgchangeContractAuditSearchAction.do"/>?method=toSearchRecord';
}
//审核
function Auditing(value){
	var submitType=document.wgchangeContractForm.submitType.value;
	var auditStatus=document.wgchangeContractForm.auditStatus.value;
	if(submitType!="Y"){
		alert("此记录没有提交不能审核！");
		return;
	}
	if(auditStatus!="N"){
		alert("此记录已经审核，不能重复审核！");
		return;
	}
	document.wgchangeContractForm.auditStatus.value=value;
	document.wgchangeContractForm.submit();
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