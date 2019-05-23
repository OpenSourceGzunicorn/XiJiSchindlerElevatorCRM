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
  window.location = '<html:rewrite page="/qcLogManagementSearchAction.do"/>?method=toSearchRecord';
}


//保存
function saveMethod(){
	if(checkform()){
		document.qcLogManagementForm.isreturn.value = "N";
    	document.qcLogManagementForm.submit();
	}
	
}

//保存返回
function saveReturnMethod(){
	if(checkform()){
		document.qcLogManagementForm.isreturn.value = "Y";
    	document.qcLogManagementForm.submit();
	}
}

//校验
function checkform(){
	var maintStation = document.getElementById("maintStation");//维保站
	var MaintPersonnel = document.getElementById("MaintPersonnel");//维保工
	var ElevatorNo = document.getElementById("ElevatorNo");//电梯编号
	var ydlh = document.getElementById("ydlh");//月度例会
	var isgzfz = document.getElementById("isgzfz");//维保负责人是否对工作负责
	var iszfwt = document.getElementById("iszfwt");//维保负责人是否存在作风问题
	var jffkwt = document.getElementById("jffkwt");//甲方反馈问题
	var ycjkwt = document.getElementById("ycjkwt");//远程监控问题
	
	if(maintStation.value == ""){
		alert("请选择维保站！");
		return false;
	}
	if(MaintPersonnel.innerHTML==""){
		alert("请选择维保工！");
		return false;
	}
	if(ElevatorNo.value==""){
		alert("电梯编号不能为空！");
		return false;
	}
	if(ydlh.value==""){
		alert("请选择月度例会！");
		return false;
	}
	if(isgzfz.value==""){
		alert("请选择维保负责人是否对工作负责！");
		return false;
	}
	if(iszfwt.value==""){
		alert("请选择维保负责人是否存在作风问题！");
		return false;
	}
	if(jffkwt.innerHTML==""){
		alert("甲方反馈问题不能为空！");
		return false;
	}
	if(ycjkwt.innerHTML==""){
		alert("远程监控问题不能为空！");
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