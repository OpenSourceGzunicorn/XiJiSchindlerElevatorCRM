
<%@ page language="java" import="java.util.*" pageEncoding="GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%
  String headerId =(String)request.getAttribute("headerId");
%>
<SCRIPT LANGUAGE=javascript>


function _submit()
{ 
  document.nullForm.submit();

}
// 为什么onsubmit="javaScript:test()" 没反应
function test(){
 alert("ok");
}

</SCRIPT>
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
    
    <title>My JSP 'RoleUserRightFrm.jsp' starting page</title>

  </head>
  
  <body>

   <table width="90%" height="30" border="0" align="center" cellpadding="2" cellspacing="0" >   
    <tr>
     <td nowrap>保存状态：<html:errors/></td>
    </tr>
    <tr>
    <td>工程业务员列表:</td>
    </tr>
  </table>
 

  <table width="90%"  border="1" align="center" cellpadding="0" cellspacing="0" bordercolordark="#FEFEFE" bordercolorlight="#808080">
  <html:form action="/EngineeringSalesmanAction.do?method=EngineUser" method="POST" onsubmit="javaScript:test()">
      <tr align="center">
      <td bgcolor="#5372A7" height="22" >　　</td>
      <td bgcolor="#5372A7" height="22" ><div align="center" class="style1">工程业务员编号</div></td>
       <td bgcolor="#5372A7" height="22" ><div align="center" class="style1">工程业务员姓名</div></td>
      </tr>

       <logic:iterate id="element" name="operationMen" type="com.gzunicorn.hibernate.engineeringmanagement.ViewProUserOperation">        
			  <tr align="center">
			   <td bgcolor="#E6F2FF" height="22">
				   <logic:equal name="element" property="headerId" value="<%=headerId%>" >
					 <input type="checkbox" name="userIds" value="<bean:write name="element" property="userId"/>" checked >
				   </logic:equal>
				   <logic:notEqual name="element" property="headerId" value="<%=headerId%>" >
					 <input type="checkbox" name="userIds" value="<bean:write name="element" property="userId"/>" >
				   </logic:notEqual>
			  </td>
			  <td bgcolor="#E6F2FF" height="22">
			   <bean:write name="element" property="userId"/>
			  </td>
			   <td bgcolor="#E6F2FF" height="22"><bean:write name="element" property="userName"/></td>
			  </tr>
	  </logic:iterate>
	  <input type="hidden" name="headerId"  value="<%=headerId%>">
      <input type="hidden" name="deletedIds" value=""/>
      <input type="hidden" name="addedIds" value=""/>
      </html:form>
    </table>

  </body>
</html>