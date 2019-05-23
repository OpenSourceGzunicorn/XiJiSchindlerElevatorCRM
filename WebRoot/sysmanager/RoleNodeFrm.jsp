<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>

<html:html>
<head>
<html:base/>
<title><bean:message key="app.title"/></title>
</head>

<frameset id="main" rows="*" cols="0,35%,75%" border="0">
	<frame name="hiddenFrame" scrolling="no" noresize="noresize" src="hiddenbar.jsp">
	<frame name="leftFrm" scrolling="auto" src="<html:rewrite page='/roleNodeAction.do?method=toLeftFrm'/>">
	<frame name="rightFrm" src="RoleNodeBlank.jsp">
</frameset>
<noframes>
<body>
<center>
<bean:message key="rolenode.error.frameset"/>
</center>
</body>
</noframes>
</html:html>