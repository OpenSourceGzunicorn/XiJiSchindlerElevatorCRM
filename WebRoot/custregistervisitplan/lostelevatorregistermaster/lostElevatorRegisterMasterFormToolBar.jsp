<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nlostelevatorregistermaster" value="Y">
  	<logic:present name="contract"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    </logic:present>
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
	
	var isreturn=document.getElementById("isreturn2").value;
	if(isreturn=="2")
		{
		window.location = '<html:rewrite page="/lostElevatorRegisterMasterAction.do"/>?id='+ document.getElementById("companyId").value +'&method=toHistoryDisplayRecord';
		}else
			{
            window.location = '<html:rewrite page="/lostElevatorRegisterMasterAction.do"/>?method=toSearchRecord';
			}
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
	var handle=document.getElementById("ministerHandle");	
	  if(handle.value!=""){
	  //alert(rowsToJSONArray("registerDetail","details"));
		  document.lostElevatorRegisterMasterForm.isreturn.value = "Y";
		  //document.lostElevatorRegisterMasterForm.lostElevators.value=rowsToJSONArray("registerDetail","details");
	      document.lostElevatorRegisterMasterForm.submit();
	   }else
	   {
		   alert("请选择是否分部长处理！");
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