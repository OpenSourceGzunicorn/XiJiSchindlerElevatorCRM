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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nelevatortransfercaseregistermanage" value="Y"> 
    AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'新 建',"65","1","addFromQuoteMethod()");
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modifyMethod()");
    AddToolBarItemEx("FactoryBtn","../../common/images/toolbar/edit.gif","","",'再次厂检',"80","1","factoryMethod()");
    AddToolBarItemEx("ReferBtn","../../common/images/toolbar/digital_confirm.gif","","",'提 交',"65","1","referMethod()");
    AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod()");
    AddToolBarItemEx("FactoryBtn","../../common/images/toolbar/close.gif","","",'关闭厂检',"80","1","closeMethod()");
  </logic:equal>
  AddToolBarItemEx("PrintBtn","../../common/images/toolbar/print.gif","","",'打印通知单',"90","1","printMethod()");
  
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
  toDoMethod(checkedIndex(),"toDisplayRecord","&returnMethod=toSearchRecord","<bean:message key="javascript.role.alert2"/>");
}

//从维保报价选取录入
function addFromQuoteMethod(){
  window.location = '<html:rewrite page="/elevatorTransferCaseRegisterManageAction.do"/>?method=toPrepareAddRecord';
}
//关闭厂检
function closeMethod(){

	var index = checkedIndex();

	if(index >= 0){	
		var isClose=document.getElementsByName("isClose")[index].value;
		
		if(isClose == "Y"){
			alert("请选择未关闭的厂检记录!"); 
			return;
		}
		
		var elevatorNo=document.getElementsByName("elevatorNo")[index].value;
		if(confirm("是否关闭厂检，电梯编号："+elevatorNo+"?")){
			toDoMethod(index,"toIsCloseRecord","");
		}
		
	
	} else {
		alert("<bean:message key="javascript.role.alert1"/>");
	}
}
//修改
function modifyMethod(){

	var index = checkedIndex();

	if(index >= 0){	
		var submitType=document.getElementsByName("submitType")[index].value;//提交状态
		
		if(submitType != "N"&&submitType != "R"){
			alert("该记录已提交或已转派,不能修改!"); 
			return;
		}
		
		toDoMethod(index,"toPrepareUpdateRecord","");
	
	} else {
		alert("<bean:message key="javascript.role.alert1"/>");
	}
}
//复制
function copyMethod(){
	var index = checkedIndex();
	if(index >= 0){	
	toDoMethod(index,"toPrepareCopyRecord","");
	} else {
		alert("请选择一条要复制厂检员的记录！");
	}
}


//删除
function deleteMethod(){
	var index = checkedIndex();
	
	if(index >= 0){	
		var submitType=document.getElementsByName("submitType")[index].value;//提交状态
		
		if(submitType != "N"&&submitType != "R"){
			alert("该记录已提交或已转派,不能删除!"); 
			return;
		}
		
		if(confirm("<bean:message key="javascript.role.deletecomfirm"/>"+document.serveTableForm.ids[index].value)){
			toDoMethod(index,"toDeleteRecord","");
		}
	} else {
		alert("<bean:message key="javascript.role.alert3"/>");
	}
}

// 提交
function referMethod(){
	var index = checkedIndex();
	if(index >= 0){	
		var submitType=document.getElementsByName("submitType")[index].value;//提交状态
		
		if(submitType != "N"&&submitType != "R"){
			alert("该记录已提交或已转派,不能提交!"); 
			return;
		}
		
		toDoMethod(index,"toReferRecord","");
	
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");		
	}
}


//再次厂检
function factoryMethod(){

	var index = checkedIndex();

	if(index >= 0){	
		var statusno=document.getElementsByName("status")[index].value;//审核状态
		var r1=document.getElementsByName("r1")[index].value
		var checkVersion=document.getElementsByName("checkVersion")[index].value;
		var factoryCheckResult=document.getElementsByName("factoryCheckResult")[index].value;//厂检状态
		//alert(r2);
		if(checkVersion!="Y" || r1!="Y"){
				var gen="";
				if(checkVersion=="N"){
					gen="该记录为历史版本，不能再次厂检！";
				}
				if(r1!="Y"){
					gen="该记录流程未启动或厂检部长未审核，不能再次厂检!";
				}
				if(factoryCheckResult=="合格")
			 	{
				 	gen="该记录厂检已合格,不能再次厂检!"; 
			 	}
				alert(gen); 
				return;
		 }else
		 {
			 if(factoryCheckResult=="合格")
			 {
				 alert("该记录厂检已合格,不能再次厂检!"); 
					return;
			 }
		 }
		
		var isClose=document.getElementsByName("isClose")[index].value;
		if(isClose=="Y"){
			alert("该记录已关闭厂检，不能再次厂检！");
			return;
		}
		
		toDoMethod(index,"toPrepareFactoryRecord","");
	
	} else {
		alert("<bean:message key="javascript.role.alert1"/>");
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
		window.open('<html:rewrite page="/elevatorTransferCaseRegisterManageAction.do"/>?id='+document.getElementsByName("ids")[index].value+'&method=toPreparePrintRecord');
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");		
	}
}

//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/elevatorTransferCaseRegisterManageAction.do"/>?id='+document.getElementsByName("ids")[index].value+'&method='+method+params;
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