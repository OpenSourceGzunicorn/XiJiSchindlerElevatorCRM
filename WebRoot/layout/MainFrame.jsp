<%@ page contentType="text/html;charset=gb2312" language="java" %>
<%@ page import="com.gzunicorn.common.util.CommonUtil" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%
String JumpURL=(String)request.getParameter("JumpURL");
String mfid=(String)request.getParameter("NodeId");
JumpURL=CommonUtil.getUrlDecode(JumpURL,null);
System.out.println("JumpUrl="+JumpURL+"	mfid="+mfid); 
%>
<body style=" overflow:hidden;">
<iframe src="<%=JumpURL%>" style="width:100%; height:100%;" frameborder="0"></iframe>
</body>