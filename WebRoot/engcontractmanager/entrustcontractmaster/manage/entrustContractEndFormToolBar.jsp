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
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nentrustcontractmaster" value="Y">
  		AddToolBarItemEx("PassBtn","../../common/images/toolbar/digital_confirm.gif","","",'保 存',"65","1","passMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//去掉空格
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

//返回
function returnMethod(){
  window.location = '<html:rewrite page="/entrustContractMasterSearchAction.do"/>?method=toSearchRecord';
}

//审核通过
function passMethod(){
	var endDate=document.getElementById("endDate").value;
	if(endDate.trim()==""){
		alert("合同终止日期 不能为空！");
		return;
	}
	document.entrustContractMasterForm.submit();
	
}
//检查输入框是否输入数字
function checkthisvalue(obj){
  if(isNaN(obj.value)){
    alert("请输入数字!");
    obj.value="0";
    obj.focus();
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