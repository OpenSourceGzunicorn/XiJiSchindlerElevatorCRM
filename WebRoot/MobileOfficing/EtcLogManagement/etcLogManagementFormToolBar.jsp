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

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:equal value="true" name="addflag">
  AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/save_back.gif","","",'保存并返回',"90","1","saveReturnMethod()");
  </logic:equal>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/etcLogManagementSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
	if(checkform()){
		document.etcLogManagementForm.isreturn.value = "N";
    	document.etcLogManagementForm.submit();
	}
	
}

//保存返回
function saveReturnMethod(){
	if(checkform()){
		document.etcLogManagementForm.isreturn.value = "Y";
    	document.etcLogManagementForm.submit();
	}
}

//校验
function checkform(){
	var r = /^\+?[1-9][0-9]*$/;//正整数
	
	var contractno = document.getElementById("contractno");//合同号
	var projectname = document.getElementById("projectname");//项目名称
	var inscompanyname = document.getElementById("inscompanyname");//安装单位
	var phnum = document.getElementById("phnum");//配合人数
	var iscjwx = document.getElementById("iscjwx");//厂检/维修
	var iszj = document.getElementById("iszj");//是否自检
	var xcfkwt = document.getElementById("xcfkwt");//现场反馈问题
	var workcontent = document.getElementById("workcontent");//厂检完成情况汇总
	
	if(contractno.value==""){
		alert("合同号不能为空！");
		return false;
	}
	if(projectname.value==""){
		alert("项目名称不能为空！");
		return false;
	}
	if(inscompanyname.innerHTML==""){
		alert("安装单位不能为空！");
		return false;
	}
	if(!r.test(phnum.value)){
		alert("配合人数请填入数字！");
		return false;
	}
	if(iscjwx.value==""){
		alert("厂检/维修不能为空！");
		return false;
	}
	if(iszj.value==""){
		alert("请选择是否自检！");
		return false;
	}
	if(xcfkwt.innerHTML==""){
		alert("现场反馈问题不能为空！");
		return false;
	}
	if(workcontent.innerHTML==""){
		alert("厂检完成情况汇总不能为空！");
		return false;
	}
	
	return true;
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