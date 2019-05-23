<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<style>
  .show{display:block;}
  .hide{display:none;}

</style>
<br>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
  <html:form action="/entrustContractMasterAction.do?method=toAuditRecord">
<logic:present name="entrustContractMasterBean">

<html:hidden name="entrustContractMasterBean" property="billNo"/>
<html:hidden property="isreturn"/>
<html:hidden property="auditStatus"/>
<html:hidden property="submitType"/>
	<%@ include file="display.jsp" %>
</logic:present>
</html:form>
</body>