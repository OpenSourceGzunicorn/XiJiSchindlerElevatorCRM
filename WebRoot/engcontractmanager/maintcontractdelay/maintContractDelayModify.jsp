<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<br>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
  <html:form action="/maintContractDelayAction.do?method=toUpdateRecord">
    <html:hidden property="id" styleId="deljnlno"/>
    <html:hidden property="isreturn" />
    <%@ include file="display.jsp" %>
  </html:form>  
  <script type="text/javascript">
    $(".show").hide();
    $(".hide").show();     
  </script>
</body>