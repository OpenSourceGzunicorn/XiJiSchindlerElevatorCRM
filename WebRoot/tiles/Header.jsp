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
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='stylessCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='jeditCSS'/>">
<script language="javascript" src="<html:rewrite forward='webToolbarJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='checkInputJS'/>"></script>
<script>
var _sLock=0;
function saveLock(msg){
	if(_sLock==0){
		_sLock=1;
		return true;
	}else{
		if(msg==null){
			alert("抱歉! 不能重复提交数据．");
		}else{
			alert(msg);
		}
		return false;
	}
}
function unLock(){
	_sLock=0;
}
</script>
</head>