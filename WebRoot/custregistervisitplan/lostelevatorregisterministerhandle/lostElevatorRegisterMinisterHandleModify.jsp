<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/lostElevatorRegisterMinisterHandleAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<logic:present name="custReturnRegisterMasterBean">
<%@ include file="/custregistervisitplan/lostelevatorregistermaster/display.jsp" %>
</logic:present>
</html:form>