<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<br>
<html:errors/>
<html:form action="/lostElevatorReportAction.do?method=toDisplayRecord">
<%@ include file="display.jsp" %>
<logic:notPresent name="isOpen">
	<%@ include file="audit.jsp" %>
</logic:notPresent>
</html:form>
<script type="text/javascript">
  	//$(".audit.show2").hide();
  	$(".audit.hide2").hide();
  	//$(".audit.show3").hide();
  	$(".audit.hide3").hide();
    //$(".audit.show").hide();
    $(".audit.hide").hide();
  </script>
  