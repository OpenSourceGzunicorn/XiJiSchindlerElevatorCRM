<%@ page contentType="text/html;charset=gb2312" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<html:html locale="true">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>西继迅达(许昌)电梯有限公司CRM系统</title>
<link href="<html:rewrite forward='LogonCSS'/>" rel="stylesheet" type="text/css">
</head>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<body >
<br/><br/>
<table width="100%" height="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td style="background-image:url(images/xjs_log.jpg);background-repeat:no-repeat;">
	   <html:form action="/Logon" focus="userID">
	      <table width="99%"  border="0" align="right" cellpadding="0" cellspacing="0">
	        <tr>
	          <td height="130" >&nbsp;</td>
	          <td>&nbsp;</td>
	          <td>&nbsp;</td>
	          <td align="center">
	          	<%-- input type="image" src="../../uploadFile/localhost.png"--%>
	          	<image src="<%=basePath%>getImageServlet"/>
	          	<%-- 
	          	<br/>
	          	<font size="-2">请扫描二维条码,下载客户端版本</font>
	          	--%>
	          </td>
	        </tr>
	        <tr>
	          <td height="130" >&nbsp;</td>
	          <td>&nbsp;</td>
	          <td>&nbsp;</td>
	          <td>&nbsp;</td>
	        </tr>
	        <tr>
	         <td>&nbsp;</td>
	          <td>&nbsp;</td>
	          <td width="152" height="25" class="text"><html:errors/>&nbsp;</td>
	          <td width="200" >&nbsp;</td>
	        </tr>
	        <tr>
	         <td>&nbsp;</td>
	          <td width="75" class="text"><div align="right"><bean:message key="logon.userID"/>：</div></td>
	          <td width="152" height="25" class="text"><html:text property="userID" size="15" maxlength="18"/></td>
	          <td>&nbsp;</td>
	        </tr>
	        <tr>
	          <td>&nbsp;</td>
	          <td class="text"><div align="right"><bean:message key="logon.passwd"/>：</div></td>
	          <td width="152" height="25" class="text"><html:password property="passwd" size="14" maxlength="18" redisplay="false"/></td>
	          <td>&nbsp;</td>
	        </tr>
	        <tr>
	          <td>&nbsp;</td>
	          <td height="35" colspan="2" valign="bottom">
		          <div align="center">
		              <input type="image" src="images/login.JPG">
		              　		  <input type="image" src="images/cancel.JPG">
		          </div>
	          </td>
	          <td>&nbsp;</td>
	          </tr>
	        <tr>
	          <td height="230" >&nbsp;</td>
	          <td>&nbsp;</td>
	          <td>&nbsp;</td>
	          <td>&nbsp;</td>
	        </tr>
	      </table>
    	</html:form>
    </td>
  </tr>
</table>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr><td align="center"><font size="-1">为了获得最佳体验，建议使用 <font color="red">IE</font> 浏览器，分辨率在 <font color="red">1024x768</font> 或以上。</font></td></tr>
</table>
</body>
</html:html> 