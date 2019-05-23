<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  <logic:present name="workisdisplay">  
	AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","closeMethod()");
	//AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","window.close()");
</logic:present>
<logic:notPresent name="workisdisplay">  

	  <logic:equal name="typejsp" value="Yes">
	    AddToolBarItemEx("closeBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","window.close()");
	  </logic:equal>
	  <logic:notPresent name="typejsp">
	    AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
	  </logic:notPresent>
	  <logic:notPresent name="display">  
		  <%-- 是否有可写的权限--%>
		  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncontractinvoicemanage" value="Y"> 
		    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");   
		    <logic:notPresent name="Backfill">
		      AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="tollbar.saveandrefer"/>',"120","1","saveReturnMethod()");
		    </logic:notPresent>
		  </logic:equal>
	  </logic:notPresent>
	  
  </logic:notPresent> 
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");

}

//关闭
function closeMethod(){
  window.close();
}

//返回
function returnMethod(){
  window.location = '<html:rewrite page="/contractInvoiceManageSearchAction.do"/>?method=toSearchRecord';
}
 <logic:notPresent name="Backfill">
//保存
function saveMethod(){
  if(checkColumnInput(contractInvoiceManageForm)){
	  if(isNaN(document.getElementById("r9").value)){
		  alert("台数 只能输入数字！");
		  return;
	  }
	  if(document.getElementById("invoiceMoney")!=null && !isNaN(document.getElementById("invoiceMoney").value)){
		  if(parseFloat(document.getElementById("invoiceMoney").value)<=${contractBean.nobilMoney}){
		  	var zero=0;
		  	if(zero!=${contractBean.nobilMoney}){
			  document.contractInvoiceManageForm.isreturn.value = "N";
			  document.contractInvoiceManageForm.submitType.value = "N";
			  document.contractInvoiceManageForm.submit();
			}else{
		    	alert("该记录已经没有开票金额！");
		    }
		  }else{
			  alert("开票金额不能大于未开票金额！");
		  }
		  	
	  }else{
		  alert("开票金额只能输入数字！");
	  }
	  
    
  }
  
}
</logic:notPresent>
<logic:equal name="Backfill" value="backfill">
function saveMethod(){
	if(checkColumnInput(contractInvoiceManageForm)){
		document.contractInvoiceManageForm.submit(); 
	}
}
</logic:equal>

//保存返回
function saveReturnMethod(){
  inputTextTrim();  
  if(checkColumnInput(contractInvoiceManageForm)){
	  if(isNaN(document.getElementById("r9").value)){
		  alert("台数 只能输入数字！");
		  return;
	  }
	  if(document.getElementById("invoiceMoney")!=null && !isNaN(document.getElementById("invoiceMoney").value)){
		  if(parseFloat(document.getElementById("invoiceMoney").value)<=${contractBean.nobilMoney}){
		  	var zero=0;
		  	if(zero!=${contractBean.nobilMoney}){
			  document.contractInvoiceManageForm.isreturn.value = "Y";
		      document.contractInvoiceManageForm.submitType.value = "Y";
		      document.contractInvoiceManageForm.submit();
		    }else{
		    	alert("该记录已经没有开票金额！");
		    }
		  }else{
			  alert("开票金额不能大于未开票金额！");
		  }
		  
	  }else{
		  alert("开票金额只能输入数字！");
	  }
      
    } 
}

//检查金额是否为数字,不可以输入负号和可以输入点号
function f_check_number3(){
 	if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==46 ){
 	}else{
		event.keyCode=0;
  	}
}

function judgePreMoney(object,receivables){
	var objv=object.value.substring(0,object.value.length-1);
	if(!isNaN(object.value)){
		if(parseFloat(object.value)>receivables){
			alert("开票金额不能大于未开票金额！");
			object.value=0;
		}
	}else{
		alert("开票金额只能输入数字！");
		object.value=0;
	}
}

function lessToday(object){
	var date = new Date();
	var yyyy = date.getFullYear();
	var mm = date.getMonth()+1;
	mm = mm > 10 ? mm : "0"+mm;
	var dd = date.getDate() < 10 ? "0"+date.getDate() : date.getDate();
	var todayDate = "" + yyyy + mm + dd;
	var invoiceDate=object.value.replace(/-/g,"");
	if(Number(todayDate)<Number(invoiceDate)){
		alert("开票日期不能大于当前日期！");
		object.value=yyyy+"-"+mm+"-"+dd;
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