<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<html:base/>
<html:errors/>
<%--?????????--%>
<body bgcolor="#ffffff">
<table border="0" width="100%">
 <tr>
    <td width="100%">

	   	<iframe src="<html:rewrite page='/sysmanager/RoleUserFrm.jsp'/>" id="MainFrm" scrolling="auto" frameborder="no" border="0" framespacing="0" align="middle" noresize width="100%" height="500">		
      	</iframe>
      </td>
   </tr>
  </table>
</body>