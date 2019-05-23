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
<html:form action="/wgchangeJbpmAction.do" enctype="multipart/form-data">
<html:hidden property="status" />
<html:hidden property="taskId" styleId="taskId"/>
<html:hidden property="tokenId" styleId="tokenId"/>
<html:hidden property="taskType" styleId="taskType"/>
<html:hidden property="taskName" styleId="taskName"/>
<html:hidden property="flowname" styleId="flowname"/>
<input type="hidden" name="typejsp" id="typejsp" value="${ typejsp}"/>
<a href="" id="avf" target="_blank"></a>

<logic:equal name="typejsp" value="display">
<div id="divshoww" style="display:inline;">
	<%@ include file="/engcontractmanager/servicingcontractquotemaster/Wgdisplay.jsp" %>
</div>
 </logic:equal>
 <logic:equal name="typejsp" value="mondity">
<div id="divshoww" style="display:inline;">
	<%@ include file="/engcontractmanager/servicingcontractquotemaster/Wgmondity.jsp" %>
</div>
 </logic:equal>
 <%@ include file="/workflow/approveResult.jsp" %>
 <%@ include file="/workflow/processApproveMessage.jsp" %>	
</html:form>



