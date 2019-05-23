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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintenanceworkplancompletion" value="Y">   
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'保养审核',"80","1","modiyMethod()");
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'单据录入',"80","1","modiyMethod2()");
    AddToolBarItemEx("ExcelBtn", "../../common/images/toolbar/print.gif", "", "",'打印保养单', "90", "1", "printMethod()");
    AddToolBarItemEx("ExcelBtn", "../../common/images/toolbar/delete.gif", "", "",'清除保养状态', "100", "1", "deleteMethod()");
    AddToolBarItemEx("ExcelBtn", "../../common/images/toolbar/delete.gif", "", "",'删除保养计划', "100", "1", "deleteMethod2()");
    AddToolBarItemEx("ExcelBtn", "../../common/images/toolbar/delete.gif", "", "",'批量删除保养计划', "140", "1", "deleteMethod3()");
  </logic:equal>
  
  AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//批量删除
function deleteMethod3(){
  window.location = '<html:rewrite page="/maintenanceWorkPlanCompletionAction.do"/>?method=toPrepareDeleteRecord';
}
//删除
function deleteMethod2(){
	var index =checkedIndex();
	if(index >= 0){	
		var hsingleno=getVal('hsingleno',index);
		if(hsingleno!=""){
			alert("请选择 保养单号 为空的记录，进行删除！"); 
		}else{
			if(confirm("是否删除保养计划？")){
	    		toDoMethod(index,"toDeleteRecord2","","<bean:message key="javascript.role.alert3"/>");
			}
		}
	}else {
		alert("<bean:message key="javascript.role.alert3"/>");
	}
}
//删除
function deleteMethod(){
	var index =checkedIndex();
	if(index >= 0){	
		if(confirm("是否清除保养状态？")){
    		toDoMethod(index,"toDeleteRecord","","<bean:message key="javascript.role.alert3"/>");
		}
	}else {
		alert("<bean:message key="javascript.role.alert3"/>");
	}
}
//查询
function searchMethod(){
	serveTableForm.genReport.value = "";
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
}
//查看
function modifyMethod(){
	var index = getIndex();	
	toDoMethod(index,"toDisplayRecord","","<bean:message key="javascript.role.alert1"/>");
}

//审核
function modiyMethod(){
	var index = getIndex();	
	var hmaintEndTime=getVal('hmaintEndTime',index);
	if(hmaintEndTime==""){
		alert("保养未完工，不能进行保养审核。");
		return;
	}
	
	var hauditType=getVal('hauditType',index);
	if(hauditType=="已审核"){
		alert("保养已审核，不能进行保养审核。");
		return;
	}
	
	toDoMethod(index,"toPrepareUpdateRecord","","<bean:message key="javascript.role.alert1"/>");

}
//审核
function modiyMethod2(){
	var index = getIndex();	

	var hauditType=getVal('hauditType',index);
	if(hauditType=="未审核"){
		alert("保养未审核，不能进行单据录入。");
		return;
	}
	
	toDoMethod(index,"toPrepareUpdate2Record","","<bean:message key="javascript.role.alert1"/>");

}

//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/maintenanceWorkPlanCompletionAction.do"/>?id='+getVal('ids',index)+'&method='+method+params;
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

//打印
function printMethod(){
	var index = checkedIndex();
	if(index >= 0){	
	 	var hmaintEndTime=document.getElementsByName("hmaintEndTime")[index].value;//保养结束时间
		if(hmaintEndTime != ""){ 
			window.open('<html:rewrite page="/maintenanceWorkPlanCompletionAction.do"/>?id='+document.getElementsByName("ids")[index].value+'&method=toPreparePrintRecord');
		}else{
			alert("保养未完工,不能打印保养单！"); 
			return;
		}
		
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");		
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

//导出Excel
function excelMethod(){
	serveTableForm.genReport.value="Y";
	serveTableForm.target = "_blank";
	document.serveTableForm.submit();
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