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

  <logic:notPresent name="typejsp">
    AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  </logic:notPresent>
<%--   <logic:notPresent name="display">  
	  是否有可写的权限
	  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncontractpaymentquery" value="Y"> 
	    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");   
	    <logic:notPresent name="Backfill">
	      AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="tollbar.saveandrefer"/>',"120","1","saveReturnMethod()");
	    </logic:notPresent>
	  </logic:equal>
  </logic:notPresent> --%>
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");

}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/contractPaymentQuerySearchAction.do"/>?method=toSearchRecord';
}

/* //保存
function saveMethod(){
  if(checkColumnInput(contractParagraphManageForm)){
	  if(document.getElementById("paragraphMoney")!=null && isNaN(document.getElementById("paragraphMoney").value)){
		  if(parseFloat(document.getElementById("paragraphMoney").value)<=${contractBean.arrearsMoney}){
			  document.contractParagraphManageForm.isreturn.value = "N";
			  document.contractParagraphManageForm.submitType.value = "N";
			  document.contractParagraphManageForm.submit();
		  }else{
			  alert("");
		  }
	  }else{
		  alert("");
	  }
    
  }
  
}

//保存返回
function saveReturnMethod(){
  inputTextTrim();  
  if(checkColumnInput(contractParagraphManageForm)){
	  if(document.getElementById("paragraphMoney")!=null && isNaN(document.getElementById("paragraphMoney").value)){
		  if(parseFloat(document.getElementById("paragraphMoney").value)<=${contractBean.arrearsMoney}){
			  document.contractParagraphManageForm.isreturn.value = "Y";
		      document.contractParagraphManageForm.submitType.value = "Y";
		      document.contractParagraphManageForm.submit();
		  }else{
			  alert("来款金额不能大于欠款金额！");
		  }
	  }else{
		  alert("来款金额只能输入数字！");
	  }
      
    } 
} */


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