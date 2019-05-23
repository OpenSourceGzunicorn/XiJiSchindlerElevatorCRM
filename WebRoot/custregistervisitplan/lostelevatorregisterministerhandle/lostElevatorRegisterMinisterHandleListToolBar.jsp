  <%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<!--
	客户地区表页工具栏
-->
<script language="javascript">
//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  AddToolBarItemEx("ViewBtn","../../common/images/toolbar/search.gif","","",'<bean:message key="toolbar.search"/>',"65","0","searchMethod()");
  AddToolBarItemEx("ModifyBtn","../../common/images/toolbar/edit.gif","","",'<bean:message key="toolbar.handle"/>',"80","1","modifyMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}


//查询
function searchMethod(){
	lostElevatorRegisterMinisterHandleForm.genReport.value = "";
	lostElevatorRegisterMinisterHandleForm.target = "_self";
	document.lostElevatorRegisterMinisterHandleForm.submit();
}

//处理
/* function modiyMethod(){
	
	if(custReturnRegisterMinisterHandleForm.ids)
	{
		
		var l = document.custReturnRegisterMinisterHandleForm.ids.length;
		if(l)
		{
			
			for(i=0;i<l;i++)
			{
				
				if(document.custReturnRegisterMinisterHandleForm.ids[i].checked == true)
				{					
					window.location = '<html:rewrite page="/custReturnRegisterMinisterHandleAction.do"/>?id='+document.custReturnRegisterMinisterHandleForm.toReturnResult[i].value +'&method=toPrepareUpdateRecord';
						return;
				}

			}
			if(l >0)
			{
				alert("<bean:message key="javascript.custreturnregister.role.alert8"/>");
			}
		}else if(document.custReturnRegisterMinisterHandleForm.ids.checked == true)
		{
			window.location = '<html:rewrite page="/custReturnRegisterMinisterHandleAction.do"/>?id='+document.custReturnRegisterMinisterHandleForm.toReturnResult.value +'&method=toPrepareUpdateRecord';
		}
		else
		{
			alert("<bean:message key="javascript.custreturnregister.role.alert8"/>");
		}
	}
} */

function modifyMethod(){
	var index = getIndex();	
	/* var ishandleValue=getVal("ishandleValue",index);//
	var isReturnResult=getVal("isReturnResult",index);//		
	if(ishandleValue!="是" && isReturnResult!="是"){
		if(ishandleValue!="是")
			alert("<bean:message key="javascript.custreturnregister.role.alert5"/>");
		if(isReturnResult!="是")
			alert("<bean:message key="javascript.custreturnregister.role.alert4"/>");
		return;
	} */
	
	toDoMethod(index,"toReturnResult","toPrepareUpdateRecord","","<bean:message key="javascript.role.alert1"/>");
}

//跳转方法
function toDoMethod(index,name,method,params,alertMsg){
	if(index >= 0){	
		window.location = '<html:rewrite page="/lostElevatorRegisterMinisterHandleAction.do"/>?id='+getVal(name,index)+'&method='+method+params;
	}else if(alertMsg && alertMsg != ""){
		alert(alertMsg);
	}
}

//获取选中记录下标
function getIndex(){
	if(lostElevatorRegisterMinisterHandleForm.ids){
		var ids = lostElevatorRegisterMinisterHandleForm.ids;
		if(ids.length == null){
			if(ids.checked==true)
				return 0;
			else
				return -1;
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
	var obj = eval("lostElevatorRegisterMinisterHandleForm."+name);
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
              //<!--
                CreateToolBar();
              //-->
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>