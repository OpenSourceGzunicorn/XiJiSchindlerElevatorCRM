<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='contractCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<html:errors/> 
<html:form action="/wgchangeContractAuditAction.do?method=tosaveAuditing" enctype="multipart/form-data">
<input type="hidden" name="typejsp" id="typejsp" value="${ typejsp}"/>
<a href="" id="avf" target="_blank"></a>
 <logic:equal name="typejsp" value="Auditing">
<div id="divshoww" style="display:inline;">
	<%@ include file="servicingContractDisplay.jsp" %>
</div>
 </logic:equal>
</html:form>



