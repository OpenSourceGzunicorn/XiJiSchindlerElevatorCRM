<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" charset="GBK">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nelevatortransfercaseregistermodify" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

function toElevator(actionName,flag)
{
	var idType=document.getElementsByName("contractType")[0].value;
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url="query/Search.jsp?path="+projectName+actionName+".do?idType="+idType;  
	var returnvalue = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');	 
	toSetInputValue2(returnvalue,flag); 
}

//返回
function returnMethod(){
  window.location = '<html:rewrite page="/elevatorTransferCaseRegisterModifySearchAction.do"/>?method=${returnMethod}';
}

//保存
function saveMethod(){
	var firstInstallation=document.getElementById("firstInstallation").value;
	if(firstInstallation==""){
		alert("请选择 是否初次安装！");
		return;
	}
    document.elevatorTransferCaseRegisterManageForm.isreturn.value = "N";
    document.elevatorTransferCaseRegisterManageForm.submit();

}

//保存返回
function saveReturnMethod(){
    document.elevatorTransferCaseRegisterManageForm.isreturn.value = "Y";
    document.elevatorTransferCaseRegisterManageForm.submit();
}

//下载附件
function downloadFile(name){
	var uri = '<html:rewrite page="/elevatorTransferCaseRegisterDisplayAction.do"/>?method=toDownloadFileRecord';
	var name1=encodeURI(name);
	name1=encodeURI(name1);
		uri +='&filesname='+ name1;
		uri +='&folder=HandoverElevatorCheckItemRegister.file.upload.folder';
	window.location = uri;
}

//下载附件
function downloadFile(name,oldName,filePath){
	var uri = '<html:rewrite page="/elevatorTransferCaseRegisterDisplayAction.do"/>?method=toDownloadFileDispose';
	var name1=encodeURI(name);
	name1=encodeURI(name1);
	var oldName1=encodeURI(oldName);
	oldName1=encodeURI(oldName1);
	filePath=encodeURI(filePath);
	filePath=encodeURI(filePath);
	    uri +='&filePath='+ filePath;
		uri +='&filesname='+ name1;
		uri +='&folder=HandoverElevatorCheckItemRegister.file.upload.folder';
		uri+='&fileOldName='+oldName1;
	window.location = uri;
	//window.open(url);
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