<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>

<!--
	控制通用CSS的调用
-->
<html>
<html:base/>
<head>
<title><bean:message key="app.title"/></title>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='queryCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='publicCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='toolbarCSS'/>">
<script language="javascript" src="<html:rewrite forward='webToolbarJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='checkInputJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='createTreeJS1'/>"></script>
<script language="javascript" src="<html:rewrite forward='queryJS'/>"></script>
</head>