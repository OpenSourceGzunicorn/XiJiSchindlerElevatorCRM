<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>

<html:html>
<head>
<html:base/>
<title><bean:message key="app.title"/></title>
</head>

<frameset id="main" rows="*" cols="0,20%,*" border="0">
	<frame name="hiddenFrame" scrolling="no" noresize="noresize" src="hiddenbar.jsp">
	<frame name="leftFrame" scrolling="auto" src="treeNode.jsp">
	<frameset id="rmain" rows="0,20%,*" cols="0" border="0">
		<frame name="rightupFrame" scrolling="no" noresize="noresize" src="righthiddenbar.jsp">
		<frame name="rightcenterFrame" scrolling="auto" src='<html:rewrite page="/dutysearchAction.do?method=toSearchRecord"/>'>
		<frame name="rightFrame" src="welcome.jsp">
	</frameset>	
</frameset>

<noframes>
<body>
<center>
对不起，你的浏览器不支持框架(Frameset)技术，无法浏览本网页。
</center>
</body>
</noframes>
</html:html>
