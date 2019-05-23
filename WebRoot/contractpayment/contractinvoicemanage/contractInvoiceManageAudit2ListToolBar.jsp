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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncontractinvoiceaudit2" value="Y"> 
    AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'查看与确认',"100","1","modifyMethod()");
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
	toDoMethod(getIndex(),"toDisplayRecord","&returnMethod=toSearchRecordAudit","<bean:message key="javascript.role.alert2"/>");
}

//修改
function modifyMethod(){
	var index = getIndex();	
	if(index >= 0){	
		var auditStatus=document.getElementsByName("auditStatus")[index].value;//审核状态
		if(auditStatus == "N" || auditStatus=="X"){
			toDoMethod(index,"toPrepareAuditRecord","&authority=procontractarfeemaster");
		}else{
			toDoMethod(getIndex(),"toAuditDisplay","&returnMethod=toSearchRecordAudit","<bean:message key="javascript.role.alert2"/>");
		}
	}else{
		alert('<bean:message key="javascript.role.alert2"/>');
	}
}

function modifyMethod2(id,auditStatus){
	if(auditStatus == "N" || auditStatus=="X"){
		window.location = '<html:rewrite page="/contractInvoiceManageAudit2Action.do"/>?id='+id+'&method=toPrepareAuditRecord';
	}else{
		window.location = '<html:rewrite page="/contractInvoiceManageAudit2Action.do"/>?id='+id+'&method=toAuditDisplay';
	}
	
	/* toDoMethod(index,"toPrepareUpdateRecord","","<bean:message key="javascript.role.alert1"/>"); */
}


//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/contractInvoiceManageAudit2Action.do"/>?id='+getVal('ids',index)+'&method='+method+params;
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