<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<style type="text/css">
<!--
body {
	background-color: #F4F2F2;
	background-image:url(images/message_bg.gif);
	background-repeat:no-repeat;
	background-position:center center;
	scrollbar-face-color: #DEE3E7;
    scrollbar-highlight-color: #FFFFFF;
    scrollbar-shadow-color: #DEE3E7;
    scrollbar-3dlight-color: #D1D7DC;
    scrollbar-arrow-color:  #006699;
    scrollbar-track-color: #EFEFEF;
    scrollbar-darkshadow-color: #98AAB1;
}
-->
</style><br>
<html:errors/>
<logic:present name="messagebeen"> <br />
<table border="0" cellpadding="2" cellspacing="0" align="center">
  <tr>
    <td align="center" style="font-size:12pt; font-weight:bold;">пео╒дзхщ<br />
</td>
  </tr>
  <tr>
    <td align="center" style="font-size:12pt; font-weight:bold;"><img src="images/message_title.GIF" width="538" height="4" /></td>
  </tr>	
  <tr>
    <td style="font-size:12pt;line-height:25px;"><bean:write name="messagebeen" scope="request" property="promulgatecontent"/></td>
    </tr>
  </table>
</logic:present>
