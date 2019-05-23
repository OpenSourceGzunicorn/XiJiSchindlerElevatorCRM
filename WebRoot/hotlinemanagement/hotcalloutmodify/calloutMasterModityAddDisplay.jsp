<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='contractCSS'/>">
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<html:errors/> 

<html:form action="/hotCalloutModifyAction.do?method=toUpdateRecord" enctype="multipart/form-data">

	<%@ include file="/hotlinemanagement/hotcalloutmaster/calloutMasterDisplay.jsp" %>

</html:form>



