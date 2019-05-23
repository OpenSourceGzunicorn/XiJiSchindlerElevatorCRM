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
		<td><bean:message key="Track.jnlno"/>£º</td><td><html:text property="property(wsid)" styleClass="default_input"/></td>
		<td>&nbsp;<bean:message key="workspacebasefit.wskey"/>£º</td><td><html:text property="property(wskey)" styleClass="default_input"/></td>
		<td>&nbsp;<bean:message key="contract.filetitle"/>£º</td><td><html:text property="property(title)" styleClass="default_input"/></td>
	  </tr>
	  <tr>
		<td><bean:message key="workspacebasefit.link"/>£º</td><td><html:text property="property(link)" styleClass="default_input"/></td>
		<td>&nbsp;<bean:message key="workspacebasefit.tip"/>£º</td><td><html:text property="property(tip)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="role.enabledflag"/>£º</td>
		<td><html:select property="property(enabledflag)"><html:option value=""><bean:message key="pageword.all"/></html:option><html:option value="Y"><bean:message key="pageword.yes"/></html:option><html:option value="N"><bean:message key="pageword.no"/></html:option></html:select></td>
	  	<html:hidden property="property(genReport)" styleId="genReport"/>
	  </tr>
	</table>
	<br>
    <table:table id="guiworkspacebasefitList" name="workspacebasefitList">
      <logic:iterate id="element" name="workspacebasefitList">
		<table:define id="c_cb">
        	<html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="wsid"/>
        	</html:multibox>
		</table:define>
		<table:define id="c_wsid">
        <input type="hidden" name="wsid" id="wsid" value='<bean:write name="element"  property="wsid" filter="false"/>'>
		<input type="hidden" name="title" id="title" value='<bean:write name="element"  property="title" filter="false"/>'>
       <a href="<html:rewrite page='/workSpaceBaseFitAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="wsid" filter="false"/>"><bean:write name="element"  property="wsid" filter="false"/></a>
		</table:define>
		<table:define id="c_wskey">
        <bean:write name="element" property="wskey"/>
		</table:define>
		<table:define id="c_title">
        <bean:write name="element" property="title"/>
		</table:define>
		<table:define id="c_link">
        <bean:write name="element" property="link"/>
		</table:define>
		<table:define id="c_tip">
        <bean:write name="element" property="tip"/>
		</table:define>
		<table:define id="c_divid">
        <bean:write name="element" property="divid"/>
		</table:define>
		<table:define id="c_jsfuname">
        <bean:write name="element" property="jsfuname"/>
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