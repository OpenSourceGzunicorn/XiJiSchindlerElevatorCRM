<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
<br>
	<html:form action="/ServeTable.do">
	<table>
	  <tr>
	  <td align="right">所属部门:</td>
	  	<td>
	  	<html:select property="property(storageid)">
			<html:option value=""> <bean:message key="XJSCRM.zhz" /></html:option>
			<html:options collection="storageidList" property="storageid" labelProperty="storagename"/>
	  	</html:select>
	  	</td>
	  	<td align="right">用户角色:</td>	
	  	<td>
	  	<html:select property="property(roleid)">
	  	    <html:option value="">请选择</html:option>
			<html:options collection="roleList" property="roleid" labelProperty="rolename"/>
	  	</html:select>
	  	</td>
	  	<td align="right">&nbsp;&nbsp;所属分部:</td>
	  	<td><html:select property="property(grcid)">
	  	<html:options collection="grclist" property="grcid" labelProperty="grcname"/>
	  	</html:select></td>
	  	</tr>
	  	<tr>
		<td align="right"><bean:message key="loginuser.userid"/>:</td><td><html:text property="property(userid)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="loginuser.username"/>:</td><td><html:text property="property(username)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;电话号码:</td><td><html:text property="property(phone)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="loginuser.enabledflag"/>:</td><td><html:select property="property(enabledflag)"><html:option value=""><bean:message key="pageword.all"/></html:option><html:option value="Y"><bean:message key="pageword.yes"/></html:option><html:option value="N"><bean:message key="pageword.no"/></html:option></html:select></td>
	  	<html:hidden property="property(genReport)" styleId="genReport"/>
	  </tr>
	</table>
	<br>
    <table:table id="guiLoginUser" name="loginuserList">
      <logic:iterate id="element" name="loginuserList">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="userid"/>
        	</html:multibox--%>
        	<bean:define id="userid" name="element" property="userid" />
			<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=userid.toString()%>" />
		</table:define>
		<table:define id="c_userid">
        <a href="<html:rewrite page='/loginuserAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="userid"/>"><bean:write name="element"  property="userid"/></a>
		</table:define>
		<table:define id="c_username">
        <bean:write name="element" property="username"/>
		</table:define>
		<table:define id="c_class1id">
        <bean:write name="element" property="class1id"/>
		</table:define>
		<table:define id="c_storageid">
        <bean:write name="element" property="storageid"/>
		</table:define>
		<table:define id="c_roleid">
        <bean:write name="element" property="roleid"/>
		</table:define>
		<%--table:define id="c_dutyid">
        <bean:write name="element" property="dutyid"/>
		</table:define--%>
		<table:define id="c_grcid">
		<bean:write name="element" property="grcid"/>
		</table:define>
		<table:define id="c_enabledflag">
		<logic:match name="element" property="enabledflag" value="Y">
		<bean:message key="pageword.yes"/>
		</logic:match>
		<logic:match name="element" property="enabledflag" value="N">
		<bean:message key="pageword.no"/>
		</logic:match>
		</table:define>
        <table:tr/>
      </logic:iterate>
    </table:table>
 </html:form>