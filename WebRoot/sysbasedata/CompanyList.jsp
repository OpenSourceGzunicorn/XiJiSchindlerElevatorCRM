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
		<td><bean:message key="company.comid"/>:</td><td><html:text property="property(comid)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="company.comfullname"/>:</td><td><html:text property="property(comfullname)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="company.enabledflag"/>:</td><td><html:select property="property(enabledflag)"><html:option value=""><bean:message key="pageword.all"/></html:option><html:option value="Y"><bean:message key="pageword.yes"/></html:option><html:option value="N"><bean:message key="pageword.no"/></html:option></html:select></td>
	  	<html:hidden property="property(genReport)" styleId="genReport"/>
	  </tr>
	</table>
	<br>
    <table:table id="guiCompany" name="companyList">
      <logic:iterate id="element" name="companyList">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="comid"/>
        	</html:multibox--%>
        	<bean:define id="comid" name="element" property="comid" />
			<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=comid.toString()%>" />
		</table:define>
		<table:define id="c_comid">
        <a href="<html:rewrite page='/companyAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="comid"/>"><bean:write name="element"  property="comid"/></a>
		</table:define>
		
		<table:define id="c_comfullname">
        <bean:write name="element" property="comfullname"/>
		</table:define>
		<table:define id="c_comtype">
        <bean:write name="element"  property="comtype"/>
		</table:define>
		<table:define id="c_linkman">
        <bean:write name="element" property="linkman"/>
		</table:define>
		<table:define id="c_linkmantel">
        <bean:write name="element" property="linkmantel"/>
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
 