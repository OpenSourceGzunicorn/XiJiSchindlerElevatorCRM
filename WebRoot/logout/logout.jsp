<%@ page contentType="text/html;charset=gb2312" language="java" %>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template" prefix="template" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<script>
if(parent.window)
{
	parent.window.location = "/XJSCRM/";
}
else
{
	window.location = "/XJSCRM/";
}
//window.close();
</script>
<html:html locale="true">
  <head>
    <html:base />
    <title>logout.jsp</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
  </head>
  
  <body>
    	<br/>&nbsp;&nbsp;&nbsp;&nbsp;登录用户已注销，请重新登录。。。<br/>
  </body>
</html:html>