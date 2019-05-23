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
  AddToolBarItemEx("AddBtn","../../common/images/toolbar/view.gif","","",'查看',"65","1","modifyMethod()");
  AddToolBarItemEx("ViewFlow","../../common/images/toolbar/viewdetail.gif","","",'查看审批流程',"110","1","viewFlow()");
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="npaymentaudit" value="Y"> 
  </logic:equal>
  //AddToolBarItemEx("ExcelBtn","../../common/images/toolbar/xls.gif","","",'<bean:message key="toolbar.xls"/>',"90","1","excelMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
	serveTableForm.target = "_self";
	document.serveTableForm.submit();
}
//修改
function modifyMethod(){
	var index = getIndex();	
	toDoMethod(index,"toDisplayRecord","","请选择查看或审核的记录！");
}

//查看流程
function viewFlow(){
if(serveTableForm.ids)
{
	var l = document.serveTableForm.ids.length;
	if(l)
	{
		for(i=0;i<l;i++){
			if(document.serveTableForm.ids[i].checked == true){
				var hiddenReturnStatus=document.getElementsByName("hstatus")[i];
				if(hiddenReturnStatus.value=="-1"){
					alert("流程未启动，无法查看审批流程图！");
				}else{
					var avf=document.getElementById("avf");
					var flowname=document.getElementById("flowname");
					var tokenid=document.getElementsByName("tokenid")[i];
					if(tokenid!=null && tokenid.value!="" && tokenid.value!=0){
						if(avf!=null && tokenid!=null && flowname!=null){
							avf.href='<html:rewrite page="/viewProcessAction.do"/>?method=toViewProcess&tokenid='+tokenid.value+'&flowname='+flowname.value;
							avf.click();
						}else{
							alert("请选择一条要查看的记录！");
						}
					}else{
						alert("该记录为历史数据，无法查看审批流程图！");
					}
				}
				return;
			}
		}
		if(l >0){
			alert("<bean:message key="javascript.role.alert2"/>");
		}
	}else if(document.serveTableForm.ids.checked == true){
		var hiddenReturnStatus=document.getElementsByName("hstatus")[0];
		if(hiddenReturnStatus.value=="-1"){
			alert("流程未启动，无法查看审批流程图！");
		}else{
			var avf=document.getElementById("avf");
			var flowname=document.getElementById("flowname");
			var tokenid=document.getElementsByName("tokenid")[0];
			if(tokenid!=null && tokenid.value!=""){
				if(avf!=null && tokenid!=null && flowname!=null){
					avf.href='<html:rewrite page="/viewProcessAction.do"/>?method=toViewProcess&tokenid='+tokenid.value+'&flowname='+flowname.value;
					avf.click();
				}else{
					alert("请选择一条要查看的记录！");
				}
			}else{
				alert("该记录为历史数据，无法查看审批流程图！");
			}
		}
	}else{
		alert("<bean:message key="javascript.role.alert2"/>");
	}
}
}

//跳转方法
function toDoMethod(index,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/paymentAuditAction.do"/>?id='+getVal('ids',index)+'&method='+method+params;
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