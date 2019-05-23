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
  
  AddToolBarItemEx("ViewAndAuditBtn","../../common/images/toolbar/edit.gif","","",'查看与审核',"100","1","viewAndAuditMethod()");
  <%-- 是否有可写的权限--%>
  <%-- <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintcontractaudit" value="Y"> --%>     
  <%-- </logic:equal> --%> 
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
  serveTableForm.target = "_self";
  document.serveTableForm.submit();
}

//查看
function viewAndAuditMethod(){
  toDoMethod(getIndex(), "toDisplayRecord","","<bean:message key="javascript.role.alert2"/>");
}

//审核
function auditMethod(){
	var index = getIndex();
	
	var submitType=getVal("submitType",index);//提交状态
	var auditStatus=getVal("auditStatus",index);//审核状态
		
	if(submitType && submitType == "N"){
		alert("请选择已提交的记录!"); 
		return;
	}
	if(auditStatus && !auditStatus == "N"){
		alert("该记录已审核!"); 
		return;
	}
		
	toDoMethod(index,"toAuditRecord","","<bean:message key="javascript.role.alert.jilu"/>");
}

//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/maintContractAuditAction.do"/>?id='+getVal('ids',index)+'&method='+method+params;
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

//合同到期的记录用红色字体显示
window.onload=function(){
	var edates = document.getElementsByName("contractEdate");
	var contractStatus= document.getElementsByName("contractStatus");
	var todayDate=document.getElementById("hiddatestr").value.replace(/-/g,"");
	var warningStatus=document.getElementsByName("warningStatus");
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
		
		if(Number(todayDate)>=Number(edate) && contractStatus[i].value != "退保" && contractStatus[i].value != "历史" && warningStatus[i].value!="S"){
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