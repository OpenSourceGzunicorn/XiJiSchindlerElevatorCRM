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
  <html:form action="/lostElevatorReportAudit3Action.do?method=toAuditRecord">
  	<input type="hidden" name="id" value="${lostElevatorReportBean.jnlno}" />
    <html:hidden name="lostElevatorReportBean" property="submitType"/>
    <html:hidden name="lostElevatorReportBean" property="auditStatus3"/>
    <%@ include file="display.jsp" %>
    <%@ include file="audit.jsp" %>
  </html:form>
  
  <script type="text/javascript">
	$(".audit.show3").hide();
	$(".audit.hide3").show();
  	//$(".audit.show2").hide();
	$(".audit.hide2").hide();
  	//$(".audit.show").hide();
  	$(".audit.hide").hide();
  </script>
</body>