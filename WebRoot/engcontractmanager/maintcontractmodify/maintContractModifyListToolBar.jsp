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
 
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintContractModify" value="Y"> 
  		AddToolBarItemEx("AuditBtn","../../common/images/toolbar/edit.gif","","",'修 改',"65","1","modifyMethod()");
  		AddToolBarItemEx("SynchBtn","../../common/images/toolbar/edit.gif","","",'同步-电梯信息',"105","1","synchMethod()");
  		AddToolBarItemEx("modifyBtn","../../common/images/toolbar/edit.gif","","",'改成未下达状态',"115","1","modifyMethod2()");
  	    AddToolBarItemEx("DelBtn","../../common/images/toolbar/delete.gif","","",'删除合同',"85","1","deleteMethod()");
  	  	AddToolBarItemEx("ImportBtn","../../common/images/toolbar/dl_log.gif","","","导 入","65","1","importMethod()");
  		AddToolBarItemEx("SynchBtn2","../../common/images/toolbar/edit.gif","","",'同步-最大维保结束日期',"160","1","synchMethod2()");
  </logic:equal>
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//同步-最大维保结束日期
function synchMethod2(){
	//禁用按钮
	document.getElementById("AuditBtn").disabled=true;
	document.getElementById("SynchBtn").disabled=true;
	document.getElementById("SynchBtn2").disabled=true;
	document.getElementById("modifyBtn").disabled=true;
	document.getElementById("DelBtn").disabled=true;
	document.getElementById("ImportBtn").disabled=true;
	document.getElementById("ViewBtn").disabled=true;
	window.location = '<html:rewrite page="/maintContractModifyAction.do"/>?method=toSynchRecord2';
}

//导入excel
function importMethod(){
	window.location = '<html:rewrite page="/maintContractModifyAction.do"/>?method=toPrepareImportRecord';
}

//删除
function deleteMethod(){
	var index = getIndex();
	/**
	var contractStatus=getVal("contractStatus",index);//提交状态	
	if(contractStatus && contractStatus != "TB"){
		alert("请选择退保的合同进行删除!"); 
		return;
	}
	*/
	var maintContractNo=getVal("maintContractNo",index);//提交状态	
	if(confirm("是否删除合同号："+maintContractNo+"?")){
		toDoMethod(index,"toDeleteRecord","","<bean:message key="javascript.role.alert3"/>");
	}
}

//查询
function searchMethod(){
  serveTableForm.target = "_self";
  document.serveTableForm.submit();
}
//改成未下达状态
function modifyMethod2(){
	var index=getIndex();
	if(index >= 0){	
		var maintContractNo=getVal("maintContractNo",index);//提交状态	
		if(confirm("确认将合同号："+maintContractNo+" 改成未下达状态？")){
			//禁用按钮
			document.getElementById("AuditBtn").disabled=true;
			document.getElementById("SynchBtn").disabled=true;
			document.getElementById("modifyBtn").disabled=true;
			window.location = '<html:rewrite page="/maintContractModifyAction.do"/>?method=toUpdateRecord&id='+getVal('ids',index); 
		}
	}else{
		alert("请选择要修改的记录！");
	}
}
//同步层站门
function synchMethod(){
	//禁用按钮
	document.getElementById("AuditBtn").disabled=true;
	document.getElementById("SynchBtn").disabled=true;
	document.getElementById("SynchBtn2").disabled=true;
	document.getElementById("modifyBtn").disabled=true;
	document.getElementById("DelBtn").disabled=true;
	document.getElementById("ImportBtn").disabled=true;
	document.getElementById("ViewBtn").disabled=true;
	window.location = '<html:rewrite page="/maintContractModifyAction.do"/>?method=toSynchRecord';
}
//查看
function modifyMethod(){
  toDoMethod(getIndex(), "toDisplayRecord","","请选择要修改的记录！");
}

//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/maintContractModifyAction.do"/>?id='+getVal('ids',index)+'&method='+method+params;
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