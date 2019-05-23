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

<link rel="stylesheet" type="text/css" href="/WebRoot/common/css/publicCSS.jsp">
<link rel="stylesheet" type="text/css" href="/WebRoot/common/css/toolbar.css">
<link rel="stylesheet" type="text/css" href="/WebRoot/common/css/Calendar.css">
<link rel="stylesheet" type="text/css" href="/WebRoot/common/css/query.css">
<script language="javascript" src="/WebRoot/common/include/Calendar.js"></script>
<script language="javascript" src="/WebRoot/common/javascript/webtoolbar.js"></script>
<script language="javascript" src="/WebRoot/common/javascript/query.js"></script>
<script language="javascript" src="/WebRoot/common/javascript/i_CheckInput.js"></script>


</head>
<!-- 框架显示效果 -->
<body  style="background-color:#ffffff;">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr id="contentTr" name="contentTr" class="Default_contentTr">
    <td id="contentTd" name="contentTd" class="Default_contentTd">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <!--开始构造圆角上边框-->
        <tr id="frameTr" name="frameTr" class="Default_frameTr">
          <td id="frame_topLeftTd" name="frame_topLeftTd" class="Default_frame_topLeftTd">
          </td>
          <td id="frame_topMiddleTd" name="frame_topMiddleTd" class="Default_frame_topMiddleTd">
          </td>
          <td id="frame_topRightTd" name="frame_topRightTd" class="Default_frame_topRightTd">
          </td>
        </tr>
        <!--结束构造圆角上边框-->
        <tr>
          <td colspan="3" id="frame_outBorder" name="frame_outBorder" class="Default_frame_outBorder">
            <div id="frame_inBorder" name="frame_inBorder" class="Default_frame_inBorder">