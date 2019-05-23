<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
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
  <logic:match name="taskname" value="关闭流程">
  		AddToolBarItemEx("printBtn","../../common/images/toolbar/print.gif","","",'打印通知单',"100","1","printMethod()");
  </logic:match>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//去掉空格
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g,""); }

//打印
function printMethod(){
	var jnlNo=document.getElementById("jnlNo").value;
	window.open('<html:rewrite page="/paymentManageAction.do"/>?id='+jnlNo+'&method=toPrintRecord');
}

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
		alert("抱歉,您选择了'不通过',请在审批意见栏上填写否决的意见!");
		return;
	}
	if(checkColumnInput(contractPaymentManageJbpmForm) && checkInput()){
		document.getElementById("SaveBtn").disabled=true;
		document.contractPaymentManageJbpmForm.submit();
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
 	if(val!="" && (val=="关闭" || val=="通过" || val.indexOf("提交")>-1)){
 	    str="1";
 	}else{
 		if(document.getElementById("approverem").value.trim()!="" || document.getElementById("approverem").value.trim().length>0){
 			str="1";
 		}
 	}
 	return str;
	
}


function checkInput(){
	var alerg="";
	<logic:match name="taskname" value="例行回访结果审核">
		var hfAuditNum=document.getElementsByName("hfAuditNum")[0];
		var hfAuditNum2=document.getElementsByName("hfAuditNum2")[0];
		if(hfAuditNum.value==null || isNaN(hfAuditNum.value))
			alerg+="因技术问题引起的负面意见次数只能输入数字！\n";
		if(hfAuditNum2.value==null || isNaN(hfAuditNum2.value))
			alerg+="因非技术问题引起的负面意见次数只能输入数字！\n";
	</logic:match>
	
	<logic:match name="taskname" value="热线管理人审核">
		var rxAuditNum=document.getElementsByName("rxAuditNum")[0];
		var rxAuditNum2=document.getElementsByName("rxAuditNum2")[0];
		if(rxAuditNum.value==null || isNaN(rxAuditNum.value))
			alerg+="因技术问题引起的投诉次数只能输入数字！\n";
		if(rxAuditNum2.value==null || isNaN(rxAuditNum2.value))
			alerg+="因非技术问题引起的投诉次数只能输入数字！\n";
	</logic:match>
	
	<logic:match name="taskname" value="分部长审核">
		var debitMoney=document.getElementsByName("debitMoney")[0];
		if(debitMoney.value==null || isNaN(debitMoney.value))
			alerg+="扣款金额只能输入数字！\n";
	</logic:match>
	
	if(alerg!=""){
		alert(alerg);
		return false;
	}else{
		return true;
	}
}

//检查金额是否为数字,不可以输入负号和可以输入点号
function f_check_number3(){
 	if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==46 ){
 	}else{
		event.keyCode=0;
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