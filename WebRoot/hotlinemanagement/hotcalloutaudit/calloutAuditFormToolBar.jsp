<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript">

//πÿ”⁄ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");

  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nhotcalloutaudit" value="Y"> 
 	AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save.gif","","","…Û ∫À","65","1","Auditing('isSendSms')");
 </logic:equal>

  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//∑µªÿ
function returnMethod(){
  window.location = '<html:rewrite page="/hotCalloutAuditSearchAction.do"/>?method=toSearchRecord';
}

function Auditing(value){
	 document.CalloutMasterjxForm.submit();
}

function delselect(){
	document.getElementById("hftId").value="";
	document.getElementById("hftDesc").value="";
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