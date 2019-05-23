<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>

<bean:parameter id="path" name="path"/>

<script language="javascript">
function window_onload()
{
  var l_sURL;
	//l_sURL = '<bean:write name="path"/>';
	 
	 <%String path1 = request.getParameter("path");
	 try{
	  path1 = new String(path1.getBytes("iso-8859-1"),"gb2312");}
	  catch(Exception e )
	  {
	  
	  }
	 %>
	 l_sURL = '<%=path1%>';
	 
	 //alert("url="+l_sURL);
 
    document.frames("iFrm").document.location = l_sURL;
 
  //document.getElementById("iFrm").src = l_sURL;
}
</script>
<html>
<html:base />
<head>
<title></title>
</head>
<body leftmargin="0" topmargin="0" onload="javascript:window_onload();">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%">

      <iframe id="iFrm" name="iFrm" src="" width="100%" height="725" scrolling="no" marginWidth="0" marginHeight="0" frameBorder="0" >
      </iframe>
    </td>
  </tr>
</table>
</body>
</html>
<script>
function returnuu(obj)
{
	window.returnValue = obj;
	window.close();
}
</script>