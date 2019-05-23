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
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nprincipal" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="toolbar.returnsave"/>',"80","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/principalSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
  if (checkForm()) {
    document.principalForm.isreturn.value = "N";
    document.principalForm.submit();
  }
}

//保存返回
function saveReturnMethod(){
  if (checkForm()) {
    document.principalForm.isreturn.value = "Y";
    document.principalForm.submit();
  }
}

//表单验证
function checkForm(){  
  var object = document.principalForm; //指定表单对象
  for ( var i = 0; i < object.length; i++ ) {
    var value = object[i].value;
    var name = object[i].name; 
    
    if(name == "principalId"){
      if(value == null || value == ''){
        alert("负责人代码不能为空！");
        return false;      
      }
    }
    
    if(name == "principalName"){
      if(value == null || value == ''){
        alert("负责人名称不能为空！");    
        return false;      
      }        
    }
    
    if(name == "phone"){
      if(value == null || value == ''){
        alert("负责人电话不能为空！");
        return false;      
      }    
    }    
    
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