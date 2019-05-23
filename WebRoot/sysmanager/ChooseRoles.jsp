<%@ page language="java" import="java.util.*" pageEncoding="GBK" import="com.gzunicorn.hibernate.Role"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<html:base/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<style type="text/css">
<!--
body,td,th {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 9pt;
}
.style1 {color: #ffffff;
         font-weight: bold;
		 }
-->
</style>
    <title>My JSP 'ChooseRoles.jsp' starting page</title>
  </head>
  
  <body>

<html:form  action="/QuotationAction2.do?method=toUpdateRecord">
    <table width="50%"  border="0" align="center" cellpadding="0" cellspacing="0" bordercolordark="#FEFEFE" bordercolorlight="#808080">
      <tr >
       <td bgcolor="#5372A7" height="22"><div align="center" class="style1">╫ги╚ап╠М</div></td>
      <td bgcolor="#5372A7" height="22"></td>
      </tr>
      

      
      <logic:iterate id="role" name="roles" type="com.gzunicorn.hibernate.Role">
      <tr>
       <td bgcolor="#E6F2FF" height="22">
       <input type="checkbox" name="roleIds" value="<bean:write name="role" property="roleid"/>">
       </td>
       <td bgcolor="#E6F2FF" height="22"><bean:write name="role" property="rolename"/></td>
      </tr>
      </logic:iterate>
      
    </table>
  </html:form>  
  <html:javascript formName="chooseRoleForm"/>
  </body>
</html>






