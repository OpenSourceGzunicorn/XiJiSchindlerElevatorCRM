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
		<td><bean:message key="customer.placeid"/>:</td><td><html:text property="property(class1id)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="customer.placename"/>:</td><td><html:text property="property(class1name)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="customer.placestart"/>:</td><td><html:select property="property(enabledflag)"><html:option value=""><bean:message key="pageword.all"/></html:option><html:option value="Y"><bean:message key="pageword.yes"/></html:option><html:option value="N"><bean:message key="pageword.no"/></html:option></html:select></td>
	  	<html:hidden property="property(genReport)" styleId="genReport"/>
	  </tr>
	</table>
	<br>
    <table:table id="guiClass1" name="class1List">
      <logic:iterate id="element" name="class1List">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="class1id"/>
        	</html:multibox--%>
        	<bean:define id="class1id" name="element" property="class1id" />
			<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=class1id.toString()%>" />				
		</table:define>
		<table:define id="c_Class1ID">
        <a href="<html:rewrite page='/Class1Action.do'/>?method=toDisplayClass1&id=<bean:write name="element"  property="class1id"/>"><bean:write name="element"  property="class1id"/></a>
		</table:define>
		<table:define id="c_Class1Name">
        <bean:write name="element" property="class1name"/>
		</table:define>
		<table:define id="c_levelid">
        <bean:write name="element" property="levelid"/>
		</table:define>
		<table:define id="c_Reml">
        <bean:write name="element" property="rem1"/>
		</table:define>
		<table:define id="c_EnabledFlag">
		<logic:match name="element" property="enabledFlag" value="Y">
		<bean:message key="pageword.yes"/>
		</logic:match>
		<logic:match name="element" property="enabledFlag" value="N">
		<bean:message key="pageword.no"/>
		</logic:match>
		</table:define>
        <table:tr/>
      </logic:iterate>
    </table:table>
 </html:form>