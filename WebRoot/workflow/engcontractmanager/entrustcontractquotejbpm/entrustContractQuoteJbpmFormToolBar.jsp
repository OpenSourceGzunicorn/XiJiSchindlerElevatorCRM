<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
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
	var flags=getSelectValue();
	if(flags!="" && flags!="1"){
		alert("抱歉,您选择了'不同意',请在审批意见栏上填写否决的意见!");
		return;
	}
	if('${approve}' == "Y" || checkColumnInput(entrustContractQuoteJbpmForm)){
		document.getElementById("SaveBtn").disabled=true;
		document.entrustContractQuoteJbpmForm.submit();
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
 	if(val!="" && (val=="同意" || val=="关闭" || val.indexOf("提交")>-1)){
 	    str="1";
 	}else{
 		if(document.getElementById("approverem").value.trim()!="" || document.getElementById("approverem").value.trim().length>0){
 			str="1";
 		}
 	}
 	return str;
	
}

function addElevators(tableId){
	var tableObj = document.getElementById(tableId);
	var paramstring = "elevatorNos=";		
	var elevatorNos = document.getElementsByName("elevatorNo");
	for(i=0;i<elevatorNos.length;i++){
		paramstring += i<elevatorNos.length-1 ? "|"+elevatorNos[i].value+"|," : "|"+elevatorNos[i].value+"|"		
	}

	var returnarray = openWindowWithParams("searchElevatorSaleAction","toSearchRecord2",paramstring);

	if(returnarray!=null && returnarray.length>0){					
		addRows(tableId,returnarray.length);
		toSetInputValue(returnarray,"entrustContractQuoteJbpmForm");
	}			
}

function checkInput(){  
	inputTextTrim();// 页面所有文本输入框去掉前后空格
	var boo = false;
	var elevatorNos = document.getElementsByName("elevatorNo"); 
	if(elevatorNos !=null && elevatorNos.length>0){
		boo = checkColumnInput(entrustContractQuoteJbpmForm) && checkRowInput("dynamictable_0","titleRow_0");
	}else{
		alert("不能保存，请添加合同明细。");
	}
	return boo;
}

function calculation(){
	var standard=document.getElementsByName("standardPrice")[0];
	var real=document.getElementsByName("realPrice")[0];
	var markups=document.getElementsByName("markups")[0];
	var m=0;
	if(!isNaN(standard.value) && !isNaN(real.value)){
		m=(real.value-standard.value)/standard.value;
		if(m>0)
			markups.value=parseFloat(m*100).toFixed(2);
		else
			markups.value=0.00;
	}else{
		var agr=null;
		if(isNaN(standard.value)){
			agr="标准委托价格只能输入数字，请重新输入！";
			standard.value=0;
		}
		if(isNaN(real.value)){
			agr="实际委托价格只能输入数字，请重新输入！";
			real.value=0;
		}
		if(agr!=null){
			alert(agr);
			markups.value=0.00;
		}
	}
}

function choiceCustomer(){
	var returnarray=openWindowWithParams('searchCustomerAction','toSearchRecord2','quote=WB');
	toSetInputValue2(returnarray,"");
}


//计算合同有效期
function setContractPeriod(){
  var sd = entrustContractQuoteJbpmForm.contractSdate.value.split("-");
  var ed = entrustContractQuoteJbpmForm.contractEdate.value.split("-");
  if(sd != "" && ed != ""){  
    var years = Number(ed[0])-Number(sd[0]);
    var months = years>0 ? 12*years - (Number(sd[1]) - Number(ed[1])) : Number(ed[1]) - Number(sd[1]);
    months = Number(ed[2])-Number(sd[2])>0 ? months+1 : months;  
    entrustContractQuoteJbpmForm.contractPeriod.value = months;     
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