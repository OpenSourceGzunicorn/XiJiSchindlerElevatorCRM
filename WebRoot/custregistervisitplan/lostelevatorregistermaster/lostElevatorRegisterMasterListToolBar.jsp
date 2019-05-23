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
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.historyread"/>',"90","1","viewMethod()");
  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nlostelevatorregistermaster" value="Y"> 
    AddToolBarItemEx("VisitBtn","../../common/images/toolbar/add.gif","","",'<bean:message key="toolbar.visit"/>',"90","1","addMethod()");
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.returnResult"/>',"90","1","modifyMethod()");
  </logic:equal>
  <%-- AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  
  是否有可写的权限
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nprocontractarfeemaster" value="Y"> 
    AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'新 建',"65","1","addMethod()");
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modifyMethod()");
    AddToolBarItemEx("ReferBtn","../../common/images/toolbar/digital_confirm.gif","","",'提 交',"65","1","referMethod()");
    AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod()");
    AddToolBarItemEx("WarnBtn","../../common/images/toolbar/edit.gif","","",'填写提醒备注',"100","1","warnMethod()");
  </logic:equal> --%>
  
  AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
	lostElevatorRegisterMasterForm.genReport.value = "";
	lostElevatorRegisterMasterForm.target = "_self";
	document.lostElevatorRegisterMasterForm.submit();
}

//历史查看
function viewMethod(){
	toDoMethod(getIndex(),"companyId","toHistoryDisplayRecord","&returnMethod=toSearchRecord","<bean:message key="javascript.role.alert2"/>");
}

//开始回访
function addMethod(){
	/* window.location = '<html:rewrite page="/proContractArfeeMasterSearchAction.do"/>?method=toSearchNext'; */
	var index = getIndex();
	var reMark=getVal("reMark",index);//是否回访
	var operDate=getVal("operDate",index);//上次回访日期
	var isReturnResult=getVal("isReturnResult",index);//
	//var submitType=getVal("submitType",index);//提交状态
	if(operDate!=null && operDate!=""){
		if(reMark!="Y" || isReturnResult!="是"){
			if(reMark!="Y")
				alert("<bean:message key="javascript.custreturnregister.role.alert7"/>");
			if(isReturnResult!="是")
				alert("<bean:message key="javascript.custreturnregister.role.alert6"/>");
			//alert("该记录已提交,不能修改!"); 
			return;
		}
		
	}
	
	toDoMethod(index,"ids","toPrepareAddRecord","","<bean:message key="javascript.role.alert1"/>");
}

//回访反馈
function modifyMethod(){
	var index = getIndex();	
	//var submitType=getVal("submitType",index);//提交状态
	var ishandleValue=getVal("ishandleValue",index);//
	var isReturnResult=getVal("isReturnResult",index);//
	//alert(isReturnResult+ishandleValue);		
	if(ishandleValue!="是" || isReturnResult=="是"){
		if(ishandleValue!="是")
			alert("<bean:message key="javascript.custreturnregister.role.alert4"/>");
		if(isReturnResult=="是")
			alert("<bean:message key="javascript.custreturnregister.role.alert5"/>");
		//alert("该记录已提交,不能修改!"); 
		return;
	}
	
	toDoMethod(index,"toReturnResult","toPrepareUpdateRecord","","<bean:message key="javascript.role.alert1"/>");
}

//跳转方法
function toDoMethod(index,name,method,params,alertMsg){
	if(index >=0 ){
		window.location = '<html:rewrite page="/lostElevatorRegisterMasterAction.do"/>?id='+getVal(name,index)+'&method='+method+params;
	}else if(alertMsg && alertMsg != ""){
		alert(alertMsg);
	}
}

//获取选中记录下标
function getIndex(){
	if(lostElevatorRegisterMasterForm.ids){
		var ids = lostElevatorRegisterMasterForm.ids;
		if(ids.length == null){
			if(ids.checked==true)
				return 0;
			else
				return -1;
		}
		for(var i=0;i<ids.length;i++){
			if(ids[i].checked == true){
				return i;
			}
		}		
	}
	return -1;	
}

//根据name和选中下标获取元素的值
function getVal(name,index){
	var obj = eval("lostElevatorRegisterMasterForm."+name);
	if(obj && obj.length){
		obj = obj[index];
	}
	return obj ? obj.value : null;
}

//导出Excel
function excelMethod(){
  lostElevatorRegisterMasterForm.genReport.value="Y";
  lostElevatorRegisterMasterForm.target = "_blank";
  document.lostElevatorRegisterMasterForm.submit();
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