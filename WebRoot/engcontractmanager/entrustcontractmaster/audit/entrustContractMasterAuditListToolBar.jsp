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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nentrustcontractmasteraudit" value="Y"> 
  AddToolBarItemEx("VABtn","../../common/images/toolbar/edit.gif","","",'查看与审核',"100","1","viewAuditMethod()"); 
  </logic:equal>
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
  serveTableForm.target = "_self";
  document.serveTableForm.submit();
}

/* //查看
function viewMethod(){
  toDoMethod(checkedIndex(),"toDisplayRecord","&returnMethod=toSearchRecordAudit","<bean:message key="javascript.role.alert2"/>");
} */

//查看与审核
function viewAuditMethod(){
	var index = checkedIndex();
	if(index >= 0){	
		var auditStatus=document.getElementsByName("auditStatus")[index].value;//审核状态
		if(auditStatus != "N"){
			toDoMethod(checkedIndex(),"toAuditDisplay","&returnMethod=toSearchRecordAudit","<bean:message key="javascript.role.alert2"/>");
		}else{
			toDoMethod(index,"toPrepareAuditRecord","&authority=entrustcontractmasteraudit");
		}
		
	}else{
		alert("<bean:message key="javascript.role.alert.jilu"/>");		
	}
}

function viewAuditMethod2(id,auditStatus){
	if(auditStatus!="N"){
		window.location = '<html:rewrite page="/entrustContractMasterAction.do"/>?id='+id+'&method=toAuditDisplay'+'&authority=entrustcontractmasteraudit'+'&returnMethod=toSearchRecordAudit';
	}else{
		window.location = '<html:rewrite page="/entrustContractMasterAction.do"/>?id='+id+'&method=toPrepareAuditRecord'+'&authority=entrustcontractmasteraudit';
	}
}

//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/entrustContractMasterAction.do"/>?id='+document.getElementsByName("ids")[index].value+'&method='+method+params;
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

//列表界面合同到期的红色字体显示
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