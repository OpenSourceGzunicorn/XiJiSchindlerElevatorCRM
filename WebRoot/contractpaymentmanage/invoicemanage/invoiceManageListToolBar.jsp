 <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/numberToCN.js"/>"></script>
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ninvoicemanage" value="Y"> 
    AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'新 建',"65","1","addMethod()");
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modifyMethod()");
    AddToolBarItemEx("ReferBtn","../../common/images/toolbar/digital_confirm.gif","","",'提 交',"65","1","referMethod()");
    AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod('N')");
  
    <logic:equal name="showroleid" value="A01"> 
		AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'管理员删除',"95","1","deleteMethod('Y')");
	</logic:equal>

   </logic:equal>
  
  //AddToolBarItemEx("printBtn","../../common/images/toolbar/print.gif","","",'打印通知单',"100","1","printMethod()");
  
  //AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
}

//查看
function viewMethod(){
	var index = getIndex();	
	toDoMethod(index,"toDisplayRecord","&returnMethod=toSearchRecord","<bean:message key="javascript.role.alert2"/>");
}

//新建
function addMethod(){
	window.location = '<html:rewrite page="/invoiceManageSearchAction.do"/>?method=toSearchNext';
}
//打印
function printMethod(){
	var index = getIndex();	
	var auditStatus=getVal("auditStatus",index);//提交状态		
	if(auditStatus && auditStatus != "Y"){
		alert("该记录未通过审核,不能打印通知单!"); 
		return;
	}
	if(index >= 0){	
		window.open('<html:rewrite page="/invoiceManageAction.do"/>?id='+getVal('ids',index)+'&method=toPrintRecord');
	}else{
		alert("请选择一条记录打印！");
	}
}


//修改
function modifyMethod(){
	var index = getIndex();	
	var submitType=getVal("submitType",index);//提交状态		
	if(submitType ==null || submitType == "Y"){
		alert("该记录已提交,不能修改!"); 
		return;
	}
	
	toDoMethod(index,"toPrepareUpdateRecord","","<bean:message key="javascript.role.alert1"/>");
}

//删除
function deleteMethod(valstr){
	var index = getIndex();
	if(index >= 0){	
		if(valstr=='N'){
			var submitType=getVal("submitType",index);//提交状态	
			if(submitType ==null || submitType == "Y"){
				alert("该记录已提交,不能删除!"); 
				return;
			}
		}
		if(confirm("<bean:message key="javascript.role.deletecomfirm"/>")){
			toDoMethod(index,"toDeleteRecord","","<bean:message key="javascript.role.alert3"/>");
		}
	}else {
		alert("<bean:message key="javascript.role.alert3"/>");
	}
}

// 提交
function referMethod(){
	var index = getIndex();	
	var submitType=getVal("submitType",index);//提交状态	
	if(submitType ==null || submitType == "Y"){
		alert("不能重复提交记录!"); 
		return;
	}	
	toDoMethod(index,"toReferRecord","","<bean:message key="javascript.role.alert.jilu"/>");
}


//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/invoiceManageAction.do"/>?id='+getVal('ids',index)+'&method='+method+params;
	}else if(alertMsg && alertMsg != ""){
		alert(alertMsg);
	}
}

//获取选中记录下标
function getIndex(){
	if(serveTableForm.ids){
		var ids = serveTableForm.ids;
		if(ids.length == null){
			return 0;
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
	var obj = eval("serveTableForm."+name);
	if(obj && obj.length){
		obj = obj[index];
	}
	return obj ? obj.value : null;
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