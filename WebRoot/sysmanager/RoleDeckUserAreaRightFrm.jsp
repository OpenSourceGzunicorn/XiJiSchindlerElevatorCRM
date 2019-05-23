
<%@ page language="java" import="java.util.*" pageEncoding="GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*;"%>


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
    
    <title>My JSP 'RoleDeckUserAreaRightFrm.jsp' starting page</title>

  </head>
  
  <body>

   <table width="90%" height="30" border="0" align="center" cellpadding="2" cellspacing="0" >   
    <tr>
     <td nowrap>保存状态：<html:errors/></td>
    </tr>
    <tr>
    <td>地区列表:</td>
    </tr>
  </table>
 

  <table width="90%"  border="1" align="center" cellpadding="0" cellspacing="0" bordercolordark="#FEFEFE" bordercolorlight="#808080">
  <html:form action="/roleNodeAction.do?method=toDeckUserAssignArea" method="POST">
      <tr align="center">
      <td bgcolor="#5372A7" height="22" >　　</td>
      <td bgcolor="#5372A7" height="22" ><div align="center" class="style1">地区编号</div></td>
      <td bgcolor="#5372A7" height="22" ><div align="center" class="style1">地区姓名</div></td>
      </tr>
	  <%
	  	List arealist = (ArrayList)request.getAttribute("arealist");
	  	int len = 0;
	  	if(arealist != null)
	  	{
	  		len = arealist.size();
	  	}
	  	HashMap map;
	  	int areaidlen = 0;
	  	for(int i=0;i<len;i++)
	  	{
	  		map = (HashMap)arealist.get(i);
	  		
	  		String areaid = map.get("areaid").toString();
	  		areaidlen = areaid.length();
	  		String areaname = map.get("areaname").toString();
	  		String areaidref = map.get("areaidref").toString();
	  		String rem1 = map.get("rem1").toString();
	  %>
	  <tr align="center">
	  <%
	  		if(areaidref.equals("0")){
	  %>
	  <td bgcolor="red" height="22">
		 <input type="checkbox" name="test" id="<%=areaid%>" value="<%=areaid%>" onclick="checkall('<%=areaid%>','<%=areaidlen%>')">
	  </td>	
	  <%
	  		}else{
				if(rem1.equals(headerId)){
	  %>
	  <td bgcolor="#E6F2FF" height="22">  
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="check" id="<%=areaid%>" value="<%=areaid%>" checked>
	  </td>
	  <%
	  			}else{
	  %>  
	  <td bgcolor="#E6F2FF" height="22">  
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="check" id="<%=areaid%>" value="<%=areaid%>">
	  </td>
	  <%		}
			}if(areaidref.equals("0")){
	  %>  
	  <td bgcolor="red" height="22">
	   <b><%=areaid%></b>
	  </td>
	  <td bgcolor="red" height="22"><b><%=areaname%></b></td>
	  <%
	  		}else{
	  %>
		  <td bgcolor="#E6F2FF" height="22"><%=areaid%></td>
		  <td bgcolor="#E6F2FF" height="22"><%=areaname%></td>
	  <%
	  		}
	  %>   
		  </tr>
	  <%}%>
	  		
	  <input type="hidden" name="headerId"  value="<%=headerId%>">
      <input type="hidden" name="deletedIds" value=""/>
      <input type="hidden" name="addedIds" value=""/>
      </html:form>
    </table>

  </body>
</html>

<script>
	function checkall(send1,send2)
	{
		var a = send1;
		var c = send2;
		//alert(a+"	"+c);
		var check = new Array();
		check = document.getElementsByName("check");
		//alert(check.length);
		for(i=0;i<check.length;i++)
		{
			var b = "";
			//if(c == '3')
			//{
				b = check[i].value.substring(0,c);
			//}
			//else
			//{
				//b = check[i].value.substring(0,5);
			//}
			//alert(a + "	"+b);
			if(a == b)
			{
				if(document.getElementById(a).checked == true)
				check[i].checked = true;
				else
				check[i].checked = false;
			}
		}

	}
</script>