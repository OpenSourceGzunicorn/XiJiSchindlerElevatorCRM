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
		<td><bean:message key="storageid.storageid"/>:</td><td><html:text property="property(storageid)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="storageid.storagename"/>:</td><td><html:text property="property(storagename)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="storageid.enabledflag"/>:</td><td><html:select property="property(enabledflag)"><html:option value=""><bean:message key="pageword.all"/></html:option><html:option value="Y"><bean:message key="pageword.yes"/></html:option><html:option value="N"><bean:message key="pageword.no"/></html:option></html:select></td>
	  	<td>&nbsp;&nbsp;À˘ Ù∑÷≤ø£∫</td><td><html:select property="property(grcid)"><html:options collection="grcidlist" property="grcid" labelProperty="grcname"/></html:select></td>
	  	<html:hidden property="property(genReport)" styleId="genReport"/>
	  </tr>
	</table>
	<br>
    <table:table id="guiStorageID" name="storageidList">
      <logic:iterate id="element" name="storageidList">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="storageid"/>
        	</html:multibox--%>
        	<bean:define id="storageid" name="element" property="storageid" />
			<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=storageid.toString()%>" />
		</table:define>
		<table:define id="c_storageid">
        <a href="<html:rewrite page='/storageidAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="storageid"/>"><bean:write name="element"  property="storageid"/></a>
		</table:define>
		
		<table:define id="c_storagename">
        <bean:write name="element" property="storagename"/>
		</table:define>
		<table:define id="c_storagehead">
        <bean:write name="element"  property="storagehead"/>
		</table:define>
		<table:define id="c_phone">
        <bean:write name="element" property="phone"/>
		</table:define>
		<table:define id="c_comname">
        <bean:write name="element" property="comname"/>
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