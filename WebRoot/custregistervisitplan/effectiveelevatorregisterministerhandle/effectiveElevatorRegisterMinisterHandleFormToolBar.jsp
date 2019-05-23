<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="neffectiveelevatorregisterministerhandle" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
    window.location = '<html:rewrite page="/effectiveElevatorRegisterMinisterHandleAction.do"/>?method=toSearchRecord';
}

//判断是否为数字
function onlyNumber(c){
	  if(!/^\d+$/.test(c.value)){
		   var tile="<bean:message key='markingitems.check.error.cgprice.required'/>";
		  alert(tile);
		  c.value=0;
	  }
}

//只能输入数字
function f_check_number2(){
	  	if((event.keyCode>=48 && event.keyCode<=57)){
	  	}else{
			event.keyCode=0;
	   	}
	 }
//保存
function saveMethod(){
	var handleResult=document.getElementsByName("handleResult");
	  if(handleResult[0].value!=""){
		   document.effectiveElevatorRegisterMinisterHandleForm.isreturn.value = "N";
	      document.effectiveElevatorRegisterMinisterHandleForm.submit();
	   }else
	   {
		   var tile="<bean:message key='custreturnregistercheck.error.handleResult.required'/>";
		   alert(tile);
	   }
}

//保存返回
function saveReturnMethod(){
	var handleResult=document.getElementsByName("handleResult");
if(handleResult[0].value!=""){
	  document.effectiveElevatorRegisterMinisterHandleForm.isreturn.value = "Y";
    document.effectiveElevatorRegisterMinisterHandleForm.submit();
   }else
   {
	   var tile="<bean:message key='custreturnregistercheck.error.handleResult.required'/>";
	   alert(tile);
   }
}	

</script>

  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" >
        <tr >
          <td height="22" class="table_outline3" valign="bottom" width="100%" >
            <div id="toolbar" align="center">
            <script language="javascript">
              
                CreateToolBar();
                //SetToolBarAttribute();
              //-->
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>