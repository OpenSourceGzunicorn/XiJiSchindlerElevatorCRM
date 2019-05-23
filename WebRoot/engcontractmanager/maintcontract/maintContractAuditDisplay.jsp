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
  <% request.setAttribute("doType", "audit"); %>
  <html:errors/>
  <html:form action="/maintContractAuditAction.do?method=toAuditRecord">
    <input type="hidden" name="id" value="${maintContractBean.billNo}" />
    <html:hidden name="maintContractBean" property="submitType"/>
    <html:hidden name="maintContractBean" property="auditStatus"/>   
    <%@ include file="display.jsp" %>
  </html:form>
  <%-- 
  <script type="text/javascript">
    $(".audit.show").hide();
    $(".audit.hide").show();
  </script>
  --%>
</body>