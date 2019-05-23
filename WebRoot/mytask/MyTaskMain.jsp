<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>

<html:html>
<head> 
<html:base/>
<title><bean:message key="web.Title"/></title>
</head> 
<!--frameset id="Index" cols="*,12,60%"  border="0"-->
<frameset id="Index" cols="*"  border="0">
	<!--frame name="upFrame"   src="<html:rewrite page="/myTaskOaSearchAction.do"/>?method=toMyTask" scrolling="auto" noresize="noresize"-->
	<frame name="upFrame"   src="<html:rewrite page="/myTaskOaSearchAction.do"/>?method=toDoList" scrolling="auto" noresize="noresize">
	<!--frame src="hiddenbar.jsp" name="f5" framespacing="0"  scrolling="no" noresize>
	<frame name="Approve" src="MyTaskDownFrm.jsp" scrolling="auto" noresize="noresize"-->
</frameset>
<noframes>
<body>
<center>
对不起，你的浏览器不支持框架(Frameset)技术，无法浏览本网页。
</center>
</body>
</noframes>

</html:html>