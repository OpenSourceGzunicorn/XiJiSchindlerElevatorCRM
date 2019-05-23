<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
	<html:form action="/ServeTable.do">
	<table border="0" cellpadding="5" cellspacing="0" nowrap="nowrap">
	  <tr>
		<td>ID：<html:text property="property(flowid)" styleClass="default_input" /></td>
		<td>名称：<html:text property="property(flowname)" styleClass="default_input" /></td>
		<td>版本：<html:select property="property(version)">
				<html:option value="0">最新版</html:option>
				<html:option value="-1">所有版本</html:option>
				</html:select>
		</td>
		</tr>
	  	<html:hidden property="property(genReport)" styleId="genReport"/>
	</table>	<div class="Tblank"></div>
	<table border="0" cellpadding="0" cellspacing="0" width="99%">
	  <tr class="header">
      <td width="9" style="background:url(/drp/common/images/tableTitleLeft.gif) left top"></td>
	  <td width="99%"></td>
	  <td width="9" style="background:url(/drp/common/images/tableTitleRight.gif) right top"></td>
	 </tr>
	</table>
    <table:table id="guiFlowList" name="FlowList">
      <logic:iterate id="element" name="FlowList">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="pid"/>
        	</html:multibox--%>
        	<bean:define id="pid" name="element" property="pid" />
			<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=pid.toString()%>" />
		</table:define>
		<table:define id="c_flowid">
       <a href="<html:rewrite page='/flowAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="pid" filter="false"/>"><bean:write name="element"  property="pname" filter="false"/></a>
		</table:define>
		<table:define id="c_flowname">
        <bean:write name="element" property="paliasname"/>
        <input type="hidden" name="paliasname" value="${element.paliasname }"/>
		</table:define>
		<table:define id="c_version">
        <bean:write name="element" property="ver"/>
		</table:define>
        <table:tr/>
      </logic:iterate>
    </table:table>
 </html:form>