<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:html>
  <head>
<html:base />
	<title><bean:message key="app.title"/></title>
  </head>
  <body>
 
	<form name="nodeRoleForm" method="post" action="../roleNodeAction.do?method=toSaveFrm">	
		<br>
		&nbsp;&nbsp;<bean:message key="rolenode.datastatus"/>£º<html:errors/>
		<input type="hidden" name="nodeRole" value="">
		<input type="hidden" name="roleID" value="">
    </form>
  </body>
</html:html>
