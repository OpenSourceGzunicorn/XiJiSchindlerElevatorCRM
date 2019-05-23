<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles"%>
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
</head>
<br>

<html:form action="/PullDownAction.do?method=toAddRecord">
	<html:hidden property="isreturn" />
	<body>
		<table width="100%" class="tb">
			<tr>
				<td width="20%"  class="wordtd">
					代码：
				</td>
				<td width="80%" >
					<html:text property="pullid" styleClass="default_input" size="30"/>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="wordtd">
					名称：
				</td>
				<td>
					<html:text property="pullname" styleClass="default_input" size="30"/>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="wordtd">
					排序号：
				</td>
				<td>
					<html:text property="orderby" styleId="orderby" styleClass="default_input" size="30" onkeypress="f_check_number()"/>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="wordtd">
					类型：
				</td>
				<td>
					<html:text property="typeflag" styleClass="default_input" size="50"/>
					<font color="red">*</font>
				</td>
			</tr>
		  <tr>
		    <td class="wordtd"><bean:message key="loginuser.enabledflag"/>：</td>
		    <td class="inputtd"><html:radio property="enabledflag" value="Y" /><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
		  </tr>
			<tr>
				<td class="wordtd">
					描述：
				</td>
				<td>
					<html:textarea property="pullrem" rows="5" cols="50" styleClass="default_textarea"/>
					<font color="red">*</font>
				</td>
			</tr>
		</table>
	</body>
</html:form>
<html:javascript formName="pulldownForm" />
