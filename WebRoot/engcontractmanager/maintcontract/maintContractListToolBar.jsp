 <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>

<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  
  AddToolBarItemEx("SearchBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/view.gif","","",'<bean:message key="toolbar.read"/>',"65","1","viewMethod()");
  
  <logic:equal name="roleidshow" value="Y"> 
  	AddToolBarItemEx("RenewBtn","../../common/images/toolbar/add.gif","","",'续 签',"65","1","renewalMethod2('ALL')");
    AddToolBarItemEx("RenewbfBtn","../../common/images/toolbar/borrow.gif","","",'部分续签',"80","1","renewalMethod2('PART')");
    AddToolBarItemEx("ModifDateyBtn","../../common/images/toolbar/edit.gif","","",'维护年检日期',"105","1","modifyDateMethod()");
  </logic:equal>
  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintcontract" value="Y"> 
    AddToolBarItemEx("SelectAddBtn","../../common/images/toolbar/add.gif","","",'选择新建',"80","1","selectAddMethod()");
    AddToolBarItemEx("AddBtn","../../common/images/toolbar/add.gif","","",'新 建',"65","1","addMethod()");
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.modify"/>',"65","1","modifyMethod()");
    AddToolBarItemEx("ReferBtn","../../common/images/toolbar/digital_confirm.gif","","",'提 交',"65","1","referMethod()");
    AddToolBarItemEx("RenewBtn","../../common/images/toolbar/add.gif","","",'续 签',"65","1","renewalMethod('ALL')");
    AddToolBarItemEx("ReferxdBtn","../../common/images/toolbar/add.gif","","",'下 达',"65","1","assignTasksMethod()");
    AddToolBarItemEx("RenewbfBtn","../../common/images/toolbar/borrow.gif","","",'部分续签',"80","1","renewalMethod2('PART')");
    //AddToolBarItemEx("SurrenderBtn","../../common/images/toolbar/edit.gif","","",'退 保',"65","1","surrenderMethod()");
    AddToolBarItemEx("SurrenderBtn","../../common/images/toolbar/edit.gif","","",'到期处理',"85","1","isExpire()");
    AddToolBarItemEx("ModifyDateBtn","../../common/images/toolbar/edit.gif","","",'维护年检日期',"105","1","modifyDateMethod()");
    AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'<bean:message key="toolbar.delete"/>',"65","1","deleteMethod()");
  </logic:equal>
  
  //AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//维护年检日期
function modifyDateMethod(){
	toDoMethod(getIndex(),"toDisplayDateRecord","","<bean:message key="javascript.role.alert1"/>");
}

//续保
function renewalMethod2(valstr){	
	var index = getIndex();	

	var auditStatus=getVal("auditStatus",index);//审核状态	
	if(auditStatus && auditStatus != "Y"){
		alert("记录未审核，不能续签!"); 
		return;
	}
	var contractStatus=getVal("contractStatus",index);//合同状态	
	if(contractStatus && contractStatus == "历史"){
		alert("不能续签历史记录!"); 
		return;
	}	
	if(contractStatus && contractStatus == "退保"){
		alert("不能续签退保记录!"); 
		return;
	}
	var taskSubFlag=getVal("taskSubFlag",index);
	var r2 = getVal("r2",index);//维保合同号
	if(taskSubFlag != "Y" && r2 != "Y"){
		alert("该记录未下达或未委托，不能进行续签！");
		return;
	}
	if(index>=0){
		if(confirm("是否有 价格、条款 的改变？")){
			toDoMethod(index,"toPrepareRenewalRecord2","&xqtype="+valstr,"<bean:message key="javascript.role.alert.jilu"/>");
		}else{
			alert("如确认无条款价格等变更，无需再进行报价申请，可直接签署合同，并在签署后立即将合同原件寄回即可。");
		}
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
	
	
	//toDoMethod(index,"toPrepareRenewalRecord","","<bean:message key="javascript.role.alert.jilu"/>");
}

//查询
function searchMethod(){
	//serveTableForm.genReport.value = "";
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
}

//查看
function viewMethod(){
	toDoMethod(getIndex(),"toDisplayRecord","","<bean:message key="javascript.role.alert2"/>");
}

//选择新建
function selectAddMethod(){
	window.location = '<html:rewrite page="/maintContractSearchAction.do"/>?method=toSearchNext';
}

//新建
function addMethod(){
	window.location = '<html:rewrite page="/maintContractAction.do"/>?method=toPrepareAddRecord';
}

//修改
function modifyMethod(){
	var index = getIndex();	
	
	var contractStatus=getVal("contractStatus",index);//合同状态	
	if(contractStatus && (contractStatus == "历史" || contractStatus == "退保")){
		alert("不能修改合同状态为历史或退保的记录!"); 
		return;
	}
	var submitType=getVal("submitType",index);//提交状态		
	if(submitType && submitType == "Y"){
		alert("该记录已提交,不能修改!"); 
		return;
	}
	
	toDoMethod(index,"toPrepareUpdateRecord","","<bean:message key="javascript.role.alert1"/>");
}

//删除
function deleteMethod(){
	var index = getIndex();
	
	//var contractStatus=getVal("contractStatus",index);//合同状态	
	//if(contractStatus && contractStatus != "新签"){
	//	alert("不能删除合同状态为非新签的记录!"); 
	//	return;
	//}
	var submitType=getVal("submitType",index);//提交状态	
	if(submitType && submitType == "Y"){
		alert("该记录已提交,不能删除!"); 
		return;
	}
	var maintContractNo=getVal("maintContractNo",index);//提交状态	
	if(confirm("是否删除合同号："+maintContractNo+"?")){
		toDoMethod(index,"toDeleteRecord","","<bean:message key="javascript.role.alert3"/>");
	}
}

// 提交
function referMethod(){
	var index = getIndex();	

	var contractStatus=getVal("contractStatus",index);//合同状态	
	if(contractStatus && (contractStatus == "历史" || contractStatus == "退保")){
		alert("不能提交合同状态为历史或退保的记录!"); 
		return;
	}
	var submitType=getVal("submitType",index);//提交状态	
	if(submitType && submitType == "Y"){
		alert("不能重复提交记录!"); 
		return;
	}
	
	toDoMethod(index,"toReferRecord","","<bean:message key="javascript.role.alert.jilu"/>");
}

//续保
function renewalMethod(valstr){	
	var index = getIndex();	

	var auditStatus=getVal("auditStatus",index);//审核状态	
	if(auditStatus && auditStatus != "Y"){
		alert("记录未审核，不能续签!"); 
		return;
	}
	var contractStatus=getVal("contractStatus",index);//合同状态	
	if(contractStatus && contractStatus == "历史"){
		alert("不能续签历史记录!"); 
		return;
	}	
	if(contractStatus && contractStatus == "退保"){
		alert("不能续签退保记录!"); 
		return;
	}
	var taskSubFlag=getVal("taskSubFlag",index);
	var r2 = getVal("r2",index);
	if(taskSubFlag != "Y" && r2 != "Y"){
		alert("该记录未下达或未委托，不能进行续签！");
		return;
	}
	if(index>=0){
		if(confirm("是否有 价格、条款 的改变？")){
			toDoMethod(index,"toPrepareRenewalRecord2","&xqtype="+valstr,"<bean:message key="javascript.role.alert.jilu"/>");
		}else{
			toDoMethod(index,"toPrepareRenewalRecord","","<bean:message key="javascript.role.alert.jilu"/>");
		}
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");
	}
	
}

/**
//把confirm中按钮的文本替换为“是”/“否”，只适用于IE
function window.confirm(str)
{
	execScript("n = (msgbox('"+str+"',vbYesNo, '提示')=vbYes)", "vbscript");
	return(n);
}
*/


//下达
function assignTasksMethod(){	
	var index = getIndex();	
	
	var r2 = getVal("r2",index);//维保合同号
	if(r2 == "Y"){
		alert("该合同已建立委托合同，不能下达!"); 
		return;
	}	
	var taskSubFlag=getVal("taskSubFlag",index);//任务下达标志
	if(taskSubFlag && taskSubFlag == "Y"){
		alert("记录已经下达，不能重复下达!"); 
		return;
	}
	var auditStatus=getVal("auditStatus",index);//审核状态	
	if(auditStatus && auditStatus != "Y"){
		alert("记录未审核，不能下达!"); 
		return;
	}
	var contractStatus=getVal("contractStatus",index);//合同状态	
	if(contractStatus && (contractStatus == "历史" || contractStatus == "退保")){
		alert("不能下达合同状态为历史或退保的记录!"); 
		return;
	}
	
	toDoMethod(index,"toPrepareAssignTasks","","<bean:message key="javascript.role.alert.jilu"/>");
}

function isExpire(){
	 var index = getIndex();
	if(index>=0){
		var edates =getVal("contractEdate",index).replace(/-/g,"");
		var contractStatus= getVal("contractStatus",index);
		var warningStatus=getVal("warningStatus",index);
		var todayDate=document.getElementById("hiddatestr").value.replace(/-/g,"");
		if(Number(todayDate)>=Number(edates) && contractStatus != "退保" && contractStatus != "历史" && warningStatus==""){
			var taskSubFlag=getVal("taskSubFlag",index);
			if(taskSubFlag=="Y"){
				toDoMethod(index,"toPrepareExpireRecord","","<bean:message key="javascript.role.alert.jilu"/>");
			}else{
				alert("该记录未下达，不能进行到期处理操作！");
			}
		}else if(contractStatus == "退保" || contractStatus == "历史"){
			alert("该记录为退保或历史合同，不能进行到期处理操作！");
		}else{
			if(warningStatus=="Y"){
				alert("该记录已建拜访计划，不能进行到期处理操作！");
			}else if(warningStatus=="S"){
				alert("该记录已拜访反馈，不能进行到期处理操作！");
			}else{
				alert("合同未到期不能进行到期处理操作！");
			}
		}
	}
	
	
}

//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(params==null){
		params='';
	}
	if(index >= 0){	
		window.location = '<html:rewrite page="/maintContractAction.do"/>?id='+getVal('ids',index)+'&method='+method+params;
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

/* //导出Excel
function excelMethod(){
  serveTableForm.genReport.value="Y";
  serveTableForm.target = "_blank";
  document.serveTableForm.submit();
} */

//合同到期的记录用红色字体显示
window.onload=function(){
	var edates = document.getElementsByName("contractEdate");
	var contractStatus= document.getElementsByName("contractStatus");
	var todayDate=document.getElementById("hiddatestr").value.replace(/-/g,"");
	var warningStatus=document.getElementsByName("warningStatus");
	var taskSubFlag=document.getElementsByName("taskSubFlag");

	/**
	var date = new Date();
	var yyyy = date.getFullYear();
	var mm = date.getMonth()+1;
	mm = mm > 10 ? mm : "0"+mm
	var dd = date.getDate() < 10 ? "0"+date.getDate() : date.getDate();
	var todayDate = "" + yyyy + mm + dd
	*/

	for ( var i = 0; i < edates.length; i++) {
		var edate = edates[i].value.replace(/-/g,"");
		
		if(Number(todayDate)>=Number(edate) && contractStatus[i].value != "退保" 
				&& contractStatus[i].value != "历史" ){
			row = edates[i].parentNode.parentNode;			
			row.style.color = "#FF0000"
		}				
	}
}

//AJAX动态显示维保站
var req;
function Evenmore(obj,listname){	
	var comid=obj.value;
	 var selectfreeid = document.getElementById(listname);
	if(comid!="" && comid!="%"){
		
		 if(window.XMLHttpRequest) {
			 req = new XMLHttpRequest();
		 }else if(window.ActiveXObject) {
			 req = new ActiveXObject("Microsoft.XMLHTTP");
		 }  //生成response
		 
		 var url='<html:rewrite page="/maintContractAction.do"/>?method=toStorageIDList&comid='+comid;//跳转路径
		 req.open("post",url,true);//post 异步
		 req.onreadystatechange=function getnextlist(){
			
				if(req.readyState==4 && req.status==200){
				 var xmlDOM=req.responseXML;
				 var rows=xmlDOM.getElementsByTagName('rows');
				 if(rows!=null){
					    selectfreeid.options.length=0;
					    selectfreeid.add(new Option("全部","%"));	

				 		for(var i=0;i<rows.length;i++){
							var colNodes = rows[i].childNodes;
							if(colNodes != null){
								var colLen = colNodes.length;
								for(var j=0;j<colLen;j++){
									var freeid = colNodes[j].getAttribute("name");
									var freename = colNodes[j].getAttribute("value");
									selectfreeid.add(new Option(freeid,freename));
					            }
				             }
				 		}
				 	}
				
				}
		 };//回调方法
		 req.send(null);//不发送
	}else{		
		selectfreeid.options.length=0;
    	selectfreeid.add(new Option("全部","%"));
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