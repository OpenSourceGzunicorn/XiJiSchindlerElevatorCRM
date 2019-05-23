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
 <%--  判断更改密码模块changePassword是否有可写的权限,在property	--%>
 <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nchangePassword" value="Y"> 
  AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","0","saveMethod()");
 </logic:equal> 
 window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  	 window.location = '<html:rewrite page="/changePasswordAction.do?method=toModifyRecord"/>';
 }

//保存
function saveMethod(){
  if(validateChangePasswordForm(document.all.item("changePasswordForm"))==true){
    document.changePasswordForm.submit();
  }
}

//保存返回
function saveReturnMethod(){
 if(validateCityForm(document.all.item("cityForm"))==true){
document.cityForm.isreturn.value = "Y";
document.cityForm.submit();
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
              <!--
                CreateToolBar();
                //SetToolBarAttribute();
              //-->
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>