<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
 
<br>
	<html:form action="/ServeTable.do">
	<font color="red">注意:此在没有和开发方协调的情况下，不能修改此处的内容。</font>
	<table>
	  <tr>
		<td><bean:message key="Track.jnlno"/>：</td><td><html:text property="property(wsid)" styleClass="default_input"/></td>
		<td>&nbsp;<bean:message key="workspacebasefit.wskey"/>：</td><td><html:text property="property(wskey)" styleClass="default_input"/></td>
		<td>&nbsp;<bean:message key="contract.filetitle"/>：</td><td><html:text property="property(title)" styleClass="default_input"/></td>
	  	<td>&nbsp;<bean:message key="workspacebasefit.link"/>：</td><td><html:text property="property(link)" styleClass="default_input"/></td>
	  </tr>
	  <tr>
		<td>&nbsp;<bean:message key="workspacebasefit.tip"/>：</td><td><html:text property="property(tip)" styleClass="default_input"/></td>
		<td>&nbsp;<bean:message key="DivId"/>：</td><td><html:text property="property(divid)" styleClass="default_input"/></td>
		<td>&nbsp;<bean:message key="JSfuName"/>：</td><td><html:text property="property(jsfuname)" styleClass="default_input"/></td>
		<td>&nbsp;<bean:message key="workspacebasefit.enabledflag"/>：</td>
		<td><html:select property="property(enabledflag)"><html:option value=""><bean:message key="pageword.all"/></html:option><html:option value="Y"><bean:message key="pageword.yes"/></html:option><html:option value="N"><bean:message key="pageword.no"/></html:option></html:select></td>
	  	<html:hidden property="property(genReport)" styleId="genReport"/>
	  </tr>
	</table>
	<br>
    <table:table id="guiworkspacebasefitList" name="workspacebasefitList">
      <logic:iterate id="element" name="workspacebasefitList">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="wsid"/>
        	</html:multibox--%>
        	<bean:define id="wsid" name="element" property="wsid" />
			<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=wsid.toString()%>" />
		</table:define>
		<table:define id="c_wsid">
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
		<table:define id="c_numno">
        <bean:write name="element" property="numno"/>
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