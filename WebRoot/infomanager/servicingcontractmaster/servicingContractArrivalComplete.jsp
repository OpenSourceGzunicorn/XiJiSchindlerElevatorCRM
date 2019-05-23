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
<html:form action="/ServicingContractMasterTaskSearchAction.do" enctype="multipart/form-data">
<input type="hidden" name="typejsp" id="typejsp" value="${ typejsp}"/>
<a href="" id="avf" target="_blank"></a>
<logic:equal name="typejsp" value="search">
<div id="divshoww" style="display:inline;">
	<%@ include file="searchOne.jsp" %>
</div>
 </logic:equal> 
<logic:equal name="typejsp" value="arrival">
<div id="divshoww" style="display:inline;">
	<%@ include file="searchTwo.jsp" %>
</div>
 </logic:equal> 
 <logic:equal name="typejsp" value="complete">
<div id="divshoww" style="display:inline;">
	<%@ include file="searchThree.jsp" %>
</div>
 </logic:equal>
  <logic:equal name="typejsp" value="display">
<div id="divshoww" style="display:inline;">
	<%@ include file="/engcontractmanager/servicingcontract/servicingContractDisplay.jsp" %>
</div>
 </logic:equal> 
</html:form>



