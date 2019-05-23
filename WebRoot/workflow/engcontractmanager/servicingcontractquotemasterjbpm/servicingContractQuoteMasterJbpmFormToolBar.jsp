<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  //AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  AddToolBarItemEx("ReturnTkBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="button.returntask"/>',"110","0","returnTaskMethod()");	
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmyTaskOA" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="button.submit"/>',"65","1","saveMethod()");
  </logic:equal>
  </logic:notPresent>
  
  AddToolBarItemEx("ViewFlow","../../common/images/toolbar/viewdetail.gif","","",'<bean:message key="toolbar.viewflow"/>',"110","1","viewFlow()");
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//去掉空格
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g,""); }

//返回
/* function returnMethod(){
  window.location = '<html:rewrite page="/maintContractQuoteSearchAction.do"/>?method=toSearchRecord';
} */

//返回代办列表
function returnTaskMethod(){
	window.location ='<html:rewrite page="/myTaskOaSearchAction.do"/>?method=toDoList&jumpto=templetdoorapp';
}

//提交
function saveMethod(){
	var typejsp=document.getElementById("typejsp").value;
	if(typejsp!=null && typejsp=='mondity'){
		if(checkInput()){
		    var elevatorNos = document.getElementsByName("elevatorNo");
		    var materialName = document.getElementsByName("materialName");
			if(elevatorNos !=null && elevatorNos.length>0){
				if(materialName !=null && materialName.length>0){
						var flags=getSelectValue();
						if(flags!="" && flags!="1"){
							alert("抱歉,您选择了'不同意',请在审批意见栏上填写否决的意见!");
							return;
						}else{
							document.getElementById("SaveBtn").disabled=true;
							document.WeiGchangeForm.submit();
						}
					}else{
						alert("不能保存，请添加物料明细。");
					}
			}else{
				alert("不能保存，请添加电梯信息。");
			}
		}
	}else{		
			document.getElementById("SaveBtn").disabled=true;
			document.WeiGchangeForm.submit();
	
	}
}

//查看流程
function viewFlow(){	
	var hiddenReturnStatus=document.getElementById("status");
	if(hiddenReturnStatus.value=="-1"){
		alert("流程未启动，无法查看审批流程图！");
	}else{
		var avf=document.getElementById("avf");
		var tokenid=document.getElementById("tokenid");
		var flowname=document.getElementById("flowname");
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
}

//取select的文本值
function getSelectValue(){
	var str="0";
	var obj=document.getElementById('approveresult');

 	var index=obj.selectedIndex; //序号，取当前选中选项的序号	
 	var val = obj.options[index].text;
 	if(val!="" && (val=="同意" || val.indexOf("提交")>-1)){
 	    str="1";
 	}else{
 		if(document.getElementById("approverem").value.trim()!="" || document.getElementById("approverem").value.trim().length>0){
 			str="1";
 		}
 	}
 	return str;
	
}

function fuz(){
	a=document.getElementsByName("xuhao");
	for(i=0;i<a.length;i++){
		a[i].value = i+1;
	}
} 

//添加电梯信息
function addElevators(tableId){
		var paramstring = "elevatorNos=";		
		var elevatorNos = document.getElementsByName("elevatorNo");
		for(i=0;i<elevatorNos.length;i++){
			paramstring += i<elevatorNos.length-1 ? "|"+elevatorNos[i].value+"|," : "|"+elevatorNos[i].value+"|"		
		}

		var returnarray = openWindowWithParams("searchElevatorSaleAction","toSearchRecord2",paramstring);

		if(returnarray!=null && returnarray.length>0){
			addRows(tableId,returnarray.length);
			toSetInputValue(returnarray,"WeiGchangeForm");
		}			
}

//返回元素指定的父节点对象
function getFirstSpecificParentNode(parentTagName,childObj){
	var parentObj = childObj.parentNode;
	
	while(parentObj){
		if(parentObj.nodeName.toLowerCase() == parentTagName.toLowerCase()){				
			break;
		}
		parentObj = parentObj.parentNode;
	}
	
	return parentObj;
}

//判断页面的值
function checkInput(){
	if(checkColumnInput(WeiGchangeForm) && checkRowInput('table_22','tb_head') && checkRowInput('table_1','wordtd_header')){
		return true;
	}else{
		return false;
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