<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="ncustomercisitfeedback" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    <logic:present name="submit">  
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'保存并提交',"90","1","saveMethod2()");
   </logic:present>
    </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//去掉空格
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

//返回
function returnMethod(){
	
       window.location = '<html:rewrite page="/customerVisitFeedbackSearchAction.do"/>?method=toSearchRecord';

}

//保存
function saveMethod(){
	<logic:present name="submit"> 
	inputTextTrim();
	if(checkInput()){
		document.customerVisitFeedbackForm.isreturn.value = "Y";
		document.getElementById("isSubmitType").value="N";
 		document.customerVisitFeedbackForm.submit();	
	}
	</logic:present>
	<logic:notPresent name="submit"> 
		var visitStaff=document.getElementById("visitStaff").value;
		if(visitStaff==""){
			alert("被转派人 不能为空！");
			return;
		}
		document.customerVisitFeedbackForm.submit();
	</logic:notPresent>
}

//保存提交
function saveMethod2(){
	inputTextTrim();
	if(checkInput()){
		document.customerVisitFeedbackForm.isreturn.value = "Y";
		document.getElementById("isSubmitType").value="Y";
 		document.customerVisitFeedbackForm.submit();	
	}
}

//添加反馈信息
function addElevators(tableId){
	var jnlno = customerVisitFeedbackForm.jnlno.value;	
	
	if(jnlno!=null&&jnlno!=""){
		var paramstring = "jnlno="+jnlno+"&elevatorNos=";		
		var billNos = document.getElementsByName("billNo");
		for(i=0;i<billNos.length;i++){
			paramstring += i<billNos.length-1 ? "|"+billNos[i].value+"|," : "|"+billNos[i].value+"|"		
		}
		
		var returnarray = openWindowWithParams("searchProjectNameAction","toSearchRecord",paramstring);

		if(returnarray!=null && returnarray.length>0){					
			 addRows(tableId,returnarray.length);//增加行
			toSetInputValue(returnarray,"customerVisitFeedbackForm");
		}		
	}else{
		alert("不能添加！");
	}
	
}

function checkInput(){
	 //return checkRowInput("dynamictable_0","titleRow_0");
	 var visitPeople= document.getElementById("visitPeople").value;
	 if(visitPeople.trim()==""){
		 alert("被拜访人  不能为空！");
		 return false;
	 }
	 var visitPeoplePhone= document.getElementById("visitPeoplePhone").value;
	 if(visitPeoplePhone.trim()==""){
		 alert("被拜访人电话  不能为空！");
		 return false;
	 }
	 
	 var projectName = document.getElementsByName("projectName");
	 if(projectName.length>0){
		 var realvisitDate = document.getElementsByName("realvisitDate");
		 var visitContent = document.getElementsByName("visitContent");
		 var isreturn=true;
		 for(var i=0;i<projectName.length;i++){
			 if(realvisitDate[i].value==""){
				 alert("第"+(i+1)+" 行 实际拜访日期 不能为空 ！");
				 isreturn=false;
				 break;
			 }
			 if(visitContent[i].value==""){
				 alert("第"+(i+1)+" 行 拜访内容/收获 不能为空 ！");
				 isreturn=false;
				 break;
			 }
		 }
		 return isreturn;
	 }else{
		 alert("请添加 拜访项目信息 ！");
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