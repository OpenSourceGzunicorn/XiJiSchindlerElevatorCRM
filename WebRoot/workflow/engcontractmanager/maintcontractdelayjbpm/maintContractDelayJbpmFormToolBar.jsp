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
	var flags=getSelectValue();
	if(flags!="" && flags!="1"){
		alert("抱歉,您选择了'不同意',请在审批意见栏上填写否决的意见!");
		return;
	}
	if('${approve}' == "Y" || checkEmpty("scrollTable","titleRow")){
		document.getElementById("SaveBtn").disabled=true;
		document.maintContractDelayJbpmForm.submit();
	}
}

//查看流程
function viewFlow(){
		
	var avf=document.getElementById("avf");//查看审批流程页面链接
	var status=document.getElementById("status").value;//审核状态	
	var flowname=document.getElementById("flowname").value;//流程名称 
	var tokenid=document.getElementById("tokenId").value;//流程令牌
	
	if(status && status == "-1"){
		alert("流程未启动，无法查看审批流程图！");
		return;
	}
	if(tokenid == null || tokenid.value==""){
		alert("该记录为历史数据，无法查看审批流程图！");
		return;
	}

	avf.href='<html:rewrite page="/viewProcessAction.do"/>?method=toViewProcess&tokenid='+tokenid+'&flowname='+flowname;
	avf.click();
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

function pickEndDay(sdate){
	var date = new Date();
	var startDate = sdate;
	var todayDate = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
	var d = startDate < todayDate ? todayDate.split('-') : startDate.split('-');
	WdatePicker({isShowClear:false,minDate:d[0]+'-'+d[1]+'-'+(Number(d[2])+1),readOnly:true});
}

function setDatesByName(name,newDate){
  if(newDate && newDate!=""){
    var dates = document.getElementsByName(name);
          
    for ( var i = 0; i < dates.length; i++) { 
      if(dates[i].value == ""){
        dates[i].value = newDate;
      }  
    }  
  }
}

//明细表中，当日期控件第一行选择日期时，自动填上同一列为空的日期
function setTopRowDateInputsPropertychange(table){
  
  var tbody = table.getElementsByTagName("tbody")[0]; 
  var rows = tbody.childNodes;
  
  for(var i=0;i<rows.length;i++){
    if(!rows[i].id){
      var inputs = rows[i].getElementsByTagName("input");
      for(var j=0;j<inputs.length;j++){
       
        if(inputs[j].className.indexOf("Wdate")>=0){
           inputs[j].onpropertychange = function(){setDatesByName(this.name,this.value);};
        }
      }
      break;
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