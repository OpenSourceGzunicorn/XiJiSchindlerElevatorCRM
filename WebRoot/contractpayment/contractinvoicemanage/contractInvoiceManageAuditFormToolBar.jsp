<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
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
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncontractinvoiceaudit" value="Y"> 
	    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'审核通过',"80","1","saveMethod()");   
	    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/delete.gif","","",'驳回',"65","1","saveReturnMethod()");
	  </logic:equal>
  </logic:notPresent>
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");

}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/contractInvoiceManageSearchAction.do"/>?method=toSearchRecordAudit';
}

//保存
function saveMethod(){
  if(checkColumnInput(contractInvoiceManageForm)){
    document.contractInvoiceManageForm.isreturn.value = "N";
    document.contractInvoiceManageForm.submitType.value = "Y";
    document.contractInvoiceManageForm.auditStatus.value = "Y";
    document.contractInvoiceManageForm.submit();
  }
  
}

//保存返回
function saveReturnMethod(){
  inputTextTrim();  
  if(checkColumnInput(contractInvoiceManageForm)){
      document.contractInvoiceManageForm.isreturn.value = "Y";
      document.contractInvoiceManageForm.submitType.value = "R";
      document.contractInvoiceManageForm.auditStatus.value = "N";
      document.contractInvoiceManageForm.submit();
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