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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nentrustcontractquotemaster" value="Y"> 
    AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'新 建',"65","1","addFromQuoteMethod()");
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modifyMethod()");
    AddToolBarItemEx("ReferBtn","../../common/images/toolbar/digital_confirm.gif","","",'提 交',"65","1","referMethod()");
    AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod()");
    AddToolBarItemEx("ViewBtn","../../common/images/toolbar/conf_func.gif","","",'重启流程',"80","1","reStartMethod()"); 
  </logic:equal>
  
  //AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//重启流程
function reStartMethod(){
	if(serveTableForm.ids){
	 	var l = document.serveTableForm.ids.length;
	 	if(l)
	 	{
	 		for(i=0;i<l;i++)
	 		{
	 			if(document.serveTableForm.ids[i].checked == true){
	 				var iscontract=document.getElementsByName("iscontract")[i].value;
	 				if(iscontract=="是"){
	 					alert("请选择未生成合同的记录进行重启流程!");
	 					return;
	 				}else{
		 				var hiddenstatus=document.getElementsByName("hstatus")[i].value;//审核状态
		 				if(hiddenstatus=="1" || hiddenstatus=="2"){
		 					if(confirm("您确定对报价流水号为:"+document.serveTableForm.ids[i].value+" 的记录进行重启流程?"))
		 					{
		 						window.location = '<html:rewrite page="/entrustContractQuoteMasterAction.do"/>?id='+document.serveTableForm.ids[i].value+'&method=toReStartProcess';
		 					}
		 					return;
		 				}else{
		 					alert("请选择审核状态为'审核通过'或'审批终止'的记录进行重启流程!");
		 					return;
		 				}
	 				}
	 			}
	 		}
	 		if(l >0)
	 		{
	 			alert("<bean:message key="javascript.role.alert3"/>");
	 		}
	 	}
	 	else if(document.serveTableForm.ids.checked == true){
	 		var iscontract=document.getElementsByName("iscontract")[0].value;
			if(iscontract=="是"){
				alert("请选择未生成合同的记录进行重启流程!");
				return;
			}else{
		 		var hiddenstatus=document.getElementsByName("hstatus")[0].value;//审核状态
		 		if(hiddenstatus=="1" || hiddenstatus=="2"){
		 			if(confirm("您确定对报价流水号为:"+document.serveTableForm.ids.value+" 的记录进行重启流程?"))
		 			{
		 				window.location = '<html:rewrite page="/entrustContractQuoteMasterAction.do"/>?id='+document.serveTableForm.ids.value+'&method=toReStartProcess';
		 			}
		 			return;
		 		}else{
		 			alert("请选择审核状态为'审核通过'或'审批终止'的记录进行重启流程!");
		 			return;
		 		}
			}
	 	}
	 	else
	 	{
	 		alert("<bean:message key="javascript.role.alert3"/>");
	 	}
	 }
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
  window.location = '<html:rewrite page="/entrustContractQuoteMasterSearchAction.do"/>?method=toSearchNext';
}

//修改
function modifyMethod(){

	var index = checkedIndex();

	if(index >= 0){	
		var submitType=document.getElementsByName("submitType")[index].value;//提交状态
		
		if(submitType == "Y"){
			alert("该记录已提交,不能修改!"); 
			return;
		}
		
		toDoMethod(index,"toPrepareUpdateRecord","");
	
	} else {
		alert("<bean:message key="javascript.role.alert1"/>");
	}
}

//删除
function deleteMethod(){
	var index = checkedIndex();
	
	if(index >= 0){	
		var submitType=document.getElementsByName("submitType")[index].value;//提交状态
		
		if(submitType == "Y"){
			alert("该记录已提交,不能修改!"); 
			return;
		}
		
		toDoMethod(index,"toDeleteRecord","");
				
	} else {
		alert("<bean:message key="javascript.role.alert3"/>");
	}
}

// 提交
function referMethod(){
	var index = checkedIndex();
	if(index >= 0){	
		var submitType=document.getElementsByName("submitType")[index].value;//提交状态
		
		if(submitType == "Y"){
			alert("该记录已提交,不能修改!"); 
			return;
		}
		
		toDoMethod(index,"toReferRecord","");
	
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");		
	}
}



//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/entrustContractQuoteMasterAction.do"/>?id='+getVal('ids',index)+'&method='+method+params;
	}else if(alertMsg && alertMsg != ""){
		alert(alertMsg);
	}
}

function checkedIndex(){
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

/* //导出Excel
function excelMethod(){
  serveTableForm.genReport.value="Y";
  serveTableForm.target = "_blank";
  document.serveTableForm.submit();
} */

/* //列表界面合同到期的红色字体显示
window.onload=function(){
	var edates = document.getElementsByName("contractEdate");
	var date = new Date();
	var yyyy = date.getFullYear();
	var mm = date.getMonth()+1;
	mm = mm > 10 ? mm : "0"+mm
	var dd = date.getDate() < 10 ? "0"+date.getDate() : date.getDate();
	var todayDate = "" + yyyy + mm + dd

	for ( var i = 0; i < edates.length; i++) {
		var edate = edates[i].value.replace(/-/g,"");
		if(Number(todayDate)>=Number(edate)){
			row = edates[i].parentNode.parentNode;			
			row.style.color = "#FF0000"
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