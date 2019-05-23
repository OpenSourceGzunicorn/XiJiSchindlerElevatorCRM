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
  <html:form action="/maintContractDelayAuditAction.do?method=toAuditRecord">
  	<input type="hidden" name="id" value="${delayBean.jnlno}" />
    <html:hidden name="delayBean" property="submitType"/>
    <html:hidden name="delayBean" property="auditStatus"/>
    <%-- <a href="" id="avf" target="_blank"></a>
    <input type="hidden" name="flowname" value="${flowname}"/>
    <html:hidden name="delayMaster" property="status" />   
    <html:hidden name="delayMaster" property="tokenId" /> --%>
    <%@ include file="display.jsp" %>
    <%@ include file="audit.jsp" %>
    <%-- <%@ include file="/workflow/processApproveMessage.jsp" %> --%>
  </html:form>
  <script type="text/javascript">
    $(".audit.show").hide();
    $(".audit.hide").show();
  </script>
</body>