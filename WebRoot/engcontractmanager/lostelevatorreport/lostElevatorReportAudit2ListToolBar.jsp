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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nlostelevatorreportaudit2" value="Y">
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/edit.gif","","",'查看与审核',"90","1","viewMethod()");     
  </logic:equal>

  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
  serveTableForm.target = "_self";
  document.serveTableForm.submit();
}

//查看
function viewMethod(){
  toDoMethod(getIndex(), "toDisplayRecord","","<bean:message key="javascript.role.alert2"/>");
}

//跳转方法
function toDoMethod(index,method,params,alertMsg){
	var	actionName = "lostElevatorReportAudit2Action";
	params = params == null ? '' : params;
	if(index >= 0){	
		window.location = '<html:rewrite page="/'+actionName+'.do"/>?id='+getVal('ids',index)+'&method='+method+params;
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
	var cause=document.getElementsByName("causeAnalysis");
	var compete=document.getElementsByName("competeCompany");
	var recovery=document.getElementsByName("recoveryPlan");
	for(var i=0;i<cause.length;i++){
		if(cause[i].value!="KHZXBY" && cause[i].value!="XSTT" 
		&& cause[i].value!="ZDFQ" && (compete[i].value==""
		|| recovery[i].value=="")){
			row = cause[i].parentNode.parentNode;			
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