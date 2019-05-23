<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
  <html:form action="/maintContractQuoteJbpmAction.do?method=toSaveUpdateApprove" enctype="multipart/form-data">
    <%@ include file="/engcontractmanager/maintcontractquote/addOrModify.jsp" %>   
    <%@ include file="/workflow/processApproveMessage.jsp" %>
    <%@ include file="/workflow/approveResult.jsp" %>
  </html:form>
</body>