<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<br>
<head>
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
  <html:form action="/lostElevatorReportAction.do?method=toAddRecord" enctype="multipart/form-data">
    <%@ include file="addOrModify.jsp" %>
    <!-- 上传的附件 -->
	<%@ include file="UpLoadFile.jsp" %>
  </html:form>
</body>