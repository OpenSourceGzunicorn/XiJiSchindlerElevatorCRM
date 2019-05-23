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
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nelevatortransfercaseregisterdispose" value="Y"> 
    AddToolBarItemEx("ReceiveBtn","../../common/images/toolbar/edit.gif","","",'接收',"65","1","receiveMethod()");
    AddToolBarItemEx("RejectBtn","../../common/images/toolbar/edit.gif","","",'驳回',"65","1","rejectMethod()");
    AddToolBarItemEx("TurnBtn","../../common/images/toolbar/edit.gif","","",'转派',"65","1","turnMethod()");
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'登记',"65","1","modifyMethod()");
    AddToolBarItemEx("PrintBtn","../../common/images/toolbar/print.gif","","",'打印通知单',"90","1","printMethod()");
    </logic:equal>
  
  //AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
  //serveTableForm.genReport.value = "";
  serveTableForm.target = "_self";
  document.serveTableForm.submit();
}

//查看
function viewMethod(){
  toDoMethod(checkedIndex(),"toDisposeDisplay","&returnMethod=toSearchRecordDispose","<bean:message key="javascript.role.alert2"/>");
}

//接收
function receiveMethod(){
  var index = checkedIndex();

	if(index >= 0){	
		var processStatus=document.getElementsByName("processStatus")[index].value;//提交状态
		
		if(processStatus != "0"){
			alert("请选择 未接收的记录,做接收操作!"); 
			return;
		}
		
		toDoMethod(index,"toReceiveDisposeRecord","");
	
	} else {
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
}

//登记
function modifyMethod(){

	var index = checkedIndex();

	if(index >= 0){	
		var processStatus=document.getElementsByName("processStatus")[index].value;//提交状态
		
		if(processStatus != "4"&&processStatus != "1"){
			alert("请选择 已接收或已登记未提交的记录,做登记操作!"); 
			return;
		}
		
		toDoMethod(index,"toPrepareDisposeRecord","");
	
	} else {
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
}

//驳回
function rejectMethod(){

	var index = checkedIndex();

	if(index >= 0){	
		var processStatus=document.getElementsByName("processStatus")[index].value;//提交状态
		
		if(processStatus != "4"&&processStatus != "0"){
			alert("请选择 未登记的记录,做驳回操作!"); 
			return;
		}
		
		toDoMethod(index,"toPrepareDisposeReject","");
	
	} else {
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
}

//转派
function turnMethod(){

	var index = checkedIndex();

	if(index >= 0){	
		var processStatus=document.getElementsByName("processStatus")[index].value;//提交状态
		
		if(processStatus != "4"&&processStatus != "0"){
			alert("请选择  未登记的记录,做转派操作!"); 
			return;
		}
		
		toDoMethod(index,"toPrepareDisposeTurn","");
	
	} else {
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
}

//打印
function printMethod(){
	var index = checkedIndex();
	if(index >= 0){	
		var processStatus=document.getElementsByName("processStatus")[index].value;//提交状态
		
		if(processStatus != "2"&&processStatus != "3"){
			alert("处理状态为已登记已提交或已审核的,才可以打印"); 
			return;
		}
		window.open('<html:rewrite page="/elevatorTransferCaseRegisterAction.do"/>?id='+document.getElementsByName("ids")[index].value+'&method=toPreparePrintDisposeRecord');
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");		
	}
}



//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/elevatorTransferCaseRegisterAction.do"/>?id='+document.getElementsByName("ids")[index].value+'&method='+method+params;
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