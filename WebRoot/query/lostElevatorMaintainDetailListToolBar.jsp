<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript">
//¹ØÓÚToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  <logic:notPresent name="isOpen">
  	
  	AddToolBarItemEx("CloseBtn","../../common/images/toolbar/close.gif","","",'<bean:message key="toolbar.close"/>',"65","0","closeMethod()"); 
  </logic:notPresent>
  <logic:present name="isOpen">
  	AddToolBarItemEx("CloseBtn","../../common/images/toolbar/close.gif","","",'<bean:message key="toolbar.close"/>',"65","0","closeMethod()");
  </logic:present>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//¹Ø±Õ
function closeMethod(){
  window.close();
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
              //-->
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>