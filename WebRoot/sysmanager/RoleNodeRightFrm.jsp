<%@ page language="java" contentType="text/html;charset=GB2312" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:html locale="true">
<html:base/>

<title><bean:message key="app.title"/></title>
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
</head>

<body>
<table width="100%">
 <tr>
    <td width="100%">
	   	<iframe src="<html:rewrite page='/sysmanager/RoleNodeInfo.jsp'/>" id="SaveFrm" scrolling="no" frameborder="no" border="0" framespacing="0" align="middle" noresize width="98%" height="30">		
      	</iframe>
      </td>
   </tr>
  </table>


<table width="100%"  border="0" align="center"  cellpadding="0" cellspacing="0">
<tr> 
<td>
<form method="post" action="">
  <table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0" >
	<input type="hidden" name="roleID" value="<bean:write name='roleID' />">
    <tr>
      <td><table width="96%"  border="1" align="center" cellpadding="0" cellspacing="0" bordercolordark="#FEFEFE" bordercolorlight="#808080">
        <tr>
          <td height="26" width="45%" valign="middle" bgcolor="#5372A7"><div align="center" class="style1"><bean:message key="rolenode.function"/></div></td>
          <td valign="middle" bgcolor="#5372A7"><div align="center" class="style1"><bean:message key="rolenode.authority"/></div></td>
        </tr>
	  <logic:iterate id="roleNodeBean"  type="com.gzunicorn.bean.RoleNodeBean" name="roleNodeList" >
        <tr>

		<logic:equal name="roleNodeBean" property="nodeURL" value="">
          <td width="45%" height="22" bgcolor="#9eccfe">
		</logic:equal>

		<logic:notEqual name="roleNodeBean" property="nodeURL" value="">
          <td width="45%" height="22" bgcolor="#E6F2FF">
		</logic:notEqual>

		  <div align="center"><bean:write name="roleNodeBean" property="nodeName" /></div></td>
          <td bgcolor="#E8ECFD">
		  
		  <logic:equal name="roleNodeBean" property="nodeURL" value="">
			  <logic:equal name="roleNodeBean" property="writeFlag" value="A" >
    	        <input type="radio" name="<bean:write name='roleNodeBean' property="nodeID" />" value="A" checked>
         	   <bean:message key="rolenode.cannotread"/>
            	<input type="radio"  name="<bean:write name='roleNodeBean' property="nodeID" />" value="N">
	            <bean:message key="rolenode.canread"/>
			</logic:equal>

			   <logic:notEqual name="roleNodeBean" property="writeFlag" value="A" >
    	        <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="A">
         	   <bean:message key="rolenode.cannotread"/>
            	<input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="N" checked>
	            <bean:message key="rolenode.canread"/>
				</logic:notEqual>
		  </logic:equal>
		  
		 <logic:notEqual name="roleNodeBean" property="nodeURL" value="">

		  <logic:equal name="roleNodeBean" property="writeFlag" value="A" >
            <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="A" checked>
            <bean:message key="rolenode.cannotread"/>
            <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="N">
            <bean:message key="rolenode.canread"/>
            <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="Y">
            <bean:message key="rolenode.canwrite"/>
			</logic:equal>

		  <logic:equal name="roleNodeBean" property="writeFlag" value="N" >
            <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="A">
            <bean:message key="rolenode.cannotread"/>
            <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="N" checked>
            <bean:message key="rolenode.canread"/>
            <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="Y">
            <bean:message key="rolenode.canwrite"/>
			</logic:equal>

		  <logic:equal name="roleNodeBean" property="writeFlag" value="Y" >
            <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="A">
            <bean:message key="rolenode.cannotread"/>
            <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="N">
            <bean:message key="rolenode.canread"/>
            <input type="radio"   name="<bean:write name='roleNodeBean' property="nodeID" />" value="Y"  checked>
            <bean:message key="rolenode.canwrite"/>
			</logic:equal>

		</logic:notEqual>

          </td>
        </tr>
	 </logic:iterate>
	</td>
    </tr>
  </table>
</form>
<br>
</td>
</tr>
</table>
</body>
</html:html>