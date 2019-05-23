<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
<br>
	<html:form action="/ServeTable.do">
	<table border="0" cellpadding="2" cellspacing="0">
	  <tr>
		<td><bean:message key="TaskExamplesSet.taskid"/>£º<html:text property="property(taskid)" styleId="taskid" styleClass="default_input"/></td>
		<td><bean:message key="TaskExamplesSet.token"/>£º<html:text property="property(token)" styleId="token" styleClass="default_input"/></td>
		<td><bean:message key="TaskExamplesSet.nodename"/>£º<html:text property="property(nodename)" styleId="nodename" styleClass="default_input"/></td>
		<td><bean:message key="TaskExamplesSet.isopen"/>£º
			<html:select property="property(isopen)" styleId="isopen">
			<html:option value="-1"><bean:message key="enabledflag.all"/></html:option>
			<html:option value="0"><bean:message key="enabledflag.yes"/></html:option>
			<html:option value="1"><bean:message key="enabledflag.no"/></html:option>
			</html:select>
		</td>
	 </tr>
		<html:hidden property="property(genReport)" styleId="genReport"/>
	</table>
	<br>
	<table:table id="guiTaskExamplesSet" name="TaskExamplesSetList">
      <logic:iterate id="element" name="TaskExamplesSetList">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="taskid"/>
        	</html:multibox--%>
        	<bean:define id="taskid" name="element" property="taskid" />
			<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=taskid.toString()%>" />
			
        	<html:hidden name="element" property="isopenid" styleId="isopenid"/>
		</table:define>
		
		<table:define id="c_taskid">
        <a href="<html:rewrite page='/taskExamplesSetAction.do'/>?method=toDisplayRecord&id=<bean:write name="element" property="taskid"/>&isopenid=<bean:write name="element" property="isopenid"/>"><bean:write name="element"  property="taskid" filter="false"/></a>
		</table:define>
		
		<table:define id="c_taskname">
        	<bean:write name="element" property="taskname" filter="false"/>
		</table:define>
		<table:define id="c_actorid">
        	<bean:write name="element" property="actorid" filter="false"/>
		</table:define>
		<table:define id="c_actorname">
        	<bean:write name="element" property="actorname" filter="false"/>
		</table:define>
		<table:define id="c_token">
        	<bean:write name="element" property="token" filter="false"/>
		</table:define>
		<table:define id="c_creatdate">
        	<bean:write name="element" property="creatdate" filter="false"/>
		</table:define>
		<table:define id="c_enddate">
        	<bean:write name="element" property="enddate" filter="false"/>
		</table:define>
		<table:define id="c_nodename">
        	<bean:write name="element" property="nodename" filter="false"/>
		</table:define>		
		<table:define id="c_isopen">
			<div align="center">
				<logic:match name="element" property="isopenid" value="0">
					<bean:message key="enabledflag.yes"/>
				</logic:match>
				<logic:match name="element" property="isopenid" value="1">
					<bean:message key="enabledflag.no"/>
				</logic:match>
			</div>
		</table:define>
        <table:tr/>
      </logic:iterate>
    </table:table>
 </html:form>