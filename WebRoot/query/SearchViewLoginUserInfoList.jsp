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
		<td>用户ID：</td>
		<td><html:text property="property(userid)" styleClass="default_input"/></td>
		
		<td>&nbsp;&nbsp;用户名：</td>
		<td><html:text property="property(username)" styleClass="default_input"/></td>
		
	  	<html:hidden property="property(genReport)" styleId="genReport"/>
	  </tr>
	</table>
	<br>
    <table:table id="guiSearchViewLoginUserInfoList" name="searchViewLoginUserInfoList">
      <logic:iterate id="element" name="searchViewLoginUserInfoList">
		<table:define id="c_cb">
        	<html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="userid"/>
        	</html:multibox>
			<html:hidden name="element" property="username" styleId="Sname"/>
			<html:hidden name="element" property="rolename" styleId="Stype1"/>  
			<html:hidden name="element" property="rolename" styleId="Stype2"/> 
		</table:define>
		
		<table:define id="c_userid">
        <bean:write name="element"  property="userid"/>
		</table:define>
		
		<table:define id="c_username">
        <bean:write name="element" property="username"/>
		</table:define>
		
		<table:define id="c_rolename">
        <bean:write name="element"  property="rolename"/>
		</table:define>
		
		<table:define id="c_rem1">
        <bean:write name="element"  property="rem1"/>
		</table:define>
		
        <table:tr/>
      </logic:iterate>
    </table:table>
 </html:form>