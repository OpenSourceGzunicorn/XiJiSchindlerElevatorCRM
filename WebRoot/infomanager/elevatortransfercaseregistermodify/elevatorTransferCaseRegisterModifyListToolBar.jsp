 <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nelevatortransfercaseregistermodify" value="Y"> 
  	AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modifyMethod()");
	AddToolBarItemEx("SynchBtn","../../common/images/toolbar/edit.gif","","",'同步-电梯信息',"105","1","synchMethod()");
	AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod()");
  </logic:equal>
  
  //AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//删除
function deleteMethod(){
	var index =checkedIndex();
	if(index >= 0){	
		if(confirm("<bean:message key="javascript.role.deletecomfirm"/>"+document.getElementsByName("ids")[index].value)){
    		toDoMethod(index,"toDeleteRecord","","<bean:message key="javascript.role.alert3"/>");
		}
	}else {
		alert("<bean:message key="javascript.role.alert3"/>");
	}
}

//同步层站门
function synchMethod(){
	//禁用按钮
	document.getElementById("SynchBtn").disabled=true;
	window.location = '<html:rewrite page="/elevatorTransferCaseRegisterModifyAction.do"/>?method=toSynchRecord';
}
//查询
function searchMethod(){
  serveTableForm.target = "_self";
  document.serveTableForm.submit();
}

//修改
function modifyMethod(){
	var index =checkedIndex();
	if(index >= 0){	
		//var statu=document.getElementsByName("status")[index].value;
		//if(statu=='1'){
		//	alert("已审批通过,不能修改!");
		//	return;
		//}
    	toDoMethod(index,"toPrepareUpdateRecord","&returnMethod=toSearchRecord","<bean:message key="javascript.role.alert2"/>");
	}else {
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}

//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/elevatorTransferCaseRegisterModifyAction.do"/>?id='+document.getElementsByName("ids")[index].value+'&method='+method+params;
	}else if(alertMsg && alertMsg != ""){
		alert(alertMsg);
	}
}

function checkedIndex(){
	if(document.getElementsByName("ids").length){	
		var ids = document.getElementsByName("ids");
		for(var i=0;i<ids.length;i++){
		  if(ids[i].checked == true){
		    return i;
		  }
		}				
	}
	return -1;	
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